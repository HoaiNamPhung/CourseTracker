# CourseTracker

There is currently no executable. To test the current state of the project, manually run src/application/Main.java.

Currently, only the Add functionality works.

Also, I'm not fully sure if the dependency on the SQLite JDBC driver fully works on machines besides my own, as I initially had to manually set the build path to include it.  None of my teammates have been online/available to test the project, either. If the problem is indeed the build path, do as follows: 
```
Project > Properties > Java Build Path > Add JARs > CourseTracker > sqlite-jdbc-3.32.3.2.jar > OK > Apply and Close 
```

If there are still problems, feel free to tell me.