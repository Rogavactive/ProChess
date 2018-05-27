import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Account.AccountManager;

class AccountManagerTest {

	@Test
	void test1() {
		AccountManager manager = new AccountManager();
		
		assertFalse(manager.accountExists("Dima", "Dima"));
		assertFalse(manager.accountExists("Jora", "Jora"));
		manager.register("Dima", "Dima", "Dima");
		assertTrue(manager.accountExists("Dima", "Dima"));
		assertFalse(manager.accountExists("Jora", "Jora"));
		manager.register("Jora", "Jora", "Jora");
		assertTrue(manager.accountExists("Dima", "Dima"));
		assertTrue(manager.accountExists("Jora", "Jora"));
	}
	
	@Test
	void test2() {
		AccountManager manager = new AccountManager();
		
		assertFalse(manager.existsEmail("droga16"));
		assertFalse(manager.existsEmail("jimn14"));
		assertFalse(manager.existsUsername("Dito"));
		assertFalse(manager.existsUsername("Jimni"));
		manager.register("Jimni", "jimn14", "racxa");
		assertFalse(manager.existsEmail("droga16"));
		assertTrue(manager.existsEmail("jimn14"));
		assertFalse(manager.existsUsername("Dito"));
		assertTrue(manager.existsUsername("Jimni"));
		manager.register("Dito", "droga16", "racxa");
		assertTrue(manager.existsEmail("droga16"));
		assertTrue(manager.existsEmail("jimn14"));
		assertTrue(manager.existsUsername("Dito"));
		assertTrue(manager.existsUsername("Jimni"));
		
	}
	
	@Test
	void test3() {
		AccountManager manager = new AccountManager();
		
		assertFalse(manager.existsEmail("droga16@freeuni.edu.ge"));
		assertFalse(manager.existsEmail("jimn14@"));
		assertFalse(manager.existsUsername("Demeter"));
		assertFalse(manager.existsUsername("Jumber"));
		assertFalse(manager.accountExists("Demeter", "racxa"));
		assertFalse(manager.accountExists("Jumber", "racxa"));
		manager.register("Demeter", "droga16@freeuni.edu.ge", "racxa");
		assertTrue(manager.existsEmail("droga16@freeuni.edu.ge"));
		assertFalse(manager.existsEmail("jimn14@"));
		assertTrue(manager.existsUsername("Demeter"));
		assertFalse(manager.existsUsername("Jumber"));
		assertTrue(manager.accountExists("Demeter", "racxa"));
		assertFalse(manager.accountExists("Jumber", "racxa"));
		manager.register("Jumber", "jimn14@", "racxa");
		assertTrue(manager.existsEmail("jimn14@"));
		assertTrue(manager.existsUsername("Demeter"));
		assertTrue(manager.existsUsername("Jumber"));
		assertTrue(manager.accountExists("Demeter", "racxa"));
		assertTrue(manager.accountExists("Jumber", "racxa"));
		manager.dispose();
		
		//WARNING!!!!: reset database after testing. just run initialBase.sql script
		
	}

}
