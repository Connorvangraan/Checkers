package main.java.backend;

import javax.swing.text.html.MinimalHTMLWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

/**
 * TODO:
 */

public class Game {
    boolean verbose = false;
    Board b = new Board(verbose);
    String playerName;
    int human, cpu, player;
    static int white = 1, black =2;
    boolean getRandom = true;
    int difficulty = 0;

    static int capture = 2;

    public Game(String playerName) throws InterruptedException {
        //System.out.println("Welcome "+playerName);
        this.playerName = playerName;
        //run();
    }

    public boolean checkLegalMove(int[][] move) {
        return b.validMove(move[0], move[1]);
    }

    public boolean checkLegalCapture(int[][] move) {
        return b.validCapture(move[0], move[1]);
    }

    public boolean makeMove(int[][] move) {
        b.makeMove(move[0], move[1], human);
        b.showBoard();
        return true;
    }

    public int[] makeCapture(int[][] move) {
        int[] c = b.capture(move[0],move[1]);
        b.showBoard();
        return c;
    }

    public ArrayList<int[][]> getValidMoves(boolean show) {
        ArrayList<int[][]> m = b.findMoves();
        if (show) {
            System.out.println("Move list:");
        }

        String moveList = "";
        for (int row = 0; row < m.size(); row++) {
            moveList = moveList.concat("" + m.get(row)[0][0] + m.get(row)[0][1] + " " + m.get(row)[1][0] + m.get(row)[1][1] + "\n");
        }
        if (show) {
            System.out.println(moveList);
        }
        return m;
    }

    public void setCurrentPlayer(int player){
        b.setCurrentPlayer(player);
    }

    public void setPlayer(int colour){
        if (colour == 1){
            b.setColour(1);
        }
        else{
            b.setColour(2);
        }
    }

    public int getPlayer(){
        return b.getCurrentPlayer();
    }

    public int[][] getcpuMove(){
        Random r = new Random();
        ArrayList<int[][]> moves = getValidMoves(false);
        if (difficulty == 0){
            int n = r.nextInt(moves.size());
            return moves.get(n);
        }
        else{
            MiniMax mm = new MiniMax(b.getBoard(), b.getCpuColour(), difficulty);
            int[][] move = mm.minimaxmove();
            if (move == null){
                return getValidMoves(false).get(r.nextInt(moves.size()));
            }
            else {
                return move;
            }

        }
    }

    public int[] convertCoord(int[] x){
        int[] y = new int[2];
        if (x[0]% 2 == 0){
            y[1] = (x[1])*2+1;
        }
        else{
            y[1] = (x[1])*2;
        }
        y[0] = x[0];
        return y;
    }

    public boolean checkVictory() {
        int temp = b.getCurrentPlayer();
        if (b.blackVictory()) {
            System.out.println("Black has won!");
            return true;
        }
        if (b.whiteVictory()) {
            System.out.println("White has won!");
            return true;
        }
        if (getValidMoves(false).size() <= 0) {
            System.out.println("all out of moves");
            return true;
        }
        b.setCurrentPlayer(temp);
        return false;
    }

    public int getVictor(){
        int temp = b.getCurrentPlayer();
        if (b.blackVictory()) {
            return 2;
        }
        if (b.whiteVictory()) {
            return 1;
        }
        else {
            b.setCurrentPlayer(temp);
            return 0;
        }
    }

    public boolean possibleCaptures(){
        getValidMoves(false);
        return b.possibleCaptures();
    }

    public String getError(int[][] move, int colour){
        if (move[0][0] > move[1][0]+1 || move[0][0] < move[1][0]-1){
            return "Too far";
        }
        if (b.getBoard()[move[1][0]][move[1][1]] != 8){
            return "Target not empty";
        }
        System.out.println(colour);
        System.out.println(""+move[1][0]+move[0][0]);
        System.out.println(""+move[1][1]+move[0][1]);
        if ((colour == white && move[1][0] < move[0][0]) || (colour == black && move[1][0] > move[0][0])){
            return "Can't travel in that direction";
        }
        if (move[0][0]==move[1][0]){
            return "Can't move to the same row";
        }
        return "Unable to make that move";

    }


    public void run() throws InterruptedException {
        boolean running = true;
        Scanner colour = new Scanner(System.in);
        System.out.println("White: 1");
        System.out.println("Black: 2");
        int c = colour.nextInt();
        b.setColour(c);

        if (c % 2 == 0) {
            human = c;
            cpu = 1;
            b.setCurrentPlayer(human);
        } else {
            human = 1;
            cpu = 2;
            b.setCurrentPlayer(cpu);
        }

        //System.out.println(checkVictory());
        b.showBoard();

        while (running) {
            //System.out.println("Running");
            // black goes first. This is if the player is black
            if (b.getHumanColour() == b.black) {
                System.out.println("Player move");
                TimeUnit.SECONDS.sleep(1);
                userMove();
                changePlayer();
                TimeUnit.SECONDS.sleep(1);
                running = !checkVictory();

                if (running == true) {
                    System.out.println("CPU Move");
                    cpumove();
                    running = !checkVictory();
                }
            }
            // if the player is white
            else {
                System.out.println("CPU Move");
                cpumove();
                changePlayer();
                TimeUnit.SECONDS.sleep(1);
                running = !checkVictory();

                if (running == true) {
                    TimeUnit.SECONDS.sleep(1);
                    userMove();
                    running = !checkVictory();
                }
            }
            changePlayer();
        }
        System.out.println("Game over");
        gameOver();
    }

    public void setDifficulty(int x) {
        if (x == 0) {
            getRandom = true;
        }
        else{
            getRandom = false;
            difficulty=x;
        }
    }

    public void userMove() {
        getValidMoves(true);
        int[][] move = getUserMove();
        int type = b.makeMove(move[0], move[1], human);
        getValidMoves(false);
        while (type == capture && b.captureOption && !checkVictory()) {
            System.out.println("Another capture available");
            getValidMoves(true);
            move = getUserMove();
            b.makeMove(move[0], move[1], human);
            getValidMoves(false);
        }
        b.showBoard();
    }

    public void cpumove() {
        ArrayList<int[][]> moves = getValidMoves(false);
        int len = moves.size();
        if (len > 0) {
            Random r = new Random();
            int choice = r.nextInt(len);
            if (choice % 2 == 1) {
                choice -= 1;
            }
            int[][] move;
            if (getRandom) {
                move = moves.get(choice);
            } else {
                // annoyingly required to make a deep copy of int[][]
                int[][] tempboard = b.getBoard().clone();
                int[][] tempboard2 = new int[8][4];
                for (int row = 0; row < tempboard.length; row++) {
                    for (int col = 0; col < tempboard[row].length; col++) {
                        tempboard2[row][col] = tempboard[row][col];//Integer.valueOf(String.valueOf(tempboard[row][col]));
                    }
                }

                MiniMax m = new MiniMax(tempboard2, difficulty, cpu);///, difficulty
                move = m.minimaxmove();
            }
            System.out.println("x: " + (move[0][0] + move[0][1]));
            System.out.println("y: " + (move[1][0] + move[1][1]));
            int type = b.makeMove(move[0], move[1], cpu);
            System.out.println("CPU did: " + move[0][0] + move[0][1] + " to " + move[1][0] + move[1][1]);
            moves = getValidMoves(false);

            while (type == capture && b.captureOption && !checkVictory()) {
                System.out.println("Another capture available");
                choice = r.nextInt(len);
                if (choice % 2 == 1) {
                    choice -= 1;
                }
                move = moves.get(choice);
                b.makeMove(move[0], move[1], cpu);
                System.out.println("CPU did: " + move[0][0] + move[0][1] + " to " + move[1][0] + move[1][1]);
                moves = getValidMoves(false);
            }
        /*
        System.out.println("move: "+move[0][0]);
        System.out.println("move: "+move[0][1]);
        System.out.println("move: "+move[1][0]);
        System.out.println("move: "+move[1][1]);*/
        }
        //System.out.println("hi");
        b.showBoard();
    }

    public int[][] getUserMove() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter move");
        System.out.println("Checker: a b");

        int[] x = new int[0];
        int[] y = new int[0];

        boolean gettingChoice = true;
        while (gettingChoice) {
            String xs = myObj.nextLine();
            x = getDigits(xs);
            if (b.b[x[0]][x[1]] % 2 == b.getHumanColour() % 2) {
                gettingChoice = false;
            }
        }

        System.out.println("Destination: x y");
        gettingChoice = true;
        while (gettingChoice) {
            String ys = myObj.nextLine();
            y = getDigits(ys);
            if (b.b[x[0]][x[1]] % 2 == b.getHumanColour() % 2) {
                gettingChoice = false;
            }
        }

        int[][] xy = {x, y};
        return xy;
    }

    public int[] getDigits(String s) {
        String[] n = s.split(" ");
        int[] t = new int[]{Integer.valueOf(n[0]), Integer.valueOf(n[1])};
        return t;
    }


    public void changePlayer() {
        if (b.currentPlayer == human) {
            player = cpu;
            b.setCurrentPlayer(cpu);
        } else {
            player = human;
            b.setCurrentPlayer(human);
        }
    }



    public void gameOver() {
        System.out.println("black: " + b.blackCheckers);
        System.out.println("white: " + b.whiteCheckers);
        b.showBoard();
        if (b.whiteVictory()) {
            System.out.println("White has won!");
        } else if (b.blackVictory()) {
            System.out.println("Black has won!");
        }
    }

}