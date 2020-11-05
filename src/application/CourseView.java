package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// List view that only shows the entries of a specific course.
public class CourseView implements ListView {
	
	private String courseName;
	private LocalDateTime courseMeetingDateTime;
	private List<ListRow> entries;
	
	public CourseView(String courseName, LocalDateTime courseMeetingDateTime) {
		this.courseName = courseName;
		this.courseMeetingDateTime = courseMeetingDateTime;
		this.entries = new ArrayList<>();
	}
	
	/** Course specific methods */
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public LocalDateTime getCourseMeetingDateTime() {
		return courseMeetingDateTime;
	}
	
	public void setMeetingDateTime(LocalDateTime courseMeetingDateTime) {
		this.courseMeetingDateTime = courseMeetingDateTime;
	}

	/** General list view methods for manipulating entries in our list */
	
	@Override
	public int initializeList() {
		// TODO:
		// Query into storage using courseName column and get all corresponding rows
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
