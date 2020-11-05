package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// List that shows all existing courses and leads to their corresponding CourseViews. NOT a list view.
public class CourseList {

	private List<CourseView> courses;
	
	public CourseList() {
		courses = new ArrayList<>();
	}
	
	public int initializeCourses() {
		// TODO:
		// Query into course storage (separate from the one containing entries) and get all rows, which contain existing course names and date-times.
		// Add the rows to courses.
		// Display entries as a list using GUI.
		return 0;
	}
	
	public boolean createCourse(String courseName, LocalDateTime meetingTime) {
		CourseView course = new CourseView(courseName, meetingTime);
		return courses.add(course);
	}
	
	public boolean deleteCourse(String courseName) {
		// We can prob use a better data structure but w.e
		if (courses.isEmpty()) {
			return false;
		}
		else {
			for (int i = 0; i < courses.size(); i++) {
				if (courses.get(i).getCourseName().equals(courseName)) {
					courses.remove(i);
					return true;
				}
			}
		}
		return false;
		// TODO: Delete all entries pertaining to the course in storage system.
	}
}
