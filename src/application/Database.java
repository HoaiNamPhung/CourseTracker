package application;

import java.sql.*;
import java.time.LocalDateTime;
import org.sqlite.SQLiteDataSource;

// If SQLite not being recognized, go to Project > Properties > Java Build Path > Add JARs > CourseTracker > sqlite-jdbc-3.32.3.2.jar > OK > Apply and Close
// TODO: Better exception handling.
public class Database {

	SQLiteDataSource ds = null;
	
	/**
	 * Constructor
	 */
	public Database() {
	}
	
	/**
	 *  Create a connection to database 'lists.db'. If it doesn't exist, it is created.
	 *  @return Returns false if failed to create to database.
	 */
	public boolean open() {
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
					+ "meetingdatetime TEXT)";
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
	 * @return Returns -1 if exception occurs, wrong parameters for given tablename, or invalid table. Otherwise, returns number of rows inserted (1).
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
				stmt.setString(3, datetime.toString());
				stmt.setString(4, description);
				result = stmt.executeUpdate();
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
	 * @return Returns -1 if exception occurs, wrong parameters for given tablename, or invalid table. Otherwise, returns number of rows inserted (1).
	 */
	public int insert(String tablename, String coursename, LocalDateTime meetingdatetime) {
		int result = -1;
		Connection conn = null;
		if (tablename.equals("courses")) {
			String query = "INSERT INTO courses (coursename, meetingdatetime)"
					+ " VALUES (?,?)"; 
			try {
				conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, coursename);
				stmt.setString(2, meetingdatetime.toString());
				result = stmt.executeUpdate();
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
			result = parseResultSet(tablename, rs);
		}
		catch (Exception e) {
			e.printStackTrace();
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
	 * Parse the ResultSet and convert it to an array.
	 * @param tablename The table the ResultSet was obtained (queried) from.
	 * @param rs The ResultSet.
	 * @return Returns the ResultSet as an array.
	 */
	public String[] parseResultSet(String tablename, ResultSet rs) {
		if (tablename.equals("courses")) {
			try {
				while (rs.next()) {
			        int id = rs.getInt(1);
			        String coursename = rs.getString(2);
			        String meetingdatetime = rs.getString(3);	    
			        String row[] = {Integer.toString(id), coursename, meetingdatetime};
			        return row;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
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
			        return row;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
