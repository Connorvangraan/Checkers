import javafx.application.Application;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

/**
 * TODO:
 * add heuristics
 * add minimax
 * add pruning
 * add ui
 *
 */


public class Game {
    boolean verbose = false;
    Board b = new Board(verbose);
    String playerName;
    int human;
    int cpu;
    int player;

    static int capture = 2;

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
            b.setCurrentPlayer(human);
        }
        else{
            human=1;
            cpu=2;
            b.setCurrentPlayer(cpu);
        }

        //System.out.println(checkVictory());


        while (running){
            //System.out.println("Running");
            // black goes first. This is if the player is black
            if (b.getHumanColour() == b.black) {
                System.out.println("Player move");
                TimeUnit.SECONDS.sleep(1);
                userMove();
                changePlayer();
                TimeUnit.SECONDS.sleep(1);
                running = !checkVictory();

                if (running == true){
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

                if(running==true){
                    TimeUnit.SECONDS.sleep(1);
                    getValidMoves(true);
                    userMove();
                    running = !checkVictory();
                }
            }
            changePlayer();
        }
        System.out.println("Game over");
        gameOver();
    }

    public void userMove() {
        getValidMoves(true);
        int[][] move = getUserMove();
        int type = b.makeMove(move[0],move[1],human);
        getValidMoves(false);
        while (type == capture && b.captureOption && !checkVictory()){
            System.out.println("Another capture available");
            getValidMoves(true);
            move = getUserMove();
            b.makeMove(move[0],move[1],human);
            getValidMoves(false);
        }
        b.showBoard();
    }

    public void cpumove(){
        ArrayList<int[][]> moves = getValidMoves(false);
        int len = moves.size();
        if (len > 0){
            Random r = new Random();
            int choice = r.nextInt(len);
            if(choice%2 == 1){
                choice -= 1;
            }
            int[][] move = moves.get(choice);
            int type = b.makeMove(move[0],move[1],cpu);
            System.out.println("CPU did: "+move[0][0]+move[0][1]+" to "+move[1][0]+move[1][1]);
            moves = getValidMoves(false);

            while (type == capture && b.captureOption && !checkVictory()){
                System.out.println("Another capture available");
                choice = r.nextInt(len);
                if(choice%2 == 1){
                    choice -= 1;
                }
                move = moves.get(choice);
                b.makeMove(move[0],move[1],cpu);
                System.out.println("CPU did: "+move[0][0]+move[0][1]+" to "+move[1][0]+move[1][1]);
                moves = getValidMoves(false);
            }
        /*
        System.out.println("move: "+move[0][0]);
        System.out.println("move: "+move[0][1]);
        System.out.println("move: "+move[1][0]);
        System.out.println("move: "+move[1][1]);*/
        }
        b.showBoard();
    }

    public ArrayList<int[][]> getValidMoves(boolean show) {
        ArrayList<int[][]> m = b.findMoves();
        if (show){
            System.out.println("Move list:");
        }

        String moveList = "";

        // [row] [col = potential move] [0:1]=

        for (int row=0; row< m.size(); row++){
            moveList = moveList.concat(""+m.get(row)[0][0]+m.get(row)[0][1]+" "+m.get(row)[1][0]+m.get(row)[1][1] + "\n");
        }
        if (show){
            System.out.println(moveList);
        }
        return m;
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
            if (b.b[x[0]][x[1]]%2 == b.getHumanColour()%2){
                gettingChoice = false;
            }
        }

        System.out.println("Destination: x y");
        gettingChoice = true;
        while (gettingChoice) {
            String ys = myObj.nextLine();
            y = getDigits(ys);
            if (b.b[x[0]][x[1]]%2 == b.getHumanColour()%2){
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
        if (b.currentPlayer == human){
            player = cpu;
            b.setCurrentPlayer(cpu);
        }
        else{
            player = human;
            b.setCurrentPlayer(human);
        }
    }

    public boolean checkVictory(){
        if(b.blackVictory()){
            return true;
        }
        if(b.whiteVictory()){
            return true;
        }
        if(getValidMoves(false).size() <= 0){
            System.out.println("all out of moves");
            return true;
        }

        return false;
    }

    public void gameOver(){
        System.out.println("black: "+b.blackCheckers);
        System.out.println("white: "+b.whiteCheckers);
        if (b.whiteVictory()){
            System.out.println("White has won!");
        }
        else if(b.blackVictory()) {
            System.out.println("Black has won!");
        }
    }

}
