package main.java.backend;

import java.util.ArrayList;


public class MiniMax {
    static int empty = 8;
    static int white = 1;
    static int black = 2;
    static int kingwhite = 3, kingblack = 4;
    static int secount, decount;

    Board b;
    int maximisingPlayer, minimisingPlayer, currentPlayer, difficulty;
    int[][] bestMove;

    /**
     * 1B, this whole class is 2C
     * Sets up the minimax function to be run on the board
     * @param board the current backend board
     * @param maximisingPlayer the player using the minimax
     * @param difficulty the depthlimit
     */
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

    /**
     * Makes a deepcopy of the board
     * @param original
     * @return
     */
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
        clone.setCurrentPlayer(original.getCurrentPlayer());
        clone.setColour(original.getHumanColour());
        return clone;
    }

    /**
     * Finds the best move based off the board
     * @return
     */
    public int[][] minimaxmove() {
        minimax(b, difficulty, maximisingPlayer, -1, 1);
        return bestMove;
    }

    /**
     * 2D
     * The actual minimax function that determines the best move
     * Board cloned before being passes in param during recursion
     * @param board
     * @param depth
     * @param currentPlayer
     * @param alpha
     * @param beta
     * @return
     */
    public double minimax(Board board, int depth, int currentPlayer, double alpha, double beta) { //, int alpha, int beta
        board.setCurrentPlayer(currentPlayer);
        double bestscore = -1;

        if (currentPlayer == maximisingPlayer) {
            bestscore = -1;
        } else {
            bestscore = 1;
        }

        if (depth == 0 || board.blackVictory() || board.whiteVictory()) {
            secount++;
            return getHeuristics(board,currentPlayer);
        }

        ArrayList<int[][]> moves = board.findMoves();

        int[][] tempmove = null;

        for (int[][] move : moves) {
            double currentscore;
            decount++;

            if (currentPlayer == maximisingPlayer) {
                Board clone = cloneBoard(board);
                clone.makeMove(move[0], move[1], maximisingPlayer); //put move here);

               if (move[1][0] == move[0][0]+2 || move[1][0] == move[0][0]-2) {
                   clone.findMoves();
                   if (clone.captureOption) {
                       currentscore = minimax(clone, depth - 1, maximisingPlayer, alpha, beta);
                       if (bestscore < currentscore) {
                           tempmove = move;
                           bestscore = currentscore;
                       }
                   }
               }
                currentscore = minimax(clone, depth - 1, minimisingPlayer, alpha, beta);
                if (bestscore < currentscore) {
                    tempmove = move;
                    bestscore = currentscore;
                }
                if (alpha < currentscore){
                    alpha = currentscore;
                }

                if (beta < alpha){
                    break;
                }

            } else {
                Board clone = cloneBoard(board);
                clone.makeMove(move[0], move[1], minimisingPlayer);//put move here);
                currentscore = minimax(clone, depth - 1, maximisingPlayer, alpha, beta);

                if (move[1][0] == move[0][0]+2 || move[1][0] == move[0][0]-2) {
                    clone.findMoves();
                    if (clone.captureOption) {
                        currentscore = minimax(clone, depth - 1, minimisingPlayer, alpha, beta);
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
                if (currentscore < beta){
                    beta = currentscore;
                }
                if (beta < alpha){
                    break;
                }
            }
        }
        bestMove = tempmove;
        return bestscore;
    }


    /**
     * Gets quantity difference in checkers on the board, as well as the amount of kings
     * @param board
     * @param player
     * @return heuristics value
     */
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
            return diffwhiteblack + (whitekings*1.5);
        } else {
            return diffblackwhite + (blackkings*1.5);
        }

    }
}



