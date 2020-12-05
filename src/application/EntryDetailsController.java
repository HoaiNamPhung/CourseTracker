package application;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
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

public class EntryDetailsController implements Initializable {

	@FXML
	private TextField course;
	@FXML
	private TextField name;
	@FXML
	private DatePicker date;
	@FXML
	private TextField time;
	@FXML
	private TextField description;
	@FXML
	private TextArea notes;
	@FXML
	private Label errorMsg;
	
	// Variables passed from OverallViewController.
	Stage stage;
	Database db;
	OverallViewController ovc;
	OverallView myOverallView;
	ObservableList<Entry> tableEntries;
	TableView<Entry> tableView;
	Entry entry;
	String currStageTitle;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		db = Database.getDatabaseInstance();
	}
	
	/**
	 * Retrieve the newly created stage from a parent stage for use, along with the parent stage's context and data.
	 * @param ovc The parent stage.
	 * @param stage The newly created stage to be used as the AddRowDialog.
	 * @param entry The entry to load the details of.
	 */
	public void getParentData(OverallViewController ovc, Stage stage, Entry entry) {
		this.ovc = ovc;
		this.stage = stage;
		this.entry = entry;
		this.myOverallView = ovc.getOverallView();
		this.tableEntries = ovc.getTableEntries();
		this.tableView = ovc.getTableView();
		this.currStageTitle = ovc.getCurrStageTitle();
		
		// Initialize all the current values of the entry.
		name.setText(entry.getName());
		course.setText(entry.getCourse());
		date.setValue(entry.getDate());
		if (entry.getTime() == null) {
			time.setText("");
		}
		else {
			time.setText(entry.getTime().toString());
		}
		description.setText(entry.getDescription());
		notes.setText(entry.getNotes());
	}
	
	/**
	 * Close the window.
	 */
	@FXML
	public void onBtnClose() {
		if (stage != null) {
			stage.close();
		}
	}
	
	/**
	 * Save the current entry's details, if updated.
	 */
	@FXML
	public void saveDetails() {
		// Create a new copy of the current entry that will be updated. The old copy is used for comparison.
		Entry newEntry = null;
		try {
			newEntry = entry.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		// Clear the error message field.
		errorMsg.setText("");
		
		// Check each field for changes.
		boolean isChanged = false;
		if (!(name.getText() == null && entry.getName() == null)) {
			// Make sure name is not empty.
			if (name.getText().isEmpty()) {
				// If it is, restore the text field and do nothing.
				name.setText(entry.getName());
				errorMsg.setText(errorMsg.getText() + "Name cannot be empty; name field restored. \n");
			}
			if (!name.getText().equals(entry.getName())) {
				newEntry.setName(name.getText());
				isChanged = true;
			}
		}
		if (!(course.getText() == null && entry.getCourse() == null)) {
			if (!course.getText().equals(entry.getCourse())) {
				newEntry.setCourse(course.getText());
				isChanged = true;
			}
		}
		if (!(date.getValue() == null && entry.getDate() == null)) {
			if (MyDateTime.compareDate(date.getValue(), entry.getDate()) != 0) {
				newEntry.setDate(date.getValue());
				isChanged = true;
			}
		}
		if (!(time.getText().isEmpty() && entry.getTime() == null)) {
			// Time was removed.
			if (time.getText().isEmpty() && !(time.getText().equals(entry.getTime().toString())) ) {
				// Note that we can't set LocalTime to null, so make ado with 00:00.
				newEntry.setTime(LocalTime.MIN);
				isChanged = true;
			}
			// Time was modified.
			else if (!time.getText().isEmpty()) {
				try {
					LocalTime newTime = LocalTime.parse(time.getText(), DateTimeFormatter.ofPattern("H:mm"));
					if (MyDateTime.compareTime(newTime, entry.getTime()) != 0) {
						newEntry.setTime(newTime);
						isChanged = true;
					}
				}
				// New time is of an invalid format. Default to 00:00.
				catch (Exception e) {
					time.setText(LocalTime.MIN.toString());
					newEntry.setTime(LocalTime.MIN);
					isChanged = true;
					errorMsg.setText(errorMsg.getText() + "Time has invalid format; defaulted to 00:00. \n");
				}
			}
		}
		if (!(description.getText() == null && entry.getDescription() == null)) {
			if (!description.getText().equals(entry.getDescription())) {
				newEntry.setDescription(description.getText());
				isChanged = true;
			}
		}
		if (!(notes.getText() == null && entry.getNotes() == null)) {
			if (!notes.getText().equals(entry.getNotes())) {
				newEntry.setNotes(notes.getText());
				isChanged = true;
			}
		}
		
		// If fields were changed, update database and OverallView. Replace old entry with new entry.
		if (isChanged) {
			boolean insertSuccess = myOverallView.modifyEntry(db, entry, newEntry);
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
				entry = newEntry;
			}
		}
	}
}
