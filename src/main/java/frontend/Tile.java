package main.java.frontend;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.TimeUnit;

public class Tile extends Rectangle {
    int row, col, size;
    boolean real;
    Checker checker;
    Trail trail;

    public Tile(boolean real, int row, int col,int size) {
        this.row = row;
        this.col = col;
        this.real = real;

        setWidth(size);
        setHeight(size);

        setLayoutX(col*size);
        setLayoutY(row*size);
        if (real){
            setFill(Color.RED);
        }
        else{
            setFill(Color.WHITE);
        }
        setStrokeWidth(5);

    }

    public void occupy(Checker c) throws InterruptedException {
        checker = c;
        trail = new Trail(size, row, col);
        //TimeUnit.SECONDS.sleep(1);
    }

    public void makeTrail(Trail t) throws InterruptedException {
        trail = t;
    }

    public void removeTrail(){
        trail = null;
    }

    public boolean occupied(){
        if (checker != null){
            return true;
        }
        else{
            return false;
        }
    }

    public Checker getChecker(){
        return checker;
    }

    public void markTarget(){
        setStroke(Color.BLUEVIOLET);
    }
    public void unmark(){
        setStroke(Color.TRANSPARENT);
    }

}
