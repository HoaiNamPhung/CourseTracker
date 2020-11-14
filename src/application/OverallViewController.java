package application;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OverallViewController implements Initializable {
	
	
	@FXML private TableView<Entry> tableView = new TableView<>();
	@FXML private TableColumn<Entry, Integer> id;
	@FXML private TableColumn<Entry, String> course;
	@FXML private TableColumn<Entry, String> name;
	@FXML private TableColumn<Entry, LocalDate> date;
	@FXML private TableColumn<Entry, LocalTime> time;
	@FXML private TableColumn<Entry, String> description;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// Set up table columns
		id.setCellValueFactory(new PropertyValueFactory<Entry, Integer>("ID"));
		course.setCellValueFactory(new PropertyValueFactory<Entry, String>("Course"));
		name.setCellValueFactory(new PropertyValueFactory<Entry, String>("Name"));
		date.setCellValueFactory(new PropertyValueFactory<Entry, LocalDate>("Date"));
		time.setCellValueFactory(new PropertyValueFactory<Entry, LocalTime>("Time"));
		description.setCellValueFactory(new PropertyValueFactory<Entry, String>("Description"));
		tableView.getColumns().setAll(id, course, name, date, time, description);
		
		// Load data from entries in OverallView, which is obtained from database.
		Database db = new Database();
		OverallView myOverallView = new OverallView();
		myOverallView.initializeList(db);
		List<Entry> sortedEntries = myOverallView.getSortedEntries();
		// TODO: Make sure ObservableList is correctly being used to fill the table.
		ObservableList<Entry> tableEntries = FXCollections.observableArrayList(sortedEntries);
		tableView.setItems(tableEntries);
	}
}
