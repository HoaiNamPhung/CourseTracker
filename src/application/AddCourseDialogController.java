package application;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCourseDialogController implements Initializable {

	@FXML
	private TextField courseNameField;
	@FXML
	private TextField meetingTimeField;
	@FXML
	private DatePicker startDateField;
	@FXML
	private TextArea courseDescriptionField;
	
	// Variables passed from OverallViewController.
	Stage stage;
	OverallViewController ovc;
	Database db;
	ObservableList<Entry> tableEntries;
	TableView<Entry> tableView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		db = Database.getDatabaseInstance();
	}
	
	/**
	 * Retrieve the newly created stage from a parent stage for use, along with the parent stage's context.
	 * @param ovc The parent stage.
	 * @param stage The newly created stage to be used as the AddRowDialog.
	 */
	public void getParentData(OverallViewController ovc, Stage stage) {
		this.ovc = ovc;
		this.stage = stage;
		tableEntries = ovc.getTableEntries();
		tableView = ovc.getTableView();
	}
	
	/**
	 * Close the window.
	 */
	@FXML
	public void onBtnCancel() {
		if (stage != null) {
			stage.close();
		}
	}
	
	/**
	 * Create a new entry based on the state of the user input fields.
	 */
	@FXML
	public void onBtnAdd() {
		// Parse fields for user input.
		String courseName = courseNameField.getText();
		LocalDate startDate = startDateField.getValue();
		String strTime = meetingTimeField.getText();
		LocalTime meetingTime = null;
		if (strTime != null && !strTime.equals("")) {
			DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("H:mm");
			meetingTime = LocalTime.parse(strTime, timeParser);
		}
		LocalDateTime meetingDateTime = null;
		if (meetingTime == null) {
			if (startDate != null) {
				meetingDateTime = startDate.atTime(LocalTime.MIN);
			}
		}
		else {
			if (startDate != null) {
				meetingDateTime = meetingTime.atDate(startDate);
			}
			else {
				meetingDateTime = meetingTime.atDate(MyDateTime.MAX_DATE);
			}
		}
		String courseDescription = courseDescriptionField.getText();
		
		// Add to database, then update the table with new values by reloading it.
		int rv = db.insert("courses", courseName, meetingDateTime, courseDescription);
		if (rv != -1) {
			// Query into SQLite database and get ALL courses.
			List<String[]> allCourses = db.queryAll("courses", null);
			
			// Convert them to a list of entries.
			List<Entry> allEntries = new ArrayList<>();
			for (String[] course : allCourses) {
				allEntries.add(new Entry(course, true));
			}
			
			// Adapt the courses to the table and display them.
			tableEntries = FXCollections.observableArrayList(allEntries);
			tableView.setItems(tableEntries);
			
			// Close the window.
			onBtnCancel();
		}
	}
}
