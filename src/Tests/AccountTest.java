package Tests;

import Accounting.Model.Account;
import Accounting.Model.AccountManager;
import dbConnection.DataBaseTestManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Test")
class AccountTest {

    private static AccountManager accManager;
    private static Account native_acc;//current native
    private static Account google_acc;//current google
    private static Account google_acc_2;

    @BeforeAll
    static void Init(){
        DataBaseTestManager dbManager = DataBaseTestManager.getInstance();
        accManager = new AccountManager(dbManager);
        native_acc = accManager.register("Dima","dimitrius98@mail.ru","ProChess123");
        google_acc = accManager.register("Dato","dbezh16@freeuni.edu.ge",null);
        google_acc_2 = accManager.register("Naruto","Uzumaki",null);
    }

    @AfterAll
    static void reset(){
        assertTrue(native_acc.remove());
        assertTrue(google_acc.remove());
        assertTrue(google_acc_2.remove());
    }

    @Test
    synchronized void acceptTests(){
        assertEquals(native_acc.getUsername(),"Dima");
        assertEquals(native_acc.getEmail(),"dimitrius98@mail.ru");
        assertTrue(native_acc.type());
    }

    @Test
    synchronized void failTests(){
        assertNotEquals(native_acc.getUsername(),"Dimitri");
        assertNotEquals(native_acc.getEmail(),"dimitrius99@mail.ru");
        assertNotEquals(native_acc.getID(),0);
        assertTrue(native_acc.type());
    }
    @Test
    synchronized void accChangeTests(){
        assertFalse(native_acc.changePassword("ProChess111","ProChess321"));
        assertTrue(native_acc.changePassword("ProChess123","ProChess321"));
        assertFalse(native_acc.changePassword("ProChess111","ProChess123"));
        assertTrue(native_acc.changePassword("ProChess321","ProChess123"));
        assertFalse(native_acc.changePassword("ProChess321",null));
        assertFalse(native_acc.change(null,null));
        assertTrue(native_acc.change("Dimitri","droga16@freeuni.edu.ge"));
        assertEquals(native_acc.getUsername(),"Dimitri");
        assertEquals(native_acc.getEmail(),"droga16@freeuni.edu.ge");
        assertTrue(native_acc.change("Dima","dimitrius98@mail.ru"));
    }

    @Test
    synchronized void googleAccTests(){
        assertFalse(google_acc.type());
        assertEquals(google_acc.getUsername(),"Dato");
        assertEquals(google_acc.getEmail(),"dbezh16@freeuni.edu.ge");
        assertNotEquals(google_acc.getID(),0);
        assertFalse(google_acc.changePassword("ProChess111","ProChess321"));
        assertFalse(google_acc.changePassword("ProChess123","ProChess321"));
        assertTrue(google_acc.changePassword(null,"ProChess321"));
        assertTrue(google_acc.type());
        assertFalse(native_acc.change("Naruto","dimitrius98@mail.ru"));
        assertTrue(native_acc.change("Davit","datobejanishvili"));
        assertEquals(native_acc.getUsername(),"Davit");
        assertEquals(native_acc.getEmail(),"datobejanishvili");
    }

}