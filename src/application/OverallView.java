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
	public boolean modifyEntry(Database db, Entry entry, Entry newEntry) {
		int id = entry.getId();
		
		// Update database's entry.
		int rv = db.update("entries", id, "course", newEntry.getCourse());
		rv = db.update("entries", id, "name", newEntry.getName()) ;
		rv = db.update("entries", id, "description", newEntry.getDescription());
		rv = db.update("entries", id, "notes", newEntry.getNotes());
		rv = db.update("entries", id, "dateTime", MyDateTime.toString(newEntry.getDateTime()));
		
		// Update entry values in BST.
		entries.delete(entry);
		newEntry.setId(id);
		entries.insert(newEntry);
		sortedEntries = entries.inorderTraversal();
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryCourse(Database db, Entry entry, String course) {
		int id = entry.getId();
		int rv = db.update("entries", id, "course", course);
		entries.delete(entry);
		entry.setCourse(course);
		entries.insert(entry);
		sortedEntries = entries.inorderTraversal();
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryName(Database db, Entry entry, String name) {
		int id = entry.getId();
		int rv = db.update("entries", id, "name", name);
		entries.delete(entry);
		entry.setName(name);
		entries.insert(entry);
		sortedEntries = entries.inorderTraversal();
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryDateTime(Database db, Entry entry, LocalDateTime dateTime) {
		int id = entry.getId();
		int rv = db.update("entries", id, "dateTime", MyDateTime.toString(dateTime));
		entries.delete(entry);
		entry.setDate(dateTime.toLocalDate());
		entry.setTime(dateTime.toLocalTime());
		entries.insert(entry);
		sortedEntries = entries.inorderTraversal();
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryDescription(Database db, Entry entry, String description) {
		int id = entry.getId();
		int rv = db.update("entries", id, "description", description);
		entries.delete(entry);
		entry.setDescription(description);
		entries.insert(entry);
		sortedEntries = entries.inorderTraversal();
		
		// Return value based on successful deletion from database.
		if (rv == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modifyEntryNotes(Database db, Entry entry, String notes) {
		int id = entry.getId();
		int rv = db.update("entries", id, "notes", notes);
		entries.delete(entry);
		entry.setNotes(notes);
		entries.insert(entry);
		sortedEntries = entries.inorderTraversal();
		
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
