# CourseTracker

There is currently no executable. To test the current state of the project, manually run src/application/Main.java.

Implemented features: 
* Three main views:
  * General View: all entries are shown.
  * Courses View: all courses are shown.
  * Course View: all entries from a selected course are shown.
* Add dialog (for entries and courses)
* Delete (on selected entry or course)
* Entry details + notes (editable)

If SQLite database isn't working, try to set up the SQLite JBDC driver dependencies as follows:
```
Project > Properties > Java Build Path > Add JARs > CourseTracker > sqlite-jdbc-3.32.3.2.jar > OK > Apply and Close 
```