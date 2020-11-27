import javafx.application.Application;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
/**
 * TODO:
 *  Fix checkers moving up and going left (too far too move)
 *  white going down right doesn't capture
 *  black can move backwards (both ways) and it shouldnt
 *  black can move sideways
 *  black can't move left
 *  black capturing left doesn't remove checker (treats as move)
 *
 *
 */


import java.util.Scanner;

public class Game {
    Board b = new Board();
    String playerName;
    int human;
    int cpu;
    int player = human;

    public Game(String playerName) throws InterruptedException {
        System.out.println("Welcome "+playerName);
        this.playerName = playerName;
        run();
    }

    public void run() throws InterruptedException {
        boolean running = true;
        Scanner colour = new Scanner(System.in);
        System.out.println("White: 1");
        System.out.println("Black: 2");
        int c = colour.nextInt();
        b.setColour(c);
        if(c%2 == 0){
            human=c;
            cpu=1;
            b.setCurrentPlayer(cpu);
        }
        else{
            human=1;
            cpu=c;
            b.setCurrentPlayer(human);
        }

        while (running){
            // black goes first. This is if the player is black
            if (b.getHumanColour() == b.black) {
                getValidMoves();
                int[][] move = getUserMove();
                b.makeMove(move[0],move[1],human);
                changePlayer();
                TimeUnit.SECONDS.sleep(1);

                System.out.println("CPU Move");
                getValidMoves();
                running = checkVictory();
            }
            // if the player is white
            else {
                System.out.println("CPU Move");
                getValidMoves();
                changePlayer();
                TimeUnit.SECONDS.sleep(1);

                int[][] move = getUserMove();
                b.makeMove(move[0],move[1],human);
                running = checkVictory();
            }
            changePlayer();
        }
        gameOver();
    }

    public void successor() {
    }

    public void getValidMoves() {
        ArrayList<int[][]> m = b.findMoves();
        System.out.println("Move list:");
        for (int row=0; row< m.size(); row++){
            for (int col = 0; col< m.get(row).length; col++){
                System.out.println("Moves: "+m.get(row)[col]);
            }
        }
    }

    public int[][] getUserMove(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter move");
        System.out.println("Checker: a b");

        int[] x = new int[0];
        int[] y = new int[0];

        boolean gettingChoice = true;
        while (gettingChoice) {
            String xs = myObj.nextLine();
            x = getDigits(xs);
            if (b.b[x[0]][x[1]] == b.getHumanColour()){
                gettingChoice = false;
            }
        }

        System.out.println("Destination: x y");
        gettingChoice = true;
        while (gettingChoice) {
            String ys = myObj.nextLine();
            y = getDigits(ys);
            if (b.b[x[0]][x[1]] == b.getHumanColour()){
                gettingChoice = false;
            }
        }

        int[][] xy = {x,y};
        return xy;
    }

    public int[] getDigits(String s){
        String[] n = s.split(" ");
        int [] t = new int[] {Integer.valueOf(n[0]), Integer.valueOf(n[1])};
        return t;
    }

    public boolean checkLegalMove(int[][] move){
        return b.validMove(move[0],move[1]);
    }

    public void changePlayer(){
        if (player == human){
            player = cpu;
        }
        else{
            player = human;
        }
    }

    public boolean checkVictory(){
        if(b.blackVictory()){
            return true;
        }
        if(b.whiteVictory()){
            return true;
        }
        return false;
    }

    public void gameOver(){
        if (b.whiteVictory()){
            System.out.println("White has won!");
        }
        else if(b.blackVictory()) {
            System.out.println("Black has won!");
        }
    }

}
