package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("OverallView.fxml"));
			primaryStage.setTitle("General");
			primaryStage.setScene(new Scene(root, 600, 900));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		BinarySearchTree.initializeBSTFromDB(Database.getDatabaseInstance());
		launch(args);
	}
}
