package application;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteDataSource;

// If SQLite not being recognized, go to Project > Properties > Java Build Path > Add JARs > CourseTracker > sqlite-jdbc-3.32.3.2.jar > OK > Apply and Close
public class Database {

	SQLiteDataSource ds = null;
	
	// Immediately initializes the one and only database instance at the start of the JVM.
	private static Database myDatabase = new Database();
	
	/** 
	 * Private default constructor to prevent more than one database instantiation.
	 */
	private Database() {
		this.open();
		this.initializeDatabase();
	}
	
	/**
	 * Retrieves an instance of the singleton, already initialized database.
	 * @return Returns the instance of the database.
	 */
	public static Database getDatabaseInstance() {
		return myDatabase;
	}
	
	/**
	 *  Create a connection to database 'lists.db'. If it doesn't exist, it is created.
	 *  @return Returns false if failed to create to database.
	 */
	private boolean open() {
		try {
			ds = new SQLiteDataSource();
			ds.setUrl("jdbc:sqlite:lists.db");
		}
		// Failed to connect to database.
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Initializes the database's tables.
	 * @return Returns true if both tables were successfully created.
	 */
	private boolean initializeDatabase() {
		int rv1 = createTable("courses");
		int rv2 = createTable("entries");
		if ((rv1 & rv2) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Create a table in the database (either 'courses' or 'entries').
	 * @param tablename The table to create.
	 * @return Returns -1 if exception occurs or invalid table. Otherwise, return 0 if table is created or already exists.
	 */
	public int createTable(String tablename) {
		int result = 0;
		Connection conn = null;
		String query = "";
		if (tablename.equals("courses")) {
			query = "CREATE TABLE IF NOT EXISTS courses ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "coursename TEXT NOT NULL,"
					+ "meetingdatetime TEXT,"
					+ "coursedescription TEXT)";
		}
		else if (tablename.equals("entries")) {
			query = "CREATE TABLE IF NOT EXISTS entries ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "course TEXT,"
					+ "name TEXT NOT NULL,"
					+ "datetime TEXT,"
					+ "description TEXT,"
					+ "notes TEXT)";
		}
		else {
			return -1;
		}

		try {
			conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			result = stmt.executeUpdate(query);
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		
		// Close connection to database.
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Insertion into table 'entries'.
	 * @param tablename The table being inserted into.
	 * @param course The course of the entry.
	 * @param name The name of the entry.
	 * @param datetime The date and time of the entry.
	 * @param description The description of the entry.
	 * @return Returns -1 if exception occurs, wrong parameters for given tablename, or invalid table. Otherwise, returns id of inserted row.
	 */
	public int insert(String tablename, String course, String name, LocalDateTime datetime, String description) {
		int result = -1;
		Connection conn = null;
		if (tablename.equals("entries")) {
			String query = "INSERT INTO entries(course, name, datetime, description)"
					+ " VALUES(?,?,?,?)"; 
			try {
				conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, course);
				stmt.setString(2, name);
				stmt.setString(3, MyDateTime.toString(datetime));
				stmt.setString(4, description);
				result = stmt.executeUpdate();
				
				// Get the new id of the row for returning. If no rows were added, return result as-is.
				if (result >= 1) {
					ResultSet id = stmt.getGeneratedKeys();
					if (id.next()) {
						result = (int) (id.getLong(1));
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				result = -1;
			}
		}
		
		// Close connection to database.
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Insertion into table 'courses'.
	 * @param tablename The table being inserted into.
	 * @param coursename The name of the new course.
	 * @param meetingdatetime The meeting date and time of the new course.
	 * @return Returns -1 if exception occurs, wrong parameters for given tablename, or invalid table. Otherwise, returns id of inserted row.
	 */
	public int insert(String tablename, String coursename, LocalDateTime meetingdatetime, String coursedescription) {
		int result = -1;
		Connection conn = null;
		if (tablename.equals("courses")) {
			String query = "INSERT INTO courses (coursename, meetingdatetime, coursedescription)"
					+ " VALUES (?,?,?)"; 
			try {
				conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, coursename);
				stmt.setString(2, MyDateTime.toString(meetingdatetime));
				stmt.setString(3, coursedescription);
				result = stmt.executeUpdate();
				
				// Get the new id of the row for returning. If no rows were added, return result as-is.
				if (result >= 1) {
					ResultSet id = stmt.getGeneratedKeys();
					if (id.next()) {
						result = (int) (id.getLong(1));
					}
				}
			}
			catch (Exception e) {
				// e.printStackTrace();
				result = -1;
			}
		}
		
		// Close connection to database.
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Update a single value in a table.
	 * @param tablename The table being updated.
	 * @param id The id of the row being updated.
	 * @param columnName The column being updated.
	 * @param newValue The new value of the column at given row.
	 * @return Returns -1 if exception occurs or invalid table. Otherwise, returns number of rows updated (1).
	 */
	public int update(String tablename, int id, String columnName, String newValue) {
		int result = 0;
		Connection conn = null;
		String query = "UPDATE " + tablename + " SET " + columnName + "=?"
				+ "WHERE id=?";
		try {
			conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, newValue);
			stmt.setInt(2, id);
			result = stmt.executeUpdate();
		}
		catch (Exception e) {
			// e.printStackTrace();
			result = -1;
		}
		
		// Close connection to database.
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Delete a row from a table.
	 * @param tablename The table being deleted from.
	 * @param id The id of the row being removed.
	 * @return Returns -1 if exception occurs or invalid table. Otherwise, returns number of rows deleted (1).
	 */
	public int delete(String tablename, int id) {
		int result = 0;
		Connection conn = null;
		String query = "DELETE FROM " + tablename + " WHERE id=?";
		try {
			conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			result = stmt.executeUpdate();
		}
		catch (Exception e) {
			// e.printStackTrace();
			result = -1;
		}
		
		// Close connection to database.
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Query a row from a table.
	 * @param tablename The table being queried from.
	 * @param id The id of the row to query for.
	 * @return Returns the row as an array.
	 */
	public String[] query(String tablename, int id) {
		String[] result = null;
		Connection conn = null;
		String query = "SELECT * FROM " + tablename + " WHERE id=?";
		try {
			conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			result = parseResultSet(tablename, rs).get(0);
		}
		catch (Exception e) {
			// e.printStackTrace();
			result = null;
		}
		
		// Close connection to database.
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Query for rows from the table belonging to a course. If null is used instead of a course, retrieve all rows.
	 * @param tablename The table being queried from.
	 * @param course The course of the rows to query for. If null, query for all rows regardless of course.
	 * @return Returns a list of rows as arrays. Returns an empty list if failed.
	 */
	public List<String[]> queryAll(String tablename, String course) {
		List<String[]> result = new ArrayList<>();
		Connection conn = null;
		String query = null;
		boolean allFlag = false;
		
		// With a course specifier, this only works with the entries database.
		if (course != null) {
			if (tablename.equals("entries")) {
				query = "SELECT * FROM " + tablename + " WHERE course=?";
			}
			else {
				return result;
			}
		}
		// Works for both the courses and entries database if null is used in place of course specifier.
		else {
			allFlag = true;
			query = "SELECT * FROM " + tablename;
		}
		
		
		try {
			conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			if (allFlag != true) {
				stmt.setString(1, course);
			}
			ResultSet rs = stmt.executeQuery();
			result = parseResultSet(tablename, rs);
		}
		catch (Exception e) {
			// e.printStackTrace();
			// No rows in database corresponding to query; return empty list.
		}
		
		// Close connection to database.
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Return the list of rows or an empty list.
		return result;
	}
	
	/**
	 * Drop a table.
	 * @param tablename The table to drop.
	 * @return Returns -1 if exception occurs. Otherwise, returns 0, regardless of whether or not a table was successfully dropped.
	 */
	public int drop(String tablename) {
		int result = 0;
		Connection conn = null;
		String query = "DROP TABLE " + tablename;
		try {
			conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			result = stmt.executeUpdate(query);
		}
		catch (Exception e) {
			// e.printStackTrace();
			result = -1;
		}
		
		// Close connection to database.
		if (conn != null) {
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Recreate the table.
		createTable(tablename);
		return result;
	}
	
	/**
	 * Parse the ResultSet and convert it to a list of rows as arrays.
	 * @param tablename The table the ResultSet was obtained (queried) from.
	 * @param rs The ResultSet.
	 * @return Returns the ResultSet as a list of rows as arrays.
	 */
	public List<String[]> parseResultSet(String tablename, ResultSet rs) {
		List<String[]> rows = new ArrayList<>();
		if (tablename.equals("courses")) {
			try {
				while (rs.next()) {
			        int id = rs.getInt(1);
			        String coursename = rs.getString(2);
			        String meetingdatetime = rs.getString(3);	    
			        String coursedescription = rs.getString(4);	  
			        String row[] = {Integer.toString(id), coursename, meetingdatetime, coursedescription};
			        rows.add(row);
				}
				return rows;
			}
			catch (Exception e) {
				// e.printStackTrace();
			}
		}
		else if (tablename.equals("entries")) {
			try {
				while (rs.next()) {
			        int id = rs.getInt(1);
			        String course = rs.getString(2);
			        String name = rs.getString(3);
			        String datetime = rs.getString(4);
			        String description = rs.getString(5);
			        String notes = rs.getString(6);
			        String row[] = {Integer.toString(id), course, name, datetime, description, notes};
			        rows.add(row);
				}
				return rows;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
