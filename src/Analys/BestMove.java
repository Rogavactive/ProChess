package Analys;

import Game.Model.Move;

import java.io.*;
import java.lang.Process;
import java.io.BufferedReader;
import java.util.Vector;

public class BestMove {

    private String username;
    private Vector< String > moves;

    public BestMove(String username, Vector<Move> history) {
        this.username = username;
        moves = new Vector<>();
        for(int i = 0; i < history.size(); i++){
            moves.add(moveToString(history.get(i)));
        }
    }

    public void addMove(Move mv) {
        moves.add(moveToString(mv));
    }

    public void deleteMove() {
        moves.remove(moves.size() - 1);
    }

    private String moveToString(Move mv) {
        String str = "";
        str += (char)(mv.getFrom().getKey() + 'a');
        str += (char)('0' + mv.getFrom().getValue() + 1);
        str += (char)(mv.getTo().getKey() + 'a');
        str += (char)('0' + mv.getTo().getValue() + 1);
        return str;
    }

    private String stringToMove(String str) {
        String mv = "";
        mv += (char)(str.charAt(0) - 'a' + '0');
        mv += (char)(str.charAt(1) - 1);
        mv += (char)(str.charAt(2) - 'a' + '0');
        mv += (char)(str.charAt(3) - 1);
        return mv;
    }

    public String getBestMove() {
        String execString = "python3 Bot/generateMove.py";
        execString += (" " + username);
        for (int i = 0; i < moves.size(); i++) {
            execString += (" " + moves.get(i));
        }

        try {
            Process p = Runtime.getRuntime().exec(execString);
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        String move = "";
        try {
            File file = new File(username + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            move = br.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }

        deleteFile();
        return stringToMove( move.substring(33, 37) );
    }

    private void deleteFile() {
        try {
            File file = new File(username + ".txt");
            file.delete();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}