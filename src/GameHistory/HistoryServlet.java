package GameHistory;

import Accounting.Model.Account;
import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/HistoryServlet")
public class HistoryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg = (String) request.getParameter("msg");
        Account acc = (Account)request.getSession().getAttribute("Account");
        int accID = acc.getID();
        if(msg==null)
            return;
        switch (msg){
            case "first":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    GameHistory history_first = new GameHistory(id,accID);
                    request.getSession().setAttribute("History",history_first);
                    String board_first = history_first.previousMove();
                    JSONObject json_first = GenerateBoardJSON(board_first,"","");
                    response.getWriter().write(json_first.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "prev":
                GameHistory history_prev = (GameHistory) request.getSession().getAttribute("History");
                String board_first = history_prev.previousMove();
                JSONObject json_first = GenerateBoardJSON(board_first,"","WBprev");
                response.getWriter().write(json_first.toString());
                break;
            case "next":
                GameHistory history_next = (GameHistory) request.getSession().getAttribute("History");
                String bestMove = history_next.getBestMove();
                Pair<String,String> pair = history_next.nextMove();
                JSONObject json_next = GenerateBoardJSON(pair.getKey(),bestMove,pair.getValue());
                response.getWriter().write(json_next.toString());
                break;
            case "end_game":
                request.getSession().removeAttribute("History");
                break;
        }
    }

    private JSONObject GenerateBoardJSON(String board,String bestMove,String move){
        JSONObject json = null;
        try {
            json = new JSONObject();
        }catch (JSONException e){
            e.printStackTrace();
        }

        String piece ="";
        if(!move.equals("")){
            piece = move.substring(0,2);
            move = move.substring(2);
        }
        json.put("board",board);
        json.put("best_move",bestMove);
        json.put("move",move);
        json.put("piece",piece);
        return json;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
