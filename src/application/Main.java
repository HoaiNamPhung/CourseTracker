package application;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Arrays;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		/* Testing database; get rid of this block if desired. */
		LocalDateTime now = LocalDateTime.now();
		Database db = new Database();
		db.open();
		db.drop("entries");
		db.drop("courses");
		db.createTable("entries");
		db.createTable("courses");
		db.insert("entries", "cs151", "Assignment 4", now, "description");
		db.insert("courses", "cs151", now);
		db.insert("courses", "cs149", now);
		db.update("entries", 1, "notes", "These are indeed notes.");
		String[] rsEntries1 = db.query("entries", 1);
		String[] rsEntries2 = db.query("entries", 2);
		String[] rsCourses1 = db.query("courses", 1);
		String[] rsCourses2 = db.query("courses", 2);
		String[] rsCourses3 = db.query("courses", 3);
		System.out.println(Arrays.toString(rsEntries1));
		System.out.println(Arrays.toString(rsEntries2));	// Should be null
		System.out.println(Arrays.toString(rsCourses1));
		System.out.println(Arrays.toString(rsCourses2));
		System.out.println(Arrays.toString(rsCourses3));	// Should be null
		/* **************************************************** */
		
		launch(args);
	}
}
