package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// List view that shows all existing entries.
public class OverallView implements ListView {
	
	List<ListRow> entries;

	public OverallView(String courseName, LocalDateTime courseMeetingDateTime) {
		this.entries = new ArrayList<>();
	}
	
	@Override
	public int initializeList() {
		// TODO:
		// Query into SQLite database and get ALL rows.
		// Add the rows to entries.
		// Display entries as a list using GUI.
		return 0;
	}
	
	@Override
	public boolean createEntry(Entry entry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createNote(String course, String name, String description, String notes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createTask(String course, String name, LocalDateTime dateTime, String description) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEntry(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyEntryCourse(int id, String course) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyEntryName(int id, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyEntryDateTime(int id, LocalDateTime dateTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyEntryDescription(int id, String description) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyEntryNotes(int id, String notes) {
		// TODO Auto-generated method stub
		return false;
	}

}
