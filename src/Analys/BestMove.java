package Analys;

import java.io.*;
import java.lang.Process;
import java.io.BufferedReader;
import java.util.ArrayList;

public class BestMove {

    static String username = "gjiki";
    static ArrayList < String > moves = new ArrayList < String > ();

    public static void main(String[] args) {

        moves.add("e2e4");
        moves.add("e7e5");
        moves.add("f1c4");
        moves.add("a7a6");
        moves.add("d1f3");
        moves.add("a6a5");

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

        String bestMove = getBestMove();
        deleteFile();

        System.out.println(bestMove);
    }

    private static String getBestMove() {
        String move = "";
        try {
            File file = new File(username + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            move = br.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return move.substring(33, 37);
    }

    private static void deleteFile() {
        try {
            File file = new File(username + ".txt");
            file.delete();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}