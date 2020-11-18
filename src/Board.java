import java.lang.Object;

public class Board {
    int[][] b;
    static int empty = 8;
    static int black = 1;
    static int white = 2;

    public Board() {
        setup();
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
                System.out.println("row:" + row);
                System.out.println("col:" + col);
                System.out.println();
                if (row < 3) {
                    b[row][col] = black;
                } else if (row > 4) {
                    b[row][col] = white;
                }
                else{
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

        showBoard();
    }

    public void showBoard() {
        String printb = "";
        for (int row = 0; row < b.length; row++) {
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
                    if (row%2 == 1){
                        printb = printb.concat(String.valueOf(b[row][col]));
                        printb = printb.concat(" 0 ");
                    }
                    else{
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

    public void makeMove(int[] x, int[] y, int player){
        if (validMove(x,y)){
            b[y[0]][y[1]] = b[x[0]][x[1]];
            b[x[0]][x[1]] = empty;
        }
        showBoard();
    }

    public boolean validMove(int[] x, int[] y){
        if (b[y[0]][y[1]] == empty || b[y[0]][y[1]] == b[x[0]][x[1]]+1 || b[y[0]][y[1]] == b[x[0]][x[1]]-1){
            return true;
        }
        System.out.println("Invalid move");
        return false;
    }
}

