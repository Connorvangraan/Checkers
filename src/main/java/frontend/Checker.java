package main.java.frontend;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;


public class Checker extends StackPane {
    int type, row, col, size;
    double originx, originy;
    boolean king = false;

    /**
     * Creates the checker piece seen on the board and stores its coordinates
     * @param t the type of checker
     * @param row the row it is on
     * @param col the column it is on
     * @param size the tilesize of the board
     */
    public Checker(int t, int row, int col, int size){ // 1 = white , 2 = black
        type = t;
        this.row = row;
        this.col = col;
        this.size = size;
        originx = (col*size) + (size/3)/2;
        originy = (row*size) + (size/3)/2;
        setLayoutX(originx);
        setLayoutY(originy);

        setWidth(size);
        setHeight(size);

        if (row%2 == 0){
            this.col--;
            this.col=this.col/2;
        } else {
            this.col=this.col/2;
        }

        Circle c = new Circle();
        c.setRadius(size/3);
        if (type == 1){
            c.setFill(Color.WHITE);
        }
        else{
            c.setFill(Color.BLACK);
        }
        getChildren().add(c);
        Ellipse e = new Ellipse(size * 0.2, size * 0.2);
        if (type == 1){
            e.setFill(Color.WHITE);
            e.setStroke(Color.BLACK);
        }
        else{
            e.setFill(Color.BLACK);
            e.setStroke(Color.WHITE);

        }
        e.setStrokeWidth(size * 0.05);
        e.setTranslateX((size - size * 0.5 * 2) / 2);
        e.setTranslateY((size - size * 0.5 * 2) / 2);

        getChildren().addAll(e);
    }

    /**
     * Type is the colour of the checker.
     * @return  White = 1, black = 2
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double[] getOrigins(){
        return new double[] {originx, originy};
    }

    /**
     * Changes the coordinates of the checker to the y,x on the board
     * @param y the row
     * @param x the column
     */
    public void move(double y, double x){
        col = (int)x;
        row = (int)y;

        if (row%2 == 0){
            x = (x*2)+1;
        }
        else{
            x = x*2;
        }

        originx = (x*size) + ((size/3)/2);
        originy = (row*size) + ((size/3)/2);
        setLayoutX(originx);
        setLayoutY(originy);
        //relocate(originx, originy);


    }

    /**
     * Run when an invalid move is attempted. Returns the check to original location
     */
    public void cancelMove(){
        relocate(originx, originy);
    }

    /**
     * gets the coordinates on the backend board (i.e. 4 columns, 8 rows)
     * @return coordinates
     */
    public int[] getCoords(){
        return new int[] {row,col};
    }

    /**
     * Gets the coordinates on the actual board
     * @return coordinates
     */
    public int[] getUIcoords(){
        if (row%2 == 0){
            return new int[] {row, (col*2)+1};
        }
        else{
            return new int[] {row, col*2};
        }
    }

    /**
     * Makes the checker king if it has reached the baseline
     */
    public void kingCheck(){
        if (type == 1 && row == 7){
            king=true;
            makeKing();

        }
        else if (type == 2 && row == 0){
            king=true;
            makeKing();
        }
    }

    /**
     * Draws the little crown on the checker, which is actually just three triangles next to each other, but dont tell the checker that
     */
    private void makeKing(){
        for (int i=0; i < 3; i++){
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(10.0, 0.0, 0.0, 10.0,20.0, 10.0);
            polygon.setFill(Color.KHAKI);
            //polygon.setStrokeWidth(0.5);
            //polygon.setStroke(Color.BLACK);
            getChildren().add(polygon);
            polygon.setTranslateX((polygon.getLayoutX()-8)+i*8);
            polygon.setTranslateY(-3);
        }
    }

    /**
     * @return true if checker is a king
     */
    public boolean isKing() {
        return king;
    }

    /**
     * Makes checker king
     * @param king true if king, false if not
     */
    public void setKing(boolean king) {
        this.king = king;
        makeKing();
    }
}
