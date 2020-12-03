package main.java.frontend;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    int row, col, size;
    boolean real;
    Checker checker;
    Trail trail;

    /**
     * Creates tile. If real then it is red, otherwise it is white
     * @param real true for tiles that can be moved on
     * @param row board row
     * @param col board column
     * @param size tilesize
     */
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

    /**
     * If a checker is on the tile then this is run,
     * @param c the checker on the tile
     */
    public void occupy(Checker c) {
        checker = c;
        trail = new Trail(size, row, col);
    }

    public void makeTrail(Trail t){
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
