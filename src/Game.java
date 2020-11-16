import java.util.Scanner;

public class Game {
    Board b = new Board();
    String playerName;
    int player = 0;

    public Game(String playerName){
        System.out.println("Welcome "+playerName);
        this.playerName = playerName;
        run();
    }

    public void run(){

        boolean running = true;

        while (running){
            if (player == 0){
                int[][] move = getUserMove();
                b.makeMove(move[0],move[1],0);
            }

        }

        //int[] x = new int[] {2,0};
        //int[] y = new int[] {3,0};
        //b.makeMove(x,y,1);
    }

    public int[][] getUserMove(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter move");
        System.out.println("Checker: a b");
        String xs = myObj.nextLine();
        int[] x = getDigits(xs);

        System.out.println("Destination: x y");
        String ys = myObj.nextLine();
        int[] y = getDigits(ys);

        int[][] xy = {x,y};
        return xy;
    }

    public int[] getDigits(String s){
        String[] n = s.split(" ");
        int [] t = new int[] {Integer.valueOf(n[0]), Integer.valueOf(n[1])};
        return t;
    }
}
