package application;

import java.time.LocalDateTime;

// Subclass of list row that also keeps track of notes, only visible upon clicking an "entry" in a list view.
public class Entry extends ListRow {
	private String notes;
	
	// Default constructor.
	public Entry() {
	}
	
	// Constructor given an array return value that was queried from our SQLite database.
	public Entry(String[] colArr) {
		setId(Integer.parseInt(colArr[0]));
		setCourse(colArr[1]);
		setName(colArr[2]);
		setDateTime(LocalDateTime.parse(colArr[3]));
		setDescription(colArr[4]);
		setNotes(colArr[5]);
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String toString() {
		String str = "[" + getId() + ", " + getCourse() + ", " + getName() + ", " + getDateTime() +
				", " + getDescription() + ", " + getNotes() + "]";
		return str;
	}
}
