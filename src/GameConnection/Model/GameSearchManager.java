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

    public String findOpponent(Account player,String timePrimary, String timeBonus){
        //ranks roca daamateb accountshi iqneba eg rank da martivad amoigeb.
        //gameType:random-0, friendly-1, bot-2   ,   time_primary:1,2,5,10,15     ,    time_bonus:1,2,5,10
        String username = player.getUsername();
        Connection conn = null;
        lock.lock();
        try {
            conn = manager.getConnection();
            if(usernameInQueue(username,conn)){
                if(!updateUserQueue(username,timePrimary,timeBonus,conn))
                    return null;
            }
            return lookForOpponnent(timePrimary,timeBonus,conn,username);
        }catch (SQLException e) {
            e.printStackTrace();
            return null;//null means something went wrong
        }finally {
            lock.unlock();
            manager.closeConnection(conn);
        }
        //if exists match, return opponent username as string, else return "" and add to queue.
    }

    private String lookForOpponnent(String timePrimary, String timeBonus, Connection conn,String username) {
        String sqlQueryStatement = "select username from search_queue\n" +
                "where timePrimary=" + timePrimary+ " and timeBonus="+timeBonus+" and username!=\""+username+"\";";
        ResultSet rslt = manager.executeQuerry(sqlQueryStatement,conn);
        String opponent = "";
        try {
            if(rslt!=null&&rslt.next()){
                opponent = rslt.getString("username");
                removeFromQueue(opponent,conn);
            }else{
                if(!addToQueue(username,timePrimary,timeBonus,conn))
                    return null;
            }
            return opponent;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            manager.closeResultset(rslt);
        }
    }

    private boolean addToQueue(String username, String timePrimary, String timeBonus, Connection conn) {
        String sqlQueryStatement = "insert INTO search_queue VALUE\n" +
                "  (\""+username+"\","+timePrimary+","+timeBonus+");";
        return manager.executeUpdate(sqlQueryStatement,conn);
    }

    private boolean updateUserQueue(String username, String timePrimary, String timeBonus, Connection conn) {
        String sqlQueryStatement = "UPDATE search_queue set timePrimary="+timePrimary+", timeBonus="+timeBonus+"\n" +
                "  where username=\""+username+"\";";
        return manager.executeUpdate(sqlQueryStatement,conn);
    }

    private boolean usernameInQueue(String username,Connection conn){
        String sqlQueryStatement = "select count(username) as users_count from search_queue\n" +
                "  where username=\""+username+"\";";
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

    public boolean removeFromQueue(String username, Connection conn){
        String sqlQueryStatement = "DELETE FROM search_queue WHERE username=\""+ username +"\";";
        return manager.executeUpdate(sqlQueryStatement,conn);
    }

    public boolean removeFromQueue(String username) {
        Connection conn = null;
        try {
            conn = manager.getConnection();
            return removeFromQueue(username,conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            manager.closeConnection(conn);
        }

    }
}
