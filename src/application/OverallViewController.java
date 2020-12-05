package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OverallViewController implements Initializable {

	@FXML
	private TableView<Entry> tableView = new TableView<>();
	@FXML
	private TableColumn<Entry, String> course;
	@FXML
	private TableColumn<Entry, String> name;
	@FXML
	private TableColumn<Entry, LocalDate> date;
	@FXML
	private TableColumn<Entry, LocalTime> time;
	@FXML
	private TableColumn<Entry, String> description;
	@FXML
	private Button navBtn;
	
	// Variables
	Database db;
	OverallView myOverallView;
	ObservableList<Entry> tableEntries;
	Stage currStage;
	String currStageTitle;
	LocalTime meetingTime;
	

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Set up table columns
		course.setCellValueFactory(new PropertyValueFactory<Entry, String>("Course"));
		name.setCellValueFactory(new PropertyValueFactory<Entry, String>("Name"));
		date.setCellValueFactory(new PropertyValueFactory<Entry, LocalDate>("Date"));
		time.setCellValueFactory(new PropertyValueFactory<Entry, LocalTime>("Time"));
		description.setCellValueFactory(new PropertyValueFactory<Entry, String>("Description"));
		tableView.getColumns().setAll(course, name, date, time, description);

		// Load data from entries in OverallView, which is obtained from database.
		db = Database.getDatabaseInstance();
		myOverallView = new OverallView();
		myOverallView.initializeList(db);
		loadTable(myOverallView);
		
		// Set up listener for entry double click. On double click, go to entry details.
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	Entry selectedEntry = tableView.getSelectionModel().getSelectedItem();
		            	if (selectedEntry != null) {
		            		// In GeneralView and CourseView, show entry details on entry double click.
		            		// In CoursesView, update the view to show the double clicked course.
		            		currStage = (Stage) tableView.getScene().getWindow();
		            		currStageTitle = currStage.getTitle();
		            		switch (currStageTitle) {
		            			case "General":
		            				showEntryDetails(selectedEntry);
		            				break;
		            			case "Courses":
		            				loadCourseView(selectedEntry);
		            				break;
		            			default: // CourseView
		            				showEntryDetails(selectedEntry);
		            				break;
		            		}
		            	}
		            }
		        }
		    }
		});
	}
	
	/**
	 * Adapts the contents of an overallView into a table's rows.
	 * @param overallView The overall view being loaded into the table.
	 */
	public void loadTable(OverallView overallView) {
		tableEntries = FXCollections.observableArrayList(overallView.getSortedEntries());
		tableView.setItems(tableEntries);
	}
	
	// Delete a selected row.
    public void deleteRow() {
    	// Remove the row from the table.
    	Entry selectedEntry = tableView.getSelectionModel().getSelectedItem();
    	if (selectedEntry != null) {
    		tableView.getItems().remove(selectedEntry);
    		// Check if what is being deleted is a course.
    		if (currStageTitle.equals("Courses")) {
    			 db.delete("courses", selectedEntry.getId());
    			 
    		}
    		// Otherwise, we're deleting an entry.
    		else {
    			myOverallView.deleteEntry(db, selectedEntry);
    		}
    	}
    }
	
	/**
	 * Loads and shows the AddRowDialog window.
	 * @return Returns the new AddRowDialog window as a stage.
	 */
    @FXML
	public Stage showAddRowDialog() {
	
    	// Check whether what is being added is an entry or a course.
    	String fxmlUrl = "AddRowDialog.fxml";
    	String addType = "entry";
		currStage = (Stage) tableView.getScene().getWindow();
		currStageTitle = currStage.getTitle();
    	if (currStageTitle.equals("Courses")) {
    		fxmlUrl = "AddCourseDialog.fxml";
    		addType = "course";
    	}
    	
		// Create the new stage for the dialog.
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlUrl));
		Stage newStage = new Stage(StageStyle.DECORATED);
		try {
			newStage.setScene(new Scene(loader.load(), 300, 280));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newStage.setTitle("Add an " + addType);
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.initOwner(currStage);
		
		// Prepare the controller for either AddRowDialog or AddCourseDialog and pass over relevant data.
    	if (currStageTitle.equals("Courses")) {
    		AddCourseDialogController controller = loader.getController();
    		controller.getParentData(this, newStage);
    	}
    	else {
    		AddRowDialogController controller = loader.getController();
    		controller.getParentData(this, newStage);
    	}
		
		// Show the stage.
		newStage.show();
		return newStage;
	}
    
	/**
	 * Loads and shows the EntryDetails window for a given entry.
	 * @param selectedEntry The entry the entry details belong to.
	 * @return Returns the EntryDetails window as a stage.
	 */
	public Stage showEntryDetails(Entry selectedEntry) {
	
		// Create the new stage for the entry details window.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EntryDetails.fxml"));
		Stage newStage = new Stage(StageStyle.DECORATED);
		try {
			newStage.setScene(new Scene(loader.load(), 600, 800));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newStage.setTitle(selectedEntry.getName());
		// An entry's details should not block user from using other windows.
		newStage.initModality(Modality.NONE);
		currStage = (Stage) tableView.getScene().getWindow();
		currStageTitle = currStage.getTitle();
		newStage.initOwner(currStage);
		
		// Prepare the controller for entry details and pass over variables.
		EntryDetailsController controller = loader.getController();
		controller.getParentData(this, newStage, selectedEntry);
		
		// Show the stage.
		newStage.show();
		return newStage;
	}
	
	/**
	 * Updates the root window to show the next view according to what the current view is.
	 */
	@FXML
	public void onBtnNavigate() {
		// Get the current stage.
		currStage = (Stage) tableView.getScene().getWindow();
		currStageTitle = currStage.getTitle();
		
		// Change stage features.
		switch (currStageTitle) {
			case "Overall":
				currStage.setTitle("Courses");
				currStageTitle = "Courses";
				navBtn.setText("Back to General");
				break;
			case "Courses":
				currStage.setTitle("General");
				currStageTitle = "General";
				navBtn.setText("Courses");
				break;
			default: // CourseView
				currStage.setTitle("Courses");
				currStageTitle = "Courses";
				navBtn.setText("Back to General");
				break;
		}
		switchTableViewLoadout(currStageTitle);
	}
	
	// Update table and stage to show the course view.
	private void loadCourseView(Entry selectedEntry) {
		// Set instance field information about current course.
		currStageTitle = selectedEntry.getCourse();
		meetingTime = selectedEntry.getTime();
		
		// Update the view to show CourseView.
		currStage.setTitle(currStageTitle);
		navBtn.setText("Back to Courses");
		switchTableViewLoadout(currStageTitle);	
	}
	
	// Changes what the table is adapting and displaying based on the current view.
	private void switchTableViewLoadout(String newViewName) {
		switch (newViewName) {
			case "General":
				loadTable(myOverallView);
				break;
			case "Courses":
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
				break;
			default: // CourseView
				tableEntries = FXCollections.observableArrayList(myOverallView.getSortedEntries());
				// Remove all entries not corresponding to the current course.
				Iterator<Entry> iterator = tableEntries.iterator(); 
				while (iterator.hasNext()) {
				    Entry entry = iterator.next();
				    if (entry.getCourse() == null || entry.getCourse().isEmpty()) {
						if (!(newViewName == null || newViewName.isEmpty())) {
							iterator.remove();
						}
					}
					else if (!entry.getCourse().equals(newViewName)) {
						iterator.remove();
					}
				}
				// Display the remaining course-affiliated items on the table.
				tableView.setItems(tableEntries);
				break;
		}
	}
	
	
	/* Getters and Setters */
	public TableView<Entry> getTableView() {
		return tableView;
	}
	
	public OverallView getOverallView() {
		return myOverallView;
	}
	
	public ObservableList<Entry> getTableEntries() {
		return tableEntries;
	}
	
	public Stage getCurrStage() {
		return currStage;
	}
	
	public String getCurrStageTitle() {
		return currStageTitle;
	}
	public LocalTime getMeetingTime() {
		return meetingTime;
	}
    
    
}
