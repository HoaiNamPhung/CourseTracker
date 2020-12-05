package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Static handler for DateTime in case the value is null. */
public class MyDateTime {
	
	public static final LocalDate MAX_DATE = LocalDate.parse("3000-01-01");
	public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
	
	/** 
	 * Gets the DateTime as a string.
	 * @param dateTime The datetime.
	 * @return Returns the DateTime as a string. If null, return null.
	 */
	public static String toString(LocalDateTime dateTime) {
		if (dateTime != null) {
			return dateTime.toString();
		}
		return null;
	}
	
	/** 
	 * Gets the DateTime as a string from a given LocalDate and LocalTime.
	 * @param date The date.
	 * @param time The time.
	 * @return Returns the DateTime as a string. If null, return null.
	 */
	public static String toString(LocalDate date, LocalTime time) {
		if (date != null) {
			if (time != null) {
				return date.atTime(time).toString();
			}
			return date.atStartOfDay().toString();
		}
		else if (time != null) {
			return time.atDate(MAX_DATE).toString();
		}
		return null;
	}
	
	/**
	 * Compares two LocalDateTimes to see which is chronologically greater. Used specfically for BST.
	 * @param a The LocalDateTime we wish to compare.
	 * @param b The LocalDateTime being compared to.
	 * @return Returns -1 if less than, 0 if equal, 1 if greater than.
	 */
	public static int compareDateTime(LocalDateTime a, LocalDateTime b) {
		
		// If null, assume greatest value.
		if (a == null) {
			return 1;
		}
		else if (b == null) {
			return -1;
		}
		
		// Normal comparison in case neither are null.
		return a.compareTo(b);	
	}
	
	/**
	 * Compares two LocalDates to see which is chronologically greater.
	 * @param a The LocalDate we wish to compare.
	 * @param b The LocalDate being compared to.
	 * @return Returns -1 if less than, 0 if equal, 1 if greater than.
	 */
	public static int compareDate(LocalDate a, LocalDate b) {
		
		if (a == null && b == null) {
			return 0;
		}
		// If null, assume greatest value.
		else if (a == null) {
			return 1;
		}
		else if (b == null) {
			return -1;
		}
		
		// Normal comparison in case neither are null.
		return a.compareTo(b);	
	}
	
	/**
	 * Compares two LocalTimes to see which is chronologically greater.
	 * @param a The LocalTime we wish to compare.
	 * @param b The LocalTime being compared to.
	 * @return Returns -1 if less than, 0 if equal, 1 if greater than.
	 */
	public static int compareTime(LocalTime a, LocalTime b) {
		
		if (a == null && b == null) {
			return 0;
		}
		// If null, assume greatest value.
		else if (a == null) {
			return 1;
		}
		else if (b == null) {
			return -1;
		}
		
		// Normal comparison in case neither are null.
		return a.compareTo(b);	
	}
}
