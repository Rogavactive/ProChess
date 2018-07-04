package Tests;

import Accounting.Model.Account;
import Accounting.Model.AccountManager;
import dbConnection.DataBaseManager;
import dbConnection.DataBaseTestManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Test")
class AccountTest {

    private static AccountManager accManager;

    @BeforeAll
    static void prepareState(){
        DataBaseTestManager dbManager = DataBaseTestManager.getInstance();
        accManager = new AccountManager(dbManager);
        accManager.register("Dima","dimitrius98@mail.ru","ProChess123");
    }

    @Test
    void simpleTests(){
        Account acc = new Account("Dima","dimitrius98@mail.ru",accManager,69,false);
        assertEquals(acc.getUsername(),"Dima");
        assertEquals(acc.getEmail(),"dimitrius98@mail.ru");
        assertEquals(acc.getID(),69);
        assertEquals(acc.type(),false);
        assertEquals(acc.remove(),true);
    }

}