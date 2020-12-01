package main.java.backend;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class BoardGUI extends Application {
    GridPane g = new GridPane();
    int boxSize = 75;
    int windowSize = boxSize*8;

    Circle[] circles = new Circle[24];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage s) throws InterruptedException {

        s.setTitle("Start Menu");
        Button startGame = new Button("Start Game");
        startGame.setOnAction((value ->  {
            startGameWindow();
        }));
        Button quitGame = new Button("Quit");

        Scene sc = new Scene(startGame);
        s.setScene(sc);
        s.show();

    }

    public void startGameWindow(){
        Stage gamewindow = new Stage();
        gamewindow.setTitle("Checkers");
        setUpBoard();
        Scene sc = new Scene(g, windowSize, windowSize);
        gamewindow.setScene(sc);
        gamewindow.show();
    }

    public void runGame() throws InterruptedException {
        System.out.println("Hello");
        Game game = new Game("Connor"); // add player name
    }

    public void setUpBoard() {
        //System.out.println("setupcalled");
        for (int row=0; row<8; row++){
            for (int col=0; col<8; col++) {
                if (row%2 != col%2) {
                    Region r = new Region();
                    r.setStyle("-fx-background-color: red; -fx-border-style: solid; -fx-border-width: 2; -fx-border-color: black; -fx-min-width: 75; -fx-min-height:75; -fx-max-width:75; -fx-max-height: 75;");
                    GridPane.setRowIndex(r, row);
                    GridPane.setColumnIndex(r, col);
                    g.getChildren().add(r);

                    if (row<3){
                        Circle circle = makeCircle(Color.WHITE);
                        GridPane.setRowIndex(circle, row);
                        GridPane.setColumnIndex(circle, col);
                        g.getChildren().add(circle);
                    }

                    else if(row>4){
                        Circle circle = makeCircle(Color.BLACK);
                        GridPane.setRowIndex(circle, row);
                        GridPane.setColumnIndex(circle, col);
                        g.getChildren().add(circle);
                    }

                    else{
                        Circle circle = makeCircle(Color.TRANSPARENT);
                        GridPane.setRowIndex(circle,row);
                        GridPane.setColumnIndex(circle, col);
                        g.getChildren().add(circle);
                    }
                }
                ColumnConstraints colcon = new ColumnConstraints();
                colcon.setHalignment(HPos.CENTER);
                g.getColumnConstraints().add(colcon);
            }
        }
        ObservableList<Node> children = g.getChildren();
        //System.out.println("Setup: "+children.size());
    }

    public Circle makeCircle(Color c) {
        Circle  circle = new Circle(25);
        circle.setFill(c);
        return circle;
    }

    public void moveChecker(int[] x, int[] y) {
        ObservableList<Node> children = g.getChildren();
        System.out.println("Children: "+children.size());
        //System.out.println(children.get(0));
        //for (int i=0, i<3)
    }

}
