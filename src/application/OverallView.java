package application;

import java.time.LocalDateTime;
import java.util.List;

// List view that shows all existing entries.
public class OverallView implements ListView {
	
	BinarySearchTree entries;
	List<Entry> sortedEntries;

	public OverallView() {
		this.entries = BinarySearchTree.getBSTInstance();
		this.sortedEntries = null;
	}
	
	@Override
	/**
	 * Initializes the list of all entries from database.
	 * return Returns 1 if successfully initialized. Else, return 0 on failure.
	 */
	public int initializeList(Database db) {
		sortedEntries = entries.inorderTraversal();
		return 1;
	}
	
	@Override
	public boolean createEntry(Database db, Entry entry) {
		// Make sure name is not null.
		if (entry.getName() == null || entry.getName().isEmpty()) {
			return false;
		}
		int id = db.insert("entries", entry.getCourse(), entry.getName(), entry.getDateTime(), entry.getDescription());
		Entry newEntry = new Entry(new String[] {Integer.toString(id), entry.getCourse(), entry.getName(), MyDateTime.toString(entry.getDateTime()), entry.getDescription(), null});
		entries.insert(newEntry);
		sortedEntries = entries.inorderTraversal();
		
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
		sortedEntries = entries.inorderTraversal();
		
		// Return value based on successful insertion to database.
		if (id > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean createTask(Database db, String course, String name, LocalDateTime dateTime, String description) {
		int id = db.insert("entries", course, name, dateTime, description);
		Entry newTask = new Entry(new String[] {Integer.toString(id), course, name,  MyDateTime.toString(dateTime), description, null});
		entries.insert(newTask);
		sortedEntries = entries.inorderTraversal();
		
		// Return value based on successful insertion to database.
		if (id > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteEntry(Database db, Entry entry) {
		int rv = db.delete("entries", entry.getId());
		entries.delete(entry);
		sortedEntries = entries.inorderTraversal();
		
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
		int rv = db.update("entries", id, "dateTime", MyDateTime.toString(dateTime));
		
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

	@Override
	public List<Entry> getSortedEntries() {
		return sortedEntries;
	}
}
