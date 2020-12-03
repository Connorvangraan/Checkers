package main.java.backend;

import java.util.ArrayList;

public class Board {
    int[][] b;
    static int empty = 8;
    static int white = 1;
    static int black = 2;
    static int kingwhite = 3;
    static int kingblack = 4;
    int currentPlayer, humanColour, cpuColour, whiteCheckers = 0, blackCheckers = 0;
    boolean verbose, captureOption = false;

    public Board(boolean verbose) {
        this.verbose = verbose;
        setup();
    }

    /**
     * Sets current player to param
     * @param currentPlayer
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets quantity of white checkers
     * @return
     */
    public int getWhiteCheckers() {
        return whiteCheckers;
    }

    /**
     * Gets quantity of black checkers
     * @return
     */
    public int getBlackCheckers() {
        return blackCheckers;
    }

    /**
     * Sets human player colour to param and cpu as the other colour
     * @param human
     */
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

    /**
     * Gets the backend board values
     * @return
     */
    public int[][] getBoard() {
        return b;
    }

    public void setBoard(int[][] newb) {
        b = newb;
    }

    /**
     * captureOption is true if captures are possible and false if not
     * @return
     */
    public boolean possibleCaptures() {
        return captureOption;
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
    }

    /**
     * Used to print the backend board in development
     */
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
    }

    /**
     * Makes move on the backend board from value at x to y
     * Can make both captures and movements
     * Checks if checker should be a king and updates it
     * @param x coordinate on board
     * @param y coordinate on board
     * @param player player number
     * @return the type of move. Move = 1, capture = 2
     */
    public int makeMove(int[] x, int[] y, int player) {
        int type = 0;
        if (validMove(x, y)) {
            //System.out.println("Move");
            b[y[0]][y[1]] = b[x[0]][x[1]];
            b[x[0]][x[1]] = empty;
            type = 1; // type 1 = move
        } else if (validCapture(x, y)) {
            //System.out.println("Capture");
            capture(x, y);
            type = 2; // type 2 = capture
        }
        if (b[y[0]][y[1]] == black && y[0] == 0) {
            b[y[0]][y[1]] = kingblack;
            //System.out.println("Black checker becomes king");
        } else if (b[y[0]][y[1]] == white && y[0] == 7) {
            b[y[0]][y[1]] = kingwhite;
            //System.out.println("White checker becomes king");
        }
        return type;
    }

    /**
     * Runs capture on backend board
     * @param x source coord
     * @param y movement target coord
     * @return the captured checker coords
     */
    public int[] capture(int[] x, int[] y) {
        b[y[0]][y[1]] = b[x[0]][x[1]];
        b[x[0]][x[1]] = empty;

        int[] c = getCaptured(x, y);
        if (b[c[0]][c[1]] == kingwhite){
            b[y[0]][y[1]] = kingblack;
            whiteCheckers--;
        }
        else if(b[c[0]][c[1]] == kingblack){
            b[y[0]][y[1]] = kingwhite;
            blackCheckers--;
        }
        if (b[y[0]][y[1]] == white || b[y[0]][y[1]] == kingwhite) {
            b[c[0]][c[1]] = empty;
            blackCheckers --;
        } else {
            b[c[0]][c[1]] = empty;
            whiteCheckers --;
        }

        if (b[y[0]][y[1]] == white && y[0] == 7) {
            b[y[0]][y[1]] = kingwhite;
        } else if (b[y[0]][y[1]] == black && y[0] == 0) {
            b[y[0]][y[1]] = kingblack;
        }

        return c;
    }

    /**
     * Gets the captured checker coordinates
     * @param x source coord
     * @param y target coord
     * @return captured coord
     */
    private int[] getCaptured(int[] x, int[] y) {
        int i = (y[0] - x[0]) / 2;
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
        coord = new int[]{i + x[0], j};
        return coord;
    }

    /**
     * Checks if a capture is valid
     * @param x source coord
     * @param y movement target coord
     * @return true if capture is valid
     */
    public boolean validCapture(int[] x, int[] y) {
        if (y[0] < 0 || y[0] > 7 || y[1] < 0 || y[1] > 3) {
            return false;
        }


        if (b[x[0]][x[1]] == white && b[y[0]][y[1]] == empty) {
            if (y[0] < x[0]) {
                return false;
            }

            int i = y[0] - x[0];
            int j = 0;
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
            if (verbose) {
                System.out.println("Checker: " + (x[0] + (i / 2)) + (j));
            }
            // finds the space between and checks if there is an opposing checker there
            if (b[(x[0] + (i / 2))][j] != black && b[(x[0] + (i / 2))][j] != kingblack) {
                if (verbose) {
                    System.out.println("Nothing to capture");
                    System.out.println(b[(x[0] + (i / 2))][j]);
                }
                return false;
            }


        } else if (b[x[0]][x[1]] == black && b[y[0]][y[1]] == empty) {
            if (y[0] > x[0]) {
                return false;
            }

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
            if (verbose) {
                System.out.println("x: " + x[0] + x[1]);
                System.out.println("y: " + y[0] + y[1]);
                System.out.println("Checker: " + (x[0] + (i / 2)) + (x[1] + (j / 2)));
                System.out.println(b[(x[0] + (i / 2))][j]);
            }

            if (b[(x[0] + (i / 2))][j] != white && b[(x[0] + (i / 2))][j] != kingwhite) {
                if (verbose) {
                    System.out.println("Nothing to capture");
                }
                return false;
            }


        } else if ((b[x[0]][x[1]] == kingwhite || b[x[0]][x[1]] == kingblack) && b[y[0]][y[1]] == empty) {
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
            if (verbose) {
                System.out.println("x: " + x[0] + x[1]);
                System.out.println("y: " + y[0] + y[1]);
                System.out.println("Checker: " + (x[0] + (i / 2)) + (x[1] + (j / 2)));
                System.out.println(b[(x[0] + (i / 2))][j]);
            }

            if (b[x[0]][x[1]] == kingblack) {
                if (b[(x[0] + (i / 2))][j] != white && b[(x[0] + (i / 2))][j] != kingwhite) {
                    if (verbose) {
                        System.out.println("Nothing to capture: " + b[(x[0] + (i / 2))][j]);
                    }
                    return false;
                }
            } else if (b[x[0]][x[1]] == kingwhite) {
                if (b[(x[0] + (i / 2))][j] != black && b[(x[0] + (i / 2))][j] != kingblack) {
                    if (verbose) {
                        System.out.println("Nothing to capture");
                    }
                    return false;
                }
            }

        } else {
            if (verbose) {
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
                    if (verbose) {
                        System.out.println("invalid column");
                    }
                }
            } else {
                if (verbose) {
                    System.out.println("invalid rows up");
                }
            }
        } else {
            if (y[0] == x[0] + 2) {
                if (y[1] == x[1] + 1 || y[1] == x[1] - 1) {
                    return true;
                } else {
                    if (verbose) {
                        System.out.println("invalid column");
                    }
                }
            } else {
                if (verbose) {
                    System.out.println("invalid rows down");
                }
            }
        }
        return false;
    }

    /**
     * Checks if a move is valid
     * @param x source coord
     * @param y target coord
     * @return true if the move is valid
     */
    public boolean validMove(int[] x, int[] y) {
        if (verbose) {
            System.out.println("Checking: " + x[0] + x[1] + " " + y[0] + y[1]);
        }
        if (y[0] < 0 || y[0] > 7 || y[1] < 0 || y[1] > 3) {
            if (verbose) {
                System.out.println("Going off board");
            }
            return false;
        }

        // checks if where it is moving is free
        if (b[y[0]][y[1]] == empty) {
            // checks if the checker is moving too far
            if (b[x[0]][x[1]] == white && y[0] != x[0] + 1) {
                if (verbose) {
                    System.out.println("Too far to move");
                }
                return false;
            } else if (b[x[0]][x[1]] == black && y[0] != x[0] - 1) {
                if (verbose) {
                    System.out.println("Too far to move");
                }
                return false;
            } else if ((b[x[0]][x[1]] == kingblack && y[0] != x[0] - 1) && (b[x[0]][x[1]] == kingblack && y[0] != x[0] + 1)) {
                if (verbose) {
                    System.out.println("Too far to move");
                }
                return false;
            } else if ((b[x[0]][x[1]] == kingwhite && y[0] != x[0] - 1) && (b[x[0]][x[1]] == kingwhite && y[0] != x[0] + 1)) {
                if (verbose) {
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
                        if (verbose) {
                            System.out.println("Edge piece can't move here");
                        }
                    }
                }
                // other end of the board
                else if (x[1] == 3) {
                    if (y[1] == x[1]) {
                        return true;
                    } else {
                        if (verbose) {
                            System.out.println("Edge piece can't move here");
                        }
                    }
                }
                // if in the middle, checks if checker is going to reachable tiles
                else if (y[1] == x[1] || y[1] == x[1] + 1) {
                    return true;
                } else {
                    if (verbose) {
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
                    if (y[1] == x[1] || y[1] == x[1] - 1) {
                        return true;
                    }
                } else if (y[1] == x[1] - 1 || y[1] == x[1]) {
                    return true;
                } else {
                    if (verbose) {
                        System.out.println();
                        System.out.println("Does not line up. going up");
                        System.out.println("y " + y[0] + y[1]);
                        System.out.println("x " + x[0] + x[1]);
                    }
                    return false;
                }
            }


        } else {
            if (verbose) {
                System.out.println("Target not empty");
            }
            return false;
        }
        //System.out.println("Invalid move");
        return false;
    }

    /**
     * Checks if white team has won
     * @return true if they have
     */
    public boolean whiteVictory() {
        if (blackCheckers == 0) {
            return true;
        }
        int temp = currentPlayer;
        currentPlayer = black;
        if (findMoves().size() == 0) {
            currentPlayer = temp;
            return true;
        }
        currentPlayer=temp;
        return false;
    }

    /**
     * Checks if black team has won
     * @return true if they have
     */
    public boolean blackVictory() {
        if (whiteCheckers == 0) {
            return true;
        }
        int temp = currentPlayer;
        //System.out.println(temp);
        currentPlayer = white;
        //System.out.println(temp);
        if (findMoves().size() == 0) {
            currentPlayer=temp;
            //System.out.println(currentPlayer);
            return true;
        }
        currentPlayer=temp;
        return false;
    }

    /**
     * Finds all possible moves for the current player
     * @return arraylist of all legal moves
     */
    public ArrayList<int[][]> findMoves() {
        captureOption = false;
        // gets all possible moves for all a players checkers
        ArrayList<int[][]> moves = new ArrayList<>();
        ArrayList<int[][]> captures = new ArrayList<>();
        if (verbose) {
            System.out.println("Current player: " + currentPlayer);
            System.out.println("Black: " + black);
            System.out.println("White: " + white);
        }

        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[row].length; col++) {
                int[] current = new int[]{row, col};
                if (b[row][col] != empty) {
                    if (b[row][col] % 2 == currentPlayer % 2) {
                        if (verbose) {
                            System.out.println("Coord: " + row + col);
                        }
                        if (b[row][col] == white || b[row][col] == kingwhite || b[row][col] == kingblack) {
                            if (row % 2 == 0) {
                                // checks moves for even row
                                if (validMove(current, new int[]{row + 1, col})) {
                                    moves.add(new int[][]{current, new int[]{row + 1, col}});
                                }
                                if (validMove(current, new int[]{row + 1, col + 1})) {
                                    moves.add(new int[][]{current, new int[]{row + 1, col + 1}});
                                }
                            } else {
                                // checks movements for odd

                                if (validMove(current, new int[]{row + 1, col})) {
                                    moves.add(new int[][]{current, new int[]{row + 1, col}});
                                }

                                if (validMove(current, new int[]{row + 1, col - 1})) {
                                    moves.add(new int[][]{current, new int[]{row + 1, col - 1}});
                                }

                            }
                            // checks captures, this is not row dependent

                            if (validCapture(current, new int[]{row + 2, col - 1})) {
                                captures.add(new int[][]{current, new int[]{row + 2, col - 1}});
                            }

                            if (validCapture(current, new int[]{row + 2, col + 1})) {
                                captures.add(new int[][]{current, new int[]{row + 2, col + 1}});
                            }
                        }
                        if (b[row][col] == black || b[row][col] == kingwhite || b[row][col] == kingblack) {
                            // same for black counters
                            // checks for even row
                            if (row % 2 == 0) {
                                if (validMove(current, new int[]{row - 1, col})) {
                                    moves.add(new int[][]{current, new int[]{row - 1, col}});
                                }

                                if (validMove(current, new int[]{row - 1, col + 1})) {
                                    moves.add(new int[][]{current, new int[]{row - 1, col + 1}});
                                }

                            } else {
                                //System.out.println("here");
                                if (validMove(current, new int[]{row - 1, col})) {
                                    moves.add(new int[][]{current, new int[]{row - 1, col}});
                                }
                                if (validMove(current, new int[]{row - 1, col - 1})) {
                                    moves.add(new int[][]{current, new int[]{row - 1, col - 1}});
                                }
                            }
                            // checks for captures
                            if (validCapture(current, new int[]{row - 2, col - 1})) {
                                captures.add(new int[][]{current, new int[]{row - 2, col - 1}});
                            }
                            if (validCapture(current, new int[]{row - 2, col + 1})) {
                                captures.add(new int[][]{current, new int[]{row - 2, col + 1}});
                            }
                        }
                    }
                }
            }
        }

        // if there are captures available, they must be done
        if (captures.size() > 0) {
            captureOption = true;
            return captures;
        } else {
            captureOption = false;
            return moves;
        }
    }


}