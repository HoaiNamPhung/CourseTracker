package application;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddRowDialogController implements Initializable {

	@FXML
	private Label label;
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
	Database db;
	OverallView myOverallView;
	ObservableList<Entry> tableEntries;
	TableView<Entry> tableView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		db = Database.getDatabaseInstance();
	}
	
	public void getParentData(Stage stage, OverallView myOverallView, ObservableList<Entry> tableEntries, TableView<Entry> tableView) {
		this.stage = stage;
		this.myOverallView = myOverallView;
		this.tableEntries = tableEntries;
		this.tableView = tableView;
	}
	
	@FXML
	public void onBtnCancel() {
		if (stage != null) {
			stage.close();
		}
	}
	
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
		
		// Add to database and overallview, then update the table with new values by reloading it.
		boolean insertSuccess = myOverallView.createEntry(db, new Entry(fieldArr));
		if (insertSuccess) {
			tableEntries = FXCollections.observableArrayList(myOverallView.getSortedEntries());
			tableView.setItems(tableEntries);
			
			// Close the window.
			onBtnCancel();
		}
	}
}
