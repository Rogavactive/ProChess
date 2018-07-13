package Analys;

import Game.Model.Cell;
import Game.Model.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import java.lang.Process;

public class GenerateMove {

    private int accID;
    private ArrayList< String > moves;

    public GenerateMove(int accID, ArrayList < String > moves) {
        this.accID = accID;
        this.moves = moves;
    }

    public String getBestMove() {

        String execString = "python3 /home/paranoid/Documents/ProChess/Bot/generateMove.py";
        execString += (" " + String.valueOf(accID));

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

        String move = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        try {
            File file = new File("/home/paranoid/Documents/ProChess/Bot" + String.valueOf(accID) + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            move = br.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }

        deleteFile();
        return move.substring(33, 37);
    }

    private void deleteFile() {
        try {
            File file = new File("/home/paranoid/Documents/ProChess/Bot" + String.valueOf(accID) + ".txt");
            file.delete();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}