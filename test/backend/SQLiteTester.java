package backend;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import application.BinarySearchTree;
import application.Database;
import application.Entry;

class SQLiteTester {

	@Test
	void test() {
		LocalDateTime now = LocalDateTime.now();
		Database db = new Database();
		db.open();
		db.drop("entries");
		db.createTable("entries");
		db.insert("entries", "cs151", "Assignment 4a", now, null);
		db.update("entries", 1, "notes", "These are indeed notes.");
		db.insert("entries", "cs151", "Assignment 4b", now.plusHours(1), null);
		db.insert("entries", "cs151", "Notes", now, null);
		db.insert("entries", "cs149", "HW6", now.minusHours(2), null);
		db.insert("entries", "cs149", "Notes", now.minusHours(1), null);
		db.insert("entries", null, "Interview", now.minusHours(3), null);
		db.insert("entries", "cs161", "Study", now, null);
		db.insert("entries", "cs161", "Study!!", now, null);
		db.insert("entries", "cs185", "Project", now.minusHours(2), null);
		String[] rsEntries1 = db.query("entries", 1);
		String[] rsEntries2 = db.query("entries", 2);
		String[] rsEntries3 = db.query("entries", 3);
		String[] rsEntries4 = db.query("entries", 4);
		String[] rsEntries5 = db.query("entries", 5);
		String[] rsEntries6 = db.query("entries", 6);
		String[] rsEntries7 = db.query("entries", 7);
		String[] rsEntries8 = db.query("entries", 8);
		String[] rsEntries9 = db.query("entries", 9);
		System.out.println("Entries in order of insertion to DB:");
		System.out.println(Arrays.toString(rsEntries1));
		System.out.println(Arrays.toString(rsEntries2));
		System.out.println(Arrays.toString(rsEntries3));
		System.out.println(Arrays.toString(rsEntries4));
		System.out.println(Arrays.toString(rsEntries5));
		System.out.println(Arrays.toString(rsEntries6));
		System.out.println(Arrays.toString(rsEntries7));
		System.out.println(Arrays.toString(rsEntries8));
		System.out.println(Arrays.toString(rsEntries9));
		System.out.println("-----------------------------");
		
		BinarySearchTree bst = new BinarySearchTree();
		bst.insert(new Entry(rsEntries1));
		bst.insert(new Entry(rsEntries2));
		bst.insert(new Entry(rsEntries3));
		bst.insert(new Entry(rsEntries4));
		bst.insert(new Entry(rsEntries5));
		bst.insert(new Entry(rsEntries6));
		bst.insert(new Entry(rsEntries7));
		bst.insert(new Entry(rsEntries8));
		bst.insert(new Entry(rsEntries9));
		
		System.out.println("Entries in chronological order, as printed by inorderTraversal():");
		List<Entry> entries = bst.inorderTraversal();
		System.out.println("------------------------------");
		
		int BST_idOrder[] = new int[9];
		System.out.println("Entries in order of output ArrayList<Entry> from inorderTraversal():");
		for (int i = 0; i < entries.size(); i++) {
			BST_idOrder[i] = entries.get(i).getId();
			String strOutput = entries.get(i).toString();
			System.out.println(strOutput);
		}
		System.out.println("------------------------------");
		
		// Test chronological order from BST.
		int[] expectedIdOrder = {6, 4, 9, 5, 1, 3, 7, 8, 2};
		assertTrue(Arrays.equals(BST_idOrder, expectedIdOrder), "Inserted entries are not sorted in the correct order. Check BST implementation.");
		
		System.out.println("Courses in order of insertion to DB:");
		db.drop("courses");
		db.createTable("courses");
		db.insert("courses", "cs151", now);
		db.insert("courses", "cs149", now);
		String[] rsCourses1 = db.query("courses", 1);
		String[] rsCourses2 = db.query("courses", 2);
		String[] rsCourses3 = db.query("courses", 3);
		System.out.println(Arrays.toString(rsCourses1));
		System.out.println(Arrays.toString(rsCourses2));
		System.out.println(Arrays.toString(rsCourses3));
		System.out.println("------------------------------");
	}

}
