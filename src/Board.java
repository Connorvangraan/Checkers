import javafx.application.Application;

import java.lang.Object;
import java.util.ArrayList;

public class Board {
    int[][] b;
    static int empty = 8;
    static int white = 1;
    static int black = 2;
    static int kingwhite = 3;
    static int kingblack = 4;
    BoardGUI gui;
    int currentPlayer;
    int currentColour;
    int humanColour;
    int cpuColour;
    int whiteCheckers = 0;
    int blackCheckers = 0;
    boolean verbose;


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

    public Board(boolean verbose) {
        this.verbose = verbose;
        //setup();
        testsetup();
    }

    /**
     * Sets up the board
     * State rep = int[rows = 8] [columns - 4]
     * State rep only covers squares that can be moved too
     */
    public void setup() {
        b = new int[8][4];
        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[row].length; col++) {
                //System.out.println("row:" + row);
                //System.out.println("col:" + col);
                //System.out.println();
                if (row < 3) {
                    b[row][col] = white;
                    whiteCheckers++;
                } else if (row > 4) {
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

        //new Thread(() -> Application.launch(BoardGUI.class)).start();
        //gui = new BoardGUI();
        showBoard();
    }

    public void testsetup() {
        b = new int[8][4];
        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[row].length; col++) {
                if (row == 2 && col == 1) {
                    b[row][col] = white;
                    whiteCheckers++;
                }else if (row == 2 && col == 2) {
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

        //new Thread(() -> Application.launch(BoardGUI.class)).start();
        //gui = new BoardGUI();
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
            if (b[y[0]][y[1]] == black && y[0]==0){
                b[y[0]][y[1]] = kingblack;
            }
            else if (b[y[0]][y[1]] == white && y[0]==7){
                b[y[0]][y[1]] = kingwhite;
            }

        } else if (validCapture(x, y)) {
            System.out.println("Capture");
            capture(x, y);
            if (b[y[0]][y[1]] == black && y[0]==0){
                b[y[0]][y[1]] = kingblack;
            }
            else if (b[y[0]][y[1]] == white && y[0]==7){
                b[y[0]][y[1]] = kingwhite;
            }
        }
        showBoard();
    }

    public void capture(int[] x, int[] y) {
        b[y[0]][y[1]] = b[x[0]][x[1]];
        b[x[0]][x[1]] = empty;

        int[] c = getCaptured(x,y);
        if (b[x[0]][x[1]] == white) {
            b[c[0]][c[1]] = empty;
            blackCheckers -= 1;
        } else {
            b[c[0]][c[1]] = empty;
            whiteCheckers -= 1;
        }

        // 2 1 taking 3 2 to 4 3
        // 2 1 taking 3 1 to 4 0
        // 3 2 taking 2 1 to 1 1
    }

    private int[] getCaptured(int[] x, int[] y) {
        int i = (y[0] - x[0])/2;
        int j = 0;
        int[] coord;
        // for even = 0 / 1
        // for odd = -1 / 0

        if (x[0] % 2 == 0) {
            if (y[1] > x[1]) {
                j = x[1] + 1;
            } else {
                j = x[1];
            }
        } else {
            if (y[1] > x[1]) {
                j = x[1];
            } else {
                j = x[1] - 1;
            }
        }
        coord = new int[] {i+x[0],j};
        return coord;
    }

    private boolean validCapture(int[] x, int[] y, int j) {
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

    private boolean validCapture(int[] x, int[] y) {
        if (y[0] < 0 || y[0] > 7 || y[1] < 0 || y[1] > 3) {
            return false;
        }

        // this part works out of there is a checker to capture
        if (b[x[0]][x[1]] == white && b[y[0]][y[1]] == empty) {
            int i = y[0] - x[0];
            int j = 0;
            // for even = 0 / 1
            // for odd = -1 / 0

            if (x[0] % 2 == 0) {
                //System.out.println("beep");
                if (y[1] > x[1]) {
                    j = x[1] + 1;
                } else {
                    j = x[1];
                }
            } else {
                if (y[1] > x[1]) {
                    j = x[1];
                } else {
                    j = x[1] - 1;
                }
            }
            if (verbose){
                System.out.println("Checker: " + (x[0] + (i / 2)) + (j));
            }
            // finds the space between and checks if there is an opposing checker there
            if (b[(x[0] + (i / 2))][j] != black) {
                if (verbose){
                    System.out.println("Nothing to capture");
                    System.out.println(b[(x[0] + (i / 2))][j]);
                }
                return false;
            }

        // trying to find checker to capture
        } else if (b[x[0]][x[1]] == black && b[y[0]][y[1]] == empty) {
            int i = y[0] - x[0];
            int j;
            //checks if row is even or odd
            if (x[0] % 2 == 0) {
                // checks if going up or down
                if (y[1] > x[1]) {
                    j = x[1] + 1;
                } else {
                    j = x[1];
                }
            } else {
                if (y[1] > x[1]) {
                    j = x[1];
                } else {
                    j = x[1] - 1;
                }
            }
            if (verbose){
                System.out.println("x: " + x[0] + x[1]);
                System.out.println("y: " + y[0] + y[1]);
                System.out.println("Checker: " + (x[0] + (i / 2)) + (x[1] + (j / 2)));
                System.out.println(b[(x[0] + (i / 2))][j]);
            }

            if (b[(x[0] + (i / 2))][j] != white) {
                if (verbose){
                    System.out.println("Nothing to capture");
                }
                return false;
            }


        } else {
            if (verbose){
                System.out.println("No checker to move, or target space is empty ");
            }
            return false;
        }


        // checks if the capture is going up or down
        if (x[0] > y[0]) {
            // checks the  amount of rows moved
            if (y[0] == x[0] - 2) {
                // checks the columns are correct
                if (y[1] == x[1] + 1 || y[1] == x[1] - 1) {
                    return true;
                } else {
                    if (verbose){
                        System.out.println("invalid column");
                    }
                }
            } else {
                if (verbose){
                    System.out.println("invalid rows up");
                }
            }
        } else {
            if (y[0] == x[0] + 2) {
                if (y[1] == x[1] + 1 || y[1] == x[1] - 1) {
                    return true;
                } else {
                    if (verbose){
                        System.out.println("invalid column");
                    }
                }
            } else {
                if (verbose){
                    System.out.println("invalid rows down");
                }
            }
        }
        return false;
    }


    public boolean validMove(int[] x, int[] y) {
        if (y[0] < 0 || y[0] > 7 || y[1] < 0 || y[1] > 3) {
            return false;
        }

        // checks if where it is moving is free
        if (b[y[0]][y[1]] == empty) {
            // checks if the checker is moving too far
            if (b[x[0]][x[1]] == white && y[0] != x[0] + 1) {
                if (verbose){
                    System.out.println("Too far to move");
                }
                return false;
            } else if (b[x[0]][x[1]] == black && y[0] != x[0] - 1) {
                if (verbose){
                    System.out.println("Too far to move");
                }
                return false;
            }
            // checks if the row of the checker is odd or even
            if (x[0] % 2 == 0) {
                // checks if the checker is on the edge of the board
                if (x[1] == 0) {
                    // checks if the checker is moving to a reachable tile
                    if (y[1] == x[1] || y[1] == x[1] + 1) {
                        return true;
                    } else {
                        if (verbose){
                            System.out.println("Edge piece can't move here");
                        }
                    }
                }
                // other end of the board
                else if (x[1] == 3) {
                    if (y[1] == x[1]) {
                        return true;
                    } else {
                        if (verbose){
                            System.out.println("Edge piece can't move here");
                        }
                    }
                }
                // if in the middle, checks if checker is going to reachable tiles
                else if (y[1] == x[1] || y[1] == x[1] + 1) {
                    return true;
                } else {
                    if (verbose){
                        System.out.println("Does not line up");
                    }
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
                } else if (y[1] == x[1] - 1 || y[1] == x[1]) {
                    return true;
                } else {
                    if (verbose){
                        System.out.println();
                        System.out.println("Does not line up. going up");
                        System.out.println("y " + y[0] + y[1]);
                        System.out.println("x " + x[0] + x[1]);
                    }
                    return false;
                }
            }


        } else {
            if (verbose){
                System.out.println("Target not empty");
            }
            return false;
        }
        //System.out.println("Invalid move");
        return false;
    }

    public boolean whiteVictory() {
        return blackCheckers == 0;
    }

    public boolean blackVictory() {
        return whiteCheckers == 0;
    }

    public ArrayList<int[][]> findMoves() {
        // gets all possible moves for all a players checkers
        ArrayList<int[][]> moves = new ArrayList<>();
        if (verbose){
            System.out.println("Current player: " + currentPlayer);
            System.out.println("Black: " + black);
            System.out.println("White: " + white);
        }

        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[row].length; col++) {
                int[] current = new int[]{row, col};
                if (b[row][col] == currentPlayer) {
                    if (verbose){
                        System.out.println("Coord: " + row + col);
                    }
                    if (currentPlayer == white) {
                        if (row % 2 == 0) {

                            // checks moves for even row
                            try {
                                if (validMove(current, new int[]{row + 1, col})) {
                                    moves.add(new int[][]{current, new int[]{row + 1, col}});
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (validMove(current, new int[]{row + 1, col + 1})) {
                                    moves.add(new int[][]{current, new int[]{row + 1, col + 1}});
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // checks movements for odd
                            // can probably move this
                            try {
                                if (validMove(current, new int[]{row + 1, col})) {
                                    moves.add(new int[][]{current, new int[]{row + 1, col}});
                                }
                            } finally {

                            }
                            try {
                                if (validMove(current, new int[]{row + 1, col - 1})) {
                                    moves.add(new int[][]{current, new int[]{row + 1, col - 1}});
                                }
                            } finally {

                            }
                        }
                        // checks captures, this is not row dependent
                        try {
                            if (validCapture(current, new int[]{row + 2, col - 1})) {
                                moves.add(new int[][]{current, new int[]{row + 2, col - 1}});
                            }
                        } finally {

                        }
                        try {
                            if (validCapture(current, new int[]{row + 2, col + 1})) {
                                moves.add(new int[][]{current, new int[]{row + 2, col + 1}});
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //System.out.println("boop");
                        // same for black counters
                        // checks for even row
                        if (row % 2 == 0) {
                            try {
                                if (validMove(current, new int[]{row - 1, col})) {
                                    moves.add(new int[][]{current, new int[]{row - 1, col}});
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (validMove(current, new int[]{row - 1, col + 1})) {
                                    moves.add(new int[][]{current, new int[]{row - 1, col + 1}});
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            try {
                                if (validMove(current, new int[]{row - 1, col})) {
                                    moves.add(new int[][]{current, new int[]{row - 1, col}});
                                }
                            } finally {

                            }
                            try {
                                if (validMove(current, new int[]{row - 1, col - 1})) {
                                    moves.add(new int[][]{current, new int[]{row - 1, col - 1}});
                                }
                            } finally {

                            }
                        }
                        // checks for captures
                        try {
                            if (validCapture(current, new int[]{row - 2, col - 1})) {
                                moves.add(new int[][]{current, new int[]{row - 2, col - 1}});
                            }
                        } finally {

                        }
                        try {
                            if (validCapture(current, new int[]{row - 2, col + 1})) {
                                moves.add(new int[][]{current, new int[]{row - 2, col + 1}});
                            }
                        } finally {
                        }
                    }
                }
            }
        }
        return moves;
    }


}

