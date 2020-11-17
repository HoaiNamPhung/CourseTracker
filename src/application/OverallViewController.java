package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
	
	Database db;
	OverallView myOverallView;
	ObservableList<Entry> tableEntries;

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
	}
	
	public void loadTable(OverallView overallView) {
		tableEntries = FXCollections.observableArrayList(overallView.getSortedEntries());
		tableView.setItems(tableEntries);
	}
	
	@FXML
	public Stage showAddRowDialog() {
	
		// Create the new stage for the dialog.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRowDialog.fxml"));
		Stage stage = new Stage(StageStyle.DECORATED);
		try {
			stage.setScene(new Scene(loader.load(), 300, 280));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setTitle("Add an entry");
		stage.initModality(Modality.APPLICATION_MODAL);
		Stage currStage = (Stage) tableView.getScene().getWindow();
		stage.initOwner(currStage);
		
		// Prepare the controller for addRowDialog and pass over variables.
		AddRowDialogController controller = loader.getController();
		controller.getParentData(stage, myOverallView, tableEntries, tableView);
		
		// Show the stage.
		stage.show();
		return stage;
	}
}
