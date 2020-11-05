package application;

// Subclass of list row that also keeps track of notes, only visible upon clicking an "entry" in a list view.
public class Entry extends ListRow {
	
	private String notes;
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
