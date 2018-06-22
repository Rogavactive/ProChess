package GameConnection.Model;

import Accounting.Model.Account;
import dbConnection.DataBaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class GameSearchManager {

    private DataBaseManager manager;
    private ReentrantLock lock = new ReentrantLock();

    public GameSearchManager(DataBaseManager manager){
        this.manager = manager;
    }

    public int findOpponent(Account player,String timePrimary, String timeBonus){
        //ranks roca daamateb accountshi iqneba eg rank da martivad amoigeb.
        //gameType:random-0, friendly-1, bot-2   ,   time_primary:1,2,5,10,15     ,    time_bonus:1,2,5,10
        int usernameID = player.getID();
        Connection conn = null;
        lock.lock();
        try {
            conn = manager.getConnection();
            if(usernameInQueue(usernameID,conn)){
                if(!updateUserQueue(usernameID,timePrimary,timeBonus,conn))
                    return -1;
                else{
                    return lookForOpponnent(timePrimary,timeBonus,conn,usernameID);
                }
            }
            int opponentID = lookForOpponnent(timePrimary,timeBonus,conn,usernameID);
            if(opponentID==0&&!addToQueue(usernameID,timePrimary,timeBonus,conn))
                return -1;
            return opponentID;
        }catch (SQLException e) {
            e.printStackTrace();
            return -1;//-1 means something went wrong
        }finally {
            lock.unlock();
            manager.closeConnection(conn);
        }
        //if exists match, return opponent username as string, else return "" and add to queue.
    }

    private int lookForOpponnent(String timePrimary, String timeBonus, Connection conn,int username_ID) {
        String sqlQueryStatement = "select username_ID from search_queue\n" +
                "where timePrimary=" + timePrimary+ " and timeBonus="+timeBonus+" and username_ID!="+username_ID+";";
        ResultSet rslt = manager.executeQuerry(sqlQueryStatement,conn);
        int opponentID = 0;
        try {
            if(rslt!=null&&rslt.next()){
                opponentID = rslt.getInt("username_ID");
                removeFromQueue(opponentID,conn);
            }
            return opponentID;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }finally {
            manager.closeResultset(rslt);
        }
    }

    private boolean addToQueue(int username_ID, String timePrimary, String timeBonus, Connection conn) {
        String sqlQueryStatement = "insert INTO search_queue VALUE\n" +
                "  ("+username_ID+","+timePrimary+","+timeBonus+");";
        return manager.executeUpdate(sqlQueryStatement,conn);
    }

    private boolean updateUserQueue(int username_ID, String timePrimary, String timeBonus, Connection conn) {
        String sqlQueryStatement = "UPDATE search_queue set timePrimary="+timePrimary+", timeBonus="+timeBonus+"\n" +
                "  where username_ID="+username_ID+";";
        return manager.executeUpdate(sqlQueryStatement,conn);
    }

    private boolean usernameInQueue(int username_ID,Connection conn){
        String sqlQueryStatement = "select count(username_ID) as users_count from search_queue\n" +
                "  where username_ID="+username_ID+";";
        ResultSet rslt = manager.executeQuerry(sqlQueryStatement,conn);
        try {
            if(rslt!=null&&rslt.next()){
                return rslt.getInt("users_count")!=0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            manager.closeResultset(rslt);
        }
        return true;
    }

    public boolean removeFromQueue(int username_ID, Connection conn){
        String sqlQueryStatement = "DELETE FROM search_queue WHERE username_ID="+ username_ID +";";
        return manager.executeUpdate(sqlQueryStatement,conn);
    }

    public boolean removeFromQueue(int username_ID) {
        Connection conn = null;
        try {
            conn = manager.getConnection();
            return removeFromQueue(username_ID,conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            manager.closeConnection(conn);
        }

    }
}
