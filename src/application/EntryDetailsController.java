package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EntryDetailsController implements Initializable {

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
	@FXML
	private BorderPane rootPane;
	
	// Variables passed from OverallViewController.
	Database db;
	OverallViewController ovc;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		db = Database.getDatabaseInstance();
	}
	
	public void getParentData(OverallViewController ovc) {
		this.ovc = ovc;
	}
	
	// Load overall view in the same window.
	@FXML
    public void loadOverallView() {
    	try {
			BorderPane pane = FXMLLoader.load(getClass().getResource("OverallView.fxml"));
			rootPane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
