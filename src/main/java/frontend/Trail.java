package main.java.frontend;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Trail extends StackPane {
    double originx, originy;

    public Trail(int size, int row, int col){
        originx = (col*size) + (size/3)/2;
        originy = (row*size) + (size/3)/2;
        setLayoutX(originx);
        setLayoutY(originy);



        setWidth(size);
        setHeight(size);

        Circle c = new Circle();
        c.setRadius(size/3);
        c.setFill(Color.GRAY);
        c.setLayoutX((col*size) + (size/3)/2);
        c.setLayoutY((col*size) + (size/3)/2);
        c.setTranslateX((size - size * 0.5 * 2) / 2);
        c.setTranslateY((size - size * 0.5 * 2) / 2);
        getChildren().add(c);

    }
}
