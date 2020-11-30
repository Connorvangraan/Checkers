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
    ArrayList<MoveAndScores> successorEvaluations;

    boolean verbose = false;

    int[][] bestMove;


    public MiniMax(int[][] board, int maximisingPlayer) {
        int[][] newboard = new int[8][4];
        for (int row=0; row < board.length; row++){
            for (int col=0; col<board[row].length; col++){
                newboard[row][col]=Integer.valueOf(String.valueOf(board[row][col]));
            }
        }
        b = new Board(false);
        b.setBoard(board);
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
        clone.setBoard(original.getBoard().clone());
        clone.setCurrentPlayer(original.getCurrentPlayer());
        clone.setColour(original.getHumanColour());
        return clone;
    }

    public int[][] minimaxmove() {
        minimax(b, 9, maximisingPlayer); //, -1, 1
        System.out.println("Static evals: " + secount);
        System.out.println("Dyanmic evals: " + decount);
        return bestMove;
    }

    // should be private
    public double minimax(Board board, int depth, int currentPlayer) { //, int alpha, int beta
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

        if (depth == 0 ) { //|| (board.findMoves().isEmpty())
            secount++;
            if (depth==0){
                System.out.println("Depth hit");
            }
            else{
                System.out.println("out of moves");
                board.showBoard();
            }
            return getHeuristics(board);
        }


        if (maximisingPlayer == black && board.whiteVictory()) {
            secount++;
            System.out.print(": black checkers eliminated - I would lose.");
            return getHeuristics(board);
        }
        if (maximisingPlayer == black && board.blackVictory()) {
            secount++;
            System.out.print(": white checkers eliminated - I would win.");
            return getHeuristics(board);
        }
        if (maximisingPlayer == white && board.blackVictory()) {
            secount++;
            System.out.print(": white checkers eliminated - I would lose.");
            return getHeuristics(board);
        }
        if (maximisingPlayer == white && board.whiteVictory()) {
            secount++;
            System.out.print(": black checkers eliminated - I would win.");
            return getHeuristics(board);
        }

        //System.out.println("here");
        ArrayList<int[][]> moves = board.findMoves();
        //System.out.println("Player " + board.getCurrentPlayer());
        //for (int[][] move : moves) {
         //   System.out.println("Move: " + move[0][0] + move[0][1] + " to " + move[1][0] + move[1][1]);
        //}

        for (int[][] move : moves) {
            double currentscore;
            decount++;

            if (currentPlayer == maximisingPlayer) {
                //System.out.println("beep");
                // do move on clone board here
                Board clone = cloneBoard(board);
                clone.makeMove(move[0], move[1], maximisingPlayer); //put move here);
                currentscore = minimax(clone, depth - 1, minimisingPlayer);
                //bestscore = Math.max(bestscore, currentscore);
                if (bestscore < currentscore) {
                    bestMove = move;
                    bestscore = currentscore;
                }
            } else {
                // do move on clone board here
                Board clone = cloneBoard(board);
                clone.makeMove(move[0], move[1], minimisingPlayer);//put move here);
                currentscore = minimax(clone, depth - 1, maximisingPlayer);
                //bestscore = Math.min(bestscore, currentscore);
                if (bestscore > currentscore){
                    bestscore = currentscore;
                }
            }
            /*
            if (depth == 0){
                successorEvaluations.add(new MoveAndScores(bestscore, move));
            }*/



        }
        return bestscore;
    }


    public int[][] aiMove() {
        successorEvaluations = new ArrayList<MoveAndScores>();
        secount = 0;
        decount = 0;

        maximisingPlayer = b.cpuColour;
        //minimax(0);
        b.setCurrentPlayer(b.cpuColour);
        return bestMove();
    }

    public int[][] bestMove() {
        int max = Integer.MIN_VALUE;
        int best = 0;

        for (int i = 0; i < successorEvaluations.size(); i++) {
            // checks if there is a value that is higher than max
            if (max < successorEvaluations.get(i).score) {
                max = successorEvaluations.get(i).score;
                best = i;
            }
        }
        return successorEvaluations.get(best).move;
    }

    public double getHeuristics(Board board) {
        double h;
        if (board.currentPlayer == white) {
            return board.getWhiteCheckers() - board.getBlackCheckers();
        } else { //board.currentPlayer == black
            System.out.println(board.getBlackCheckers() - board.getWhiteCheckers());
            return board.getBlackCheckers() - board.getWhiteCheckers();
        }

    }

}