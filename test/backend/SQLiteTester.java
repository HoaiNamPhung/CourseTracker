package backend;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
		Database db = Database.getDatabaseInstance();
		List<Integer> ids = new ArrayList<>();
		db.drop("entries");
		ids.add(db.insert("entries", "cs151", "Assignment 4a", now, "JavaFX notes."));
		db.update("entries", 1, "notes", "These are indeed notes.");
		ids.add(db.insert("entries", "cs151", "Assignment 4b", now.plusHours(1), null));
		ids.add(db.insert("entries", "cs151", "Notes", now, null));
		ids.add(db.insert("entries", "cs149", "HW6", now.minusHours(2), null));
		ids.add(db.insert("entries", "cs149", "Notes", now.minusHours(1), null));
		ids.add(db.insert("entries", null, "Interview", now.minusHours(3), null));
		ids.add(db.insert("entries", "cs161", "Study", now, "Midterm 2"));
		ids.add(db.insert("entries", "cs161", "Study!!", now, null));
		ids.add(db.insert("entries", "cs185", "Project", now.minusHours(2), null));
		for (int i = 1; i <= ids.size(); i++) {
			assertEquals((int) ids.get(i - 1), i, "Inserted entry into database has unexpected id. Check insertion implementation.");
		}
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
		
		BinarySearchTree bst = BinarySearchTree.getBSTInstance();
		Entry entry1 = new Entry(rsEntries1);
		Entry entry2 = new Entry(rsEntries2);
		Entry entry3 = new Entry(rsEntries3);
		Entry entry4 = new Entry(rsEntries4);
		Entry entry5 = new Entry(rsEntries5);
		Entry entry6 = new Entry(rsEntries6);
		Entry entry7 = new Entry(rsEntries7);
		Entry entry8 = new Entry(rsEntries8);
		Entry entry9 = new Entry(rsEntries9);
		bst.insert(entry1);
		bst.insert(entry2);
		bst.insert(entry3);
		bst.insert(entry4);
		bst.insert(entry5);
		bst.insert(entry6);
		bst.insert(entry7);
		bst.insert(entry8);
		bst.insert(entry9);
		
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
		db.insert("courses", "cs151", now);
		db.insert("courses", "cs149", now);
		String[] rsCourses1 = db.query("courses", 1);
		String[] rsCourses2 = db.query("courses", 2);
		String[] rsCourses3 = db.query("courses", 3);
		System.out.println(Arrays.toString(rsCourses1));
		System.out.println(Arrays.toString(rsCourses2));
		System.out.println(Arrays.toString(rsCourses3));
		System.out.println("------------------------------");
		
		// Test queryAll().
		List<String[]> allCourses = db.queryAll("courses", null);
		List<String[]> failedQuery = db.queryAll("courses", "cs151");
		List<String[]> allEntries = db.queryAll("entries", null);
		List<String[]> entriesCs151 = db.queryAll("entries", "cs151");
		List<String[]> entriesCs149 = db.queryAll("entries", "cs149");
		List<String[]> entriesCs147 = db.queryAll("entries", "cs147");
		
		assertEquals(allCourses.size(), 2, "Incorrect number of entries read. Check queryAll() implementation.");
		assertEquals(failedQuery.size(), 0, "Incorrect number of entries read. Check queryAll() implementation.");
		assertEquals(allEntries.size(), 9, "Incorrect number of entries read. Check queryAll() implementation.");
		assertEquals(entriesCs151.size(), 3, "Incorrect number of entries read. Check queryAll() implementation.");
		assertEquals(entriesCs149.size(), 2, "Incorrect number of entries read. Check queryAll() implementation.");
		assertEquals(entriesCs147.size(), 0, "Incorrect number of entries read. Check queryAll() implementation.");
		
		System.out.println("All courses:");
		for (String[] row : allCourses) {
			System.out.println(Arrays.toString(row));
		}
		System.out.println("======");
		System.out.println("All entries:");
		for (String[] row : allEntries) {
			System.out.println(Arrays.toString(row));
		}
		System.out.println("======");
		System.out.println("All entries for CS151:");
		for (String[] row : entriesCs151) {
			System.out.println(Arrays.toString(row));
		}
		System.out.println("======");
		System.out.println("All entries for CS149:");
		for (String[] row : entriesCs149) {
			System.out.println(Arrays.toString(row));
		}
		System.out.println("======");
		
		// Test delete().
		System.out.println("Entries in chronological order after deleting entry 5.");
		bst.delete(entry5);
		entries = bst.inorderTraversal();
		System.out.println("------------------------------");
		
		System.out.println("Entries in chronological order after deleting oldest entry.");
		bst.delete(entry2);
		entries = bst.inorderTraversal();
		System.out.println("------------------------------");
		
		System.out.println("Entries in chronological order after deleting first entry.");
		bst.delete(entry6);
		entries = bst.inorderTraversal();
		System.out.println("------------------------------");
	}

}
