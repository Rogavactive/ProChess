package Puzzles.Controller;

import Puzzles.Model.Puzzle;
import Puzzles.Model.databaseConnection;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/PuzzleServlet")
public class PuzzleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = (String) request.getParameter("type");
        System.out.println(type);
        if(type==null){
            return;
        }
        switch (type){
            case "comp_move":
                Puzzle puzz = (Puzzle) request.getSession().getAttribute("Puzzle");
                if(puzz==null)
                    return;
                String boardStat = puzz.doComputerMove();
                if(boardStat.equals("success")){
                    JSONObject json = null;
                    try {
                        json = new JSONObject();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    json.put("type","winner_state");
                    json.put("winner",boardStat);
                    request.getSession().removeAttribute("Puzzle");
                    response.getWriter().write(json.toString());
                }else{
                    response.getWriter().write(GenerateBoardJSON(boardStat,puzz.getPossibleMoves()).toString());
                }
                break;
            case "make_move":
                String move = (String) request.getParameter("move");
                if(move==null)
                    return;
                System.out.println("1");
                Puzzle puzzz = (Puzzle) request.getSession().getAttribute("Puzzle");
                System.out.println("2");
                int srcRow = Character.getNumericValue(move.charAt(0));
                int srcCol = Character.getNumericValue(move.charAt(1));
                int dstRow = Character.getNumericValue(move.charAt(2));
                int dstCol = Character.getNumericValue(move.charAt(3));
                if(!puzzz.pieceMoved(srcRow,srcCol,dstRow,dstCol)){
                    JSONObject json = null;
                    try {
                        json = new JSONObject();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    json.put("type","winner_state");
                    json.put("winner","wrong move");
                    request.getSession().removeAttribute("Puzzle");
                    response.getWriter().write(json.toString());
                }else{
                    String board = puzzz.getBoardState();
                    response.getWriter().write(GenerateBoardJSON(board,"").toString());
                }
                System.out.println("make_move");
                break;
            case "initial_state":
                databaseConnection con = new databaseConnection();
                try {
                    int cou = con.puzzlesCount();
                    int puzzle_id = Integer.parseInt(request.getParameter("puzzleID"));
                    Puzzle puzzle = con.getPuzzle(puzzle_id);
                    if(puzzle==null)
                        return;
                    request.getSession().setAttribute("Puzzle",puzzle);
                    String boardState = puzzle.getBoardState();
                    String possibleMoves = puzzle.getPossibleMoves();
                    JSONObject json = GenerateBoardJSON(boardState,possibleMoves);
                    response.getWriter().write(json.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "stop_puzzle":
                request.getSession().removeAttribute("Puzzle");
                break;
        }
    }


    private JSONObject GenerateBoardJSON(String boardState, String currentMovesPossible){
        JSONObject json = null;
        try {
            json = new JSONObject();
        }catch (JSONException e){
            e.printStackTrace();
        }
        json.put("type","board_state");
        json.put("board",boardState);
        json.put("player",'W');
        json.put("moves",currentMovesPossible);
        return json;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
