package backend;

import org.junit.jupiter.api.Test;

import application.Database;

public class DropDB {
	
	@Test
	void test() {
		Database db = Database.getDatabaseInstance();
		db.drop("entries");
		db.drop("courses");
	}
}
