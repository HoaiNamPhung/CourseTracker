package backend;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import application.Database;

public class DropDB {
	
	@Test
	void test() {
		Database db = new Database();
		db.open();
		db.drop("entries");
	}
}
