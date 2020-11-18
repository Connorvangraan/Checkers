import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Collection;


public class BoardGUI extends Application {
    GridPane g = new GridPane();
    int boxSize = 75;
    int windowSize = boxSize*8;

    Circle[] circles = new Circle[24];

    @Override
    public void start(Stage s) {
        s.setTitle("Checkers");

        setUpBoard();

        Scene sc = new Scene(g, windowSize, windowSize);
        s.setScene(sc);
        s.show();
    }

    public void setUpBoard(){
        for (int row=0; row<8; row++){
            for (int col=0; col<8; col++){
                if (row%2 != col%2){
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
                }
                ColumnConstraints colcon = new ColumnConstraints();
                colcon.setHalignment(HPos.CENTER);
                g.getColumnConstraints().add(colcon);
            }
        }
    }

    public Circle makeCircle(Color c){
        Circle  circle = new Circle(25);
        circle.setFill(c);

        return circle;
    }

}
