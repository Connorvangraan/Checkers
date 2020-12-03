package main.java.backend;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;


public class Game {
    boolean verbose = false;
    Board b = new Board(verbose);
    String playerName;
    int human, cpu, player,difficulty = 0;
    static int white = 1, black =2, capture = 2;
    boolean getRandom = true;

    /**
     * Doesn't do anything other than sets the player name
     * @param playerName 
     * @throws InterruptedException
     */
    public Game(String playerName) throws InterruptedException {
        this.playerName = playerName;
    }

    /**
     * Returns true if the move is legal and false if it is not
     * @param move move[0] is the source and move[1] is the target
     * @return
     */
    public boolean checkLegalMove(int[][] move) {
        return b.validMove(move[0], move[1]);
    }

    /**
     * Returns true if the capture is legal and false if it is not
     * @param move move[0] is the source and move[1] is the target
     * @return
     */
    public boolean checkLegalCapture(int[][] move) {
        return b.validCapture(move[0], move[1]);
    }

    /**
     * makes move on the backend board given by the param
     * @param move move[0] is the source and move[1] is the target
     * @return
     */
    public void makeMove(int[][] move) {
        b.makeMove(move[0], move[1], human);
    }

    /**
     * Makes capture on the backend board given by the param
     * @param move move[0] is the source and move[1] is the target
     * @return
     */
    public int[] makeCapture(int[][] move) {
        int[] c = b.capture(move[0],move[1]);
        b.showBoard();
        return c;
    }

    /**
     * Gets a list of all the valid moves for the user
     * @param show
     * @return
     */
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

    /**
     * sets the current player to the param
     * @param player same int as their colour
     */
    public void setCurrentPlayer(int player){
        b.setCurrentPlayer(player);
    }

    /**
     * Sets the colour of the player to the param
     * @param colour white = 1, black = 2
     */
    public void setPlayer(int colour){
        if (colour == 1){
            b.setColour(1);
        }
        else{
            b.setColour(2);
        }
    }

    /**
     * returns the current player
     * @return
     */
    public int getPlayer(){
        return b.getCurrentPlayer();
    }


    /**
     * Gets the cpu move for the cpu turn
     * If difficulty is 0 then random moves are used
     * @return move
     */
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

    /**
     * Converts coord x from the backend format to the frontend format
     * @param x
     * @return
     */
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

    /**
     * Returns true if the game is over
     * @return
     */
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

    /**
     * Returns the winner number of the game. white = 1, black = 2, none = 0
     * @return
     */
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

    /**
     * Used to see if multi jump is possible
     * Returns true if there are captures available
     * @return
     */
    public boolean possibleCaptures(){
        getValidMoves(false);
        return b.possibleCaptures();
    }

    /**
     * Produces error message depending on the type of error
     * @param move move associated with error
     * @param colour colour of the checker being moved
     * @return error message
     */
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

    /**
     * Sets the difficulty to x
     * @param x
     */
    public void setDifficulty(int x) {
        if (x == 0) {
            getRandom = true;
        }
        else{
            getRandom = false;
            difficulty=x;
        }
    }


}