package main.java.backend;

import main.java.backend.Board;

import java.util.ArrayList;

public class MiniMax {
    static int empty = 8;
    static int white = 1;
    static int black = 2;

    Board b;
    int maximisingPlayer;
    int minimisingPlayer;
    int currentPlayer;

    static int secount, decount;

    int[][] bestMove;

    int difficulty;

    static int kingwhite = 3, kingblack = 4;

    public MiniMax(int[][] board, int maximisingPlayer, int difficulty) {
        int[][] newboard = new int[8][4];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                newboard[row][col] = Integer.valueOf(String.valueOf(board[row][col]));
            }
        }
        b = new Board(false);
        b.setBoard(board);
        this.difficulty = difficulty;

        this.maximisingPlayer = maximisingPlayer;
        currentPlayer = maximisingPlayer;
        b.setCurrentPlayer(maximisingPlayer);
        if (maximisingPlayer == black) {
            minimisingPlayer = white;
        } else {
            minimisingPlayer = black;
        }
    }

    public Board cloneBoard(Board original) {
        Board clone = new Board(false);
        int[][] tempboard = original.getBoard().clone();
        int[][] clonedboard = new int[8][4];
        for (int row = 0; row < tempboard.length; row++) {
            for (int col = 0; col < tempboard[row].length; col++) {
                clonedboard[row][col] = Integer.valueOf(String.valueOf(tempboard[row][col]));
            }
        }
        clone.setBoard(clonedboard);
        String player = String.valueOf(original.getCurrentPlayer());
        clone.setCurrentPlayer(Integer.valueOf(player));
        String colour = String.valueOf(original.getHumanColour());
        clone.setColour(Integer.valueOf(colour));
        return clone;
    }

    public int[][] minimaxmove() {
        minimax(b, difficulty, maximisingPlayer); //, -1, 1
        System.out.println("Static evals: " + secount);
        System.out.println("Dyanmic evals: " + decount);
        return bestMove;
    }

    // should be private
    public double minimax(Board board, int depth, int currentPlayer) { //, int alpha, int beta
        //System.out.println("minimising: "+minimisingPlayer);
        //System.out.println("maximising: "+maximisingPlayer);
        board.setCurrentPlayer(currentPlayer);
        //System.out.println("Player " + board.getCurrentPlayer());
        double bestscore = -1;

        int humanColour = board.getHumanColour();
        int cpuColour = board.getCpuColour();

        if (currentPlayer == maximisingPlayer) {
            bestscore = -1;
        } else {
            bestscore = 1;
        }

        if (depth == 0 || board.blackVictory() || board.whiteVictory()) { //|| (board.findMoves().isEmpty())
            secount++;
            return getHeuristics(board,currentPlayer);
        }

        ArrayList<int[][]> moves = board.findMoves();

        int[][] tempmove = null;

        for (int[][] move : moves) {
            double currentscore;
            decount++;

            if (currentPlayer == maximisingPlayer) {
                //System.out.println("beep");
                // do move on clone board here
                Board clone = cloneBoard(board);
                clone.makeMove(move[0], move[1], maximisingPlayer); //put move here);

               if (move[1][0] == move[0][0]+2 || move[1][0] == move[0][0]-2) {
                   clone.findMoves();
                   if (clone.captureOption) {
                       currentscore = minimax(clone, depth - 1, maximisingPlayer);
                       if (bestscore < currentscore) {
                           tempmove = move;
                           bestscore = currentscore;
                       }
                   }
               }
                currentscore = minimax(clone, depth - 1, minimisingPlayer);
                //bestscore = Math.max(bestscore, currentscore);
                if (bestscore < currentscore) {
                    tempmove = move;
                    bestscore = currentscore;
                }
            } else {
                // do move on clone board here
                Board clone = cloneBoard(board);
                clone.makeMove(move[0], move[1], minimisingPlayer);//put move here);
                currentscore = minimax(clone, depth - 1, maximisingPlayer);

                if (move[1][0] == move[0][0]+2 || move[1][0] == move[0][0]-2) {
                    clone.findMoves();
                    if (clone.captureOption) {
                        currentscore = minimax(clone, depth - 1, minimisingPlayer);
                        if (bestscore < currentscore) {
                            tempmove = move;
                            bestscore = currentscore;
                        }
                    }
                }

                if (bestscore > currentscore) {
                    bestscore = currentscore;
                    tempmove = move;
                }
            }
        }
        bestMove = tempmove;
        return bestscore;
    }


    public double getHeuristics(Board board,int player) {
        double h;
        int whitekings = 0;
        int blackkings = 0;
        int whitecheckers = 0;
        int blackcheckers = 0;

        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++) {
                if (board.getBoard()[row][col] == kingwhite) {
                    whitekings++;
                }
                if (board.getBoard()[row][col] == kingblack) {
                    blackkings++;
                }
                if (board.getBoard()[row][col] == white){
                    whitecheckers++;
                }
                if (board.getBoard()[row][col] == black){
                    blackcheckers++;
                }
            }
        }
        int diffblackwhite = blackcheckers - whitecheckers;
        int diffwhiteblack = whitecheckers - blackcheckers;

        if (maximisingPlayer == white) {
            System.out.println(maximisingPlayer);
            int checkerdiff = board.getWhiteCheckers() - board.getBlackCheckers();
            return diffwhiteblack + (whitekings*1.5);// - board.getBlackCheckers() ;//+ (whitekings * 1.2)
        } else {
            int checkerdiff = board.getBlackCheckers() - board.getWhiteCheckers();
            return diffblackwhite + (blackkings*1.5);// - board.getWhiteCheckers() ;//+ (blackkings  * 1.2)
        }

    }
}



