package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// List view that shows all existing entries.
public class OverallView implements ListView {
	
	BinarySearchTree entries;

	public OverallView(String courseName, LocalDateTime courseMeetingDateTime) {
		this.entries = new BinarySearchTree();
	}
	
	@Override
	/**
	 * Initializes the list of all entries from database and displays it chronologically on the screen.
	 * return Returns -1 if failed.
	 */
	public int initializeList(Database db) {
		// Query into SQLite database and get ALL rows.
		List<String[]> allEntries = db.queryAll("entries", null);
		
		// Add the rows to entries if entries exist.
		List<Entry> sortedEntries = null;
		if (allEntries != null) {
			for (String[] row : allEntries) {
				entries.insert(new Entry(row));
			}
			sortedEntries = entries.inorderTraversal();
		}
		
		// TODO: Display entries as a list using GUI. (Use sortedEntries for this; if it's null, no entries will be displayed in GUI).
		return 0;
	}
	
	@Override
	public boolean createEntry(Database db, Entry entry) {
		int id = db.insert("entries", entry.getCourse(), entry.getName(), entry.getDateTime(), entry.getDescription());
		entries.insert(entry);
		
		// TODO: Update overall list GUI to show new entry.
		
		
		// Return value based on successful insertion to database.
		if (id > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean createNote(Database db, String course, String name, String description, String notes) {
		// When we implement CourseView, we will also set the entry dateTime to the course's meetingDateTime, rather than null.
		int id = db.insert("entries", course, name, null, description);
		Entry newNote = new Entry(new String[] {Integer.toString(id), course, name, null, description, notes});
		entries.insert(newNote);
		
		// TODO: Update overall list GUI to show new entry.
		
		
		// Return value based on successful insertion to database.
		if (id > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean createTask(Database db, String course, String name, LocalDateTime dateTime, String description) {
		int id = db.insert("entries", course, name, dateTime, description);
		Entry newTask = new Entry(new String[] {Integer.toString(id), course, name, dateTime.toString(), description, null});
		entries.insert(newTask);
		
		// TODO: Update overall list GUI to show new entry.
		
		// Return value based on successful insertion to database.
		if (id > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteEntry(Database db, int id) {
		int rv = db.delete("entries", id);
		// Delete the entry from the binary search tree, once we actually need to implement it.
		
		
		// Update overall list GUI to no longer show removed entry.
		
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryCourse(Database db, int id, String course) {
		int rv = db.update("entries", id, "course", course);
		
		// Update overall list GUI to show updated entry.
		
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryName(Database db, int id, String name) {
		int rv = db.update("entries", id, "name", name);
		
		// Update overall list GUI to show updated entry.
		
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryDateTime(Database db, int id, LocalDateTime dateTime) {
		int rv = db.update("entries", id, "dateTime", dateTime.toString());
		
		// Update overall list GUI to show updated entry.
		
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryDescription(Database db, int id, String description) {
		int rv = db.update("entries", id, "description", description);
		
		// Update overall list GUI to show updated entry.
		
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryNotes(Database db, int id, String notes) {
		int rv = db.update("entries", id, "notes", notes);
		
		// Probably don't need to update GUI? We can just refresh the entry when it is clicked on so that it shows the new notes inside.
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

}
