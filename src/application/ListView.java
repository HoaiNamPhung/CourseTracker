package application;

import java.time.LocalDateTime;

// Interface for manipulating a list.
public interface ListView {
	
	// Use the SQLite database -- see the methods I implemented in Database.java. Message me (Asyrium) on Discord if it's not working correctly.
	// If list doesn't exist, create a new one. If it does exist, query for all rows from storage system and initialize them in the app as a list.
	// Afterward, make the list visible.
	// Return number of rows initialized; thus, 0 if a new list was created.
	public int initializeList();

	// Booleans return true if successful, false if failed.
	boolean createEntry(Entry entry);
	boolean createNote(String course, String name, String description, String notes);
	boolean createTask(String course, String name, LocalDateTime dateTime, String description);
	
	// Deletion/update based on id, which will automatically be supplied by system when user acts on the corresponding row.
	boolean deleteEntry(int id);
	boolean modifyEntryCourse(int id, String course);
	boolean modifyEntryName(int id, String name);
	boolean modifyEntryDateTime(int id, LocalDateTime dateTime);
	boolean modifyEntryDescription(int id, String description);
	boolean modifyEntryNotes(int id, String notes);
}
