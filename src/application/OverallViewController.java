package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
	private BorderPane rootPane;
	
	// Variables
	Database db;
	OverallView myOverallView;
	ObservableList<Entry> tableEntries;
	

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
		            		showEntryDetails(selectedEntry);
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
        	myOverallView.deleteEntry(db, selectedEntry);
    	}
    }
	
	/**
	 * Loads and shows the AddRowDialog window.
	 * @return Returns the new AddRowDialog window as a stage.
	 */
    @FXML
	public Stage showAddRowDialog() {
	
		// Create the new stage for the dialog.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRowDialog.fxml"));
		Stage newStage = new Stage(StageStyle.DECORATED);
		try {
			newStage.setScene(new Scene(loader.load(), 300, 280));
		} catch (IOException e) {
			e.printStackTrace();
		}
		newStage.setTitle("Add an entry");
		newStage.initModality(Modality.APPLICATION_MODAL);
		Stage currStage = (Stage) tableView.getScene().getWindow();
		newStage.initOwner(currStage);
		
		// Prepare the controller for addRowDialog and pass over variables.
		AddRowDialogController controller = loader.getController();
		controller.getParentData(this, newStage);
		
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
		Stage currStage = (Stage) tableView.getScene().getWindow();
		newStage.initOwner(currStage);
		
		// Prepare the controller for entry details and pass over variables.
		EntryDetailsController controller = loader.getController();
		controller.getParentData(this, newStage, selectedEntry);
		
		// Show the stage.
		newStage.show();
		return newStage;
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
    
    /*
    public void loadEntryDetails(Entry entry) {
    	try {
			BorderPane pane = FXMLLoader.load(getClass().getResource("EntryDetails.fxml"));
			rootPane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    */
}
