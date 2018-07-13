package GameConnection.Model;

import Accounting.Model.Account;
import dbConnection.DataBaseMainManager;
import dbConnection.DataBaseManager;
import dbConnection.DataBaseTestManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class GameSearchManager {

    private DataBaseManager manager;
    private ReentrantLock lock = new ReentrantLock();
    private static GameSearchManager instance = new GameSearchManager(DataBaseMainManager.getInstance());

    private GameSearchManager(DataBaseMainManager manager){
        this.manager = manager;
    }

    public static GameSearchManager getInstance() {
        return instance;
    }

    //    public GameSearchManager(DataBaseTestManager manager){
//        this.manager = manager;
//    }

    public int findOpponent(Account player,String timePrimary, String timeBonus){
        //ranks roca daamateb accountshi iqneba eg rank da martivad amoigeb.
        //gameType:random-0, friendly-1, bot-2   ,   time_primary:1,2,5,10,15     ,    time_bonus:1,2,5,10
        int usernameID = player.getID();
        int rating = 0;
        if(timePrimary.equals("1")||timePrimary.equals("2")){
            rating = player.getBulletRaiting();
        }else if(timePrimary.equals("5")){
            rating = player.getBlitzRaiting();
        }else{
            rating = player.getClassicalRaiting();
        }
        Connection conn = null;
        lock.lock();
        try {
            conn = manager.getConnection();
            if(usernameInQueue(usernameID,conn)){
                if(!updateUserQueue(usernameID,timePrimary,timeBonus,conn))
                    return -1;
                else{
                    return lookForOpponnent(timePrimary,timeBonus,conn,usernameID,rating);
                }
            }
            int opponentID = lookForOpponnent(timePrimary,timeBonus,conn,usernameID,rating);
            if(opponentID==0&&!addToQueue(usernameID,timePrimary,timeBonus,conn,rating))
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

    private int lookForOpponnent(String timePrimary, String timeBonus, Connection conn,int username_ID,int rating) {
        String sqlQueryStatement = "select username_ID from search_queue\n" +
                "where timePrimary=" + timePrimary+ " and timeBonus="+timeBonus+" and username_ID!="+username_ID+" " +
                "and rating between "+(rating-500)+" and "+(rating+500)+";";
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

    private boolean addToQueue(int username_ID, String timePrimary, String timeBonus, Connection conn,int rating) {
        String sqlQueryStatement = "insert INTO search_queue VALUE\n" +
                "  ("+username_ID+","+timePrimary+","+timeBonus+","+rating+");";
        System.out.println(sqlQueryStatement);
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

    private boolean removeFromQueue(int username_ID, Connection conn){
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
