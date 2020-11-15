package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// Contains all instance field members visible to user when they're on a list view.
public class ListRow {

	private Integer id;
	private String course;
	private String name;
	private LocalDateTime dateTime;
	private String description;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCourse() {
		return course;
	}
	
	public void setCourse(String course) {
		this.course = course;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		if (dateTime == null) {
			return null;
		}
		return dateTime.toLocalDate();
	}

	public void setDate(LocalDate date) {
		this.dateTime = dateTime.toLocalTime().atDate(date);
	}
	
	public LocalTime getTime() {
		if (dateTime == null) {
			return null;
		}
		return dateTime.toLocalTime();
	}

	public void setTime(LocalTime time) {
		this.dateTime = dateTime.toLocalDate().atTime(time);
	}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}