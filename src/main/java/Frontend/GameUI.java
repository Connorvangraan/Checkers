package main.java.Frontend;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.java.backend.*;

public class GameUI extends Application {
    public static int width=8, height=8, tilesize=80;
    Tile[][] b = new Tile[width][height];
    Group tiles = new Group();
    Group checkers = new Group();
    Game game;


    private Parent createBoard() {
        Pane root = new Pane();
        root.setPrefSize(height*tilesize, width*tilesize);
        root.getChildren().addAll(tiles,checkers);

        for (int row=0; row<height; row++){
            for (int col=0; col<width; col++){
                Tile t;
                Checker c = null;
                if (row%2 != col%2){
                    t = new Tile(true, row, col, tilesize);
                    if (row < 3){
                        c = makeNewChecker(1,row,col);
                        //System.out.println(""+row+col);
                    }
                    else if (row > 4){
                        c = makeNewChecker(2,row,col);
                        //System.out.println(""+row+col);
                    }
                }
                else{
                    t = new Tile(false, row, col,tilesize);
                }
                tiles.getChildren().add(t);
                b[row][col] = t;
                if (c != null){
                    t.occupy(c);
                    checkers.getChildren().add(c);
                }
            }
        }
        return root;
    }

    public Checker makeNewChecker(int type, int row, int col){
        Checker checker = new Checker(type, row, col,tilesize);
        checker.setOnMouseReleased(e -> {
            double targetx = checker.getLayoutX(); //toBoard(checker.getLayoutX());
            double targety = checker.getLayoutY(); //toBoard(checker.getLayoutY());
            int coordy = getCoord(targety);
            int coordx;
            if (coordy%2 == 0){
                coordx = (getCoord(targetx)-1)/2;
            }
            else{
                coordx = (getCoord(targetx))/2;
            }

            System.out.println();
            System.out.println("Source: "+ checker.getCoords()[0]+" "+checker.getCoords()[1]);
            System.out.println("Target: "+coordy+" "+coordx);

            moveChecker(checker,new int[] {coordy, coordx});
            /*
            if (checker.getCoords()[0]%2 && ){

            }
            else{
                checker.move(checker.getOrigins()[0]/tilesize, checker.getOrigins()[1]/tilesize);
            }*/

        });
        return checker;
    }

    private void moveChecker(Checker c ,int[] newCoords) {
        System.out.println("Source: "+c.getCoords()[0]+" "+c.getCoords()[1]);
        System.out.println("Target: "+newCoords[0]+" "+newCoords[1]);

        int oldCoordy = c.getCoords()[0];
        int oldCoordx = c.getCoords()[1];
        int[] origin = new int[] {oldCoordy, oldCoordx};
        if (game.makeMove(new int[][] {origin, newCoords})){
            System.out.println("Moving");
            c.move(newCoords[0],newCoords[1]);
        }
        else{
            c.cancelMove();
        }


    }

    private int getCoord(double x){
        double coord = x/tilesize;
        return (int) Math.round(coord);
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        Scene scene = new Scene(createBoard());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
        game = new Game("Connor");


    }

    public static void main(String[] args) {
        launch(args);
    }
}
