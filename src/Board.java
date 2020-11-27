import javafx.application.Application;

import java.lang.Object;
import java.util.ArrayList;

public class Board {
    int[][] b;
    static int empty = 8;
    static int white = 1;
    static int black = 2;
    BoardGUI gui;
    int currentPlayer;
    int currentColour;
    int humanColour;
    int cpuColour;
    int whiteCheckers = 0;
    int blackCheckers = 0;


    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setColour(int human) {
        if (human == 1) {
            humanColour = white;
            cpuColour = black;
        } else {
            humanColour = black;
            cpuColour = white;
        }
        currentPlayer = black;
    }

    public int getHumanColour() {
        return humanColour;
    }

    public int getCpuColour() {
        return cpuColour;
    }

    public Board() {
        testsetup();
    }

    /**
     * Sets up the board
     * State rep = int[rows = 8] [columns - 4]
     * State rep only covers squares that can be moved too
     */
    public void setup() {
        b = new int[8][4];
        System.out.println(b[0].length);
        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[row].length; col++) {
                //System.out.println("row:" + row);
                //System.out.println("col:" + col);
                //System.out.println();
                if (row < 3) {
                    b[row][col] = black;
                    blackCheckers++;
                } else if (row > 4) {
                    b[row][col] = white;
                    whiteCheckers++;
                } else {
                    b[row][col] = empty;
                }
            }
        }

        /*
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                System.out.print(b[i][j]);
            }
            System.out.println();
        }*/
        new Thread(() -> Application.launch(BoardGUI.class)).start();
        gui = new BoardGUI();
        showBoard();
    }

    public void testsetup() {
        b = new int[8][4];
        System.out.println(b[0].length);
        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[row].length; col++) {
                if (row == 2 && col == 1) {
                    b[row][col] = white;
                    whiteCheckers++;
                } else if (row == 3 && col == 2) {
                    b[row][col] = black;
                    blackCheckers++;
                } else if (row == 3 && col == 1) {
                    b[row][col] = black;
                    blackCheckers++;
                } else {
                    b[row][col] = empty;
                }
            }
        }

        /*
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                System.out.print(b[i][j]);
            }
            System.out.println();
        }*/

        new Thread(() -> Application.launch(BoardGUI.class)).start();
        gui = new BoardGUI();
        showBoard();
    }

    public void showBoard() {
        String printb = "";
        for (int row = 0; row < b.length; row++) {
            printb = printb.concat("Row:" + row + "  ");
            for (int col = 0; col < b[row].length; col++) {
                if (row < 3 || row > 4) {
                    if (row % 2 == 0) {
                        printb = printb.concat("0 ");
                        printb = printb.concat(String.valueOf(b[row][col]));
                        printb = printb.concat(" ");
                    } else {
                        printb = printb.concat(String.valueOf(b[row][col]));
                        printb = printb.concat(" 0 ");
                    }
                } else {
                    if (row % 2 == 1) {
                        printb = printb.concat(String.valueOf(b[row][col]));
                        printb = printb.concat(" 0 ");
                    } else {
                        printb = printb.concat("0 ");
                        printb = printb.concat(String.valueOf(b[row][col]));
                        printb = printb.concat(" ");
                    }
                }
            }
            printb = printb.concat("\n");
        }
        System.out.println(printb);
        //gui.moveChecker(new int[]{0}, new int[]{1});
    }

    // from x to y
    public void makeMove(int[] x, int[] y, int player) {
        if (validMove(x, y)) {
            System.out.println("Move");
            b[y[0]][y[1]] = b[x[0]][x[1]];
            b[x[0]][x[1]] = empty;
        } else if (validCapture(x, y)) {
            System.out.println("Capture");
            capture(x, y);
        }
        showBoard();
    }

    public void capture(int[] x, int[] y) {
            /*
            if (y[1] > x[1]){
                b[y[0]][y[1]-1] = empty;
            }
            else { //if (x[1] > y[1])
                b[y[0]][y[1]+1] = empty;
            }*/
        b[y[0]][y[1]] = b[x[0]][x[1]];
        b[x[0]][x[1]] = empty;
        int checkerc = (y[0] - x[0]) / 2;
        int checkerr = (y[1] - x[1]) / 2;
        System.out.println(checkerc);
        System.out.println(checkerr);
        b[x[0] + checkerc][x[1] + checkerr] = empty;
        // 2 1 taking 3 2 to 4 3
        // 2 1 taking 3 1 to 4 0
        // 3 2 taking 2 1 to 1 1
    }

    private boolean validCapture(int[] x, int[] y) {
        // checks the row of the capturing token
        if (x[0] % 2 == 0) {
            // checks if the capture is going up or down
            if (x[0] < y[0]) {
                // checks if the capture is legal in terms of rows
                if (y[0] == x[0] + 2) {
                    // checks if the capture is legal in terms of columns
                    // also checks if there is an opposite token that can be captured
                    if (y[1] == x[1] + 1 && b[x[0] + 1][x[1] + 1] == 2) {
                        return true;
                    } else if (y[1] == x[1] - 1 && b[x[0] + 1][x[1] + 1] == 2) {
                        return true;
                    }
                }
            } else {
                if (y[0] == x[0] - 2) {
                    if (y[1] == x[1] + 1 && b[x[0] + 1][x[1] + 1] == 1) {
                        return true;
                    } else if (y[1] == x[1] - 1 && b[x[0] + 1][x[1] + 1] == 1) {
                        return true;
                    }
                }
            }
        }
        /*
        else{
            if(x[0] < y[0]){
                if(y[0] == x[0]+2){
                    if(y[1] == x[1]+1 || y[1] == x[1]-1){
                        return true;
                    }
                }
            }
            if(x[0] > y[0]){
                if (y[0] == x[0]-2){
                    if(y[1] == x[1]+1 || y[1] == x[1]-1){
                        return true;
                    }

                }
            }
        }*/

        return false;
    }

    public boolean validMove(int[] x, int[] y) {
        // checks if where it is moving is free
        if (b[y[0]][y[1]] == empty) {
            // checks the row of the checker
            if (x[0] % 2 == 0) {
                // checks if the row is valid
                if (y[0] == x[0] + 1) {
                    // checks if the checker is on the edge of the board
                    if (x[1] == 0) {
                        // checks if the checker is moving to a reachable tile
                        if (y[1] == x[1] || y[1] == x[1] + 1) {
                            return true;
                        }
                    }
                    // other end of the board
                    else if (x[1] == 3) {
                        if (y[1] == x[1]) {
                            return true;
                        }
                    }
                    // if in the middle, checks if checker is going to reachable tiles
                    else if (y[1] == x[1] || y[1] == x[1] + 1) {
                        return true;
                    } else {
                        System.out.println("Does not line up");
                        return false;
                    }
                } else {
                    System.out.println("Too far to move");
                    return false;
                }

            } else {
                // same thing above but for odd rows
                if (x[1] == 0) {
                    if (y[1] == x[1]) {
                        return true;
                    }
                } else if (x[1] == 3) {
                    if (y[1] == x[1] || y[1] == x[1] + 1) {
                        return true;
                    }
                } else if (y[1] == x[1] || y[1] == x[1] - 1) {
                    return true;
                } else {
                    System.out.println("Does not line up");
                    return false;
                }
            }
        } else {
            System.out.println("Target not empty");
            return false;
        }
        //System.out.println("Invalid move");
        return false;
    }

    public boolean whiteVictory() {
        return whiteCheckers == 0;
    }

    public boolean blackVictory() {
        return blackCheckers == 0;
    }

    public ArrayList<int[][]> findMoves() {
        // gets all possible moves for all a players checkers
        ArrayList<int[][]> moves = new ArrayList<>();

        System.out.println("Current player: " + currentPlayer);
        System.out.println("Black: " + black);
        System.out.println("White: " + white);
        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[row].length; col++) {
                int[] current = new int[]{row, col};
                if (b[row][col] == currentPlayer) {
                    System.out.println("Coord: " + b[row][col]);
                    if (currentPlayer == white) {
                        System.out.println("boop");
                        if (validMove(current, new int[]{row + 1, col})) {
                            moves.add(new int[][]{current, new int[]{row + 1, col}});
                        }
                        if (validMove(current, new int[]{row + 1, col + 1})) {
                            moves.add(new int[][]{current, new int[]{row + 1, col + 1}});
                        }
                        if (validCapture(current, new int[]{row + 2, col - 1})) {
                            moves.add(new int[][]{current, new int[]{row + 2, col - 1}});
                        }
                        if (validCapture(current, new int[]{row + 2, col + 1})) {
                            moves.add(new int[][]{current, new int[]{row + 2, col + 1}});
                        }
                    } else {
                        System.out.println("beep");
                        if (validMove(current, new int[]{row - 1, col})) {
                            moves.add(new int[][]{current, new int[]{row + 1, col}});
                        }
                        if (validMove(current, new int[]{row - 1, col + 1})) {
                            moves.add(new int[][]{current, new int[]{row + 1, col + 1}});
                        }
                        if (validCapture(current, new int[]{row - 2, col - 1})) {
                            moves.add(new int[][]{current, new int[]{row + 2, col - 1}});
                        }
                        if (validCapture(current, new int[]{row - 2, col + 1})) {
                            moves.add(new int[][]{current, new int[]{row + 2, col + 1}});
                        }
                    }

                }
            }
        }
        return moves;
    }


}

