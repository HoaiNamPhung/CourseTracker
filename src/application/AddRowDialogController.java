package application;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
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

public class AddRowDialogController implements Initializable {

	@FXML
	private TextField courseField;
	@FXML
	private TextField nameField;
	@FXML
	private DatePicker dateField;
	@FXML
	private TextField timeField;
	@FXML
	private TextArea descriptionField;
	
	// Variables passed from OverallViewController.
	Stage stage;
	OverallViewController ovc;
	Database db;
	OverallView myOverallView;
	ObservableList<Entry> tableEntries;
	TableView<Entry> tableView;
	String currStageTitle;
	LocalTime meetingTime;
	
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
		myOverallView = ovc.getOverallView();
		tableEntries = ovc.getTableEntries();
		tableView = ovc.getTableView();
		currStageTitle = ovc.getCurrStageTitle();
		meetingTime = ovc.getMeetingTime();
		
		// If in CourseView, initialize course and time fields to be the same as the Course's by default.
		if (!(currStageTitle.equals("General") || currStageTitle.equals("Courses"))) {
			courseField.setText(currStageTitle);
			timeField.setText("");
			if (meetingTime != null) {
				timeField.setText(meetingTime.toString());	
			}
		}
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
		String course = courseField.getText();
		String name = nameField.getText();
		LocalDate date = dateField.getValue();
		String strTime = timeField.getText();
		LocalTime time = null;
		if (strTime != null && !strTime.equals("")) {
			DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("H:mm");
			time = LocalTime.parse(strTime, timeParser);
		}
		String dateTime = MyDateTime.toString(date, time);
		String description = descriptionField.getText();
		String[] fieldArr = {null, course, name, dateTime, description, null};
		
		// Add to database, then update the table with new values by reloading it.
		boolean insertSuccess = myOverallView.createEntry(db, new Entry(fieldArr));
		if (insertSuccess) {
			tableEntries = FXCollections.observableArrayList(myOverallView.getSortedEntries());
			
			// If currently in CourseView rather than OverallView, remove all unrelated entries.
			if (!(currStageTitle.equals("General") || currStageTitle.equals("Courses"))) {
				Iterator<Entry> iterator = tableEntries.iterator(); 
				while (iterator.hasNext()) {
				    Entry entry = iterator.next();
				    if (entry.getCourse() == null || entry.getCourse().isEmpty()) {
						if (!(currStageTitle == null || currStageTitle.isEmpty())) {
							iterator.remove();
						}
					}
					else if (!entry.getCourse().equals(currStageTitle)) {
						iterator.remove();
					}
				}
			}
			// Display the updated table.
			tableView.setItems(tableEntries);
			
			// Close the window.
			onBtnCancel();
		}
	}
}
