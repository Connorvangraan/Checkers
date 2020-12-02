package main.java.frontend;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import main.java.testingboard.PieceType;

import static main.java.testingboard.GameUI.TILE_SIZE;


public class Checker extends StackPane {
    int type, row, col, size;
    double mx, my, originx, originy;
    boolean king = false;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double[] getOrigins(){
        return new double[] {originx, originy};
    }

    public void move(double y, double x){
        System.out.println(""+y+x);
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

    public void cancelMove(){
        System.out.println(""+originx+""+originy);
        relocate(originx, originy);
    }

    public int[] getCoords(){
        return new int[] {row,col};
    }
    public int[] getUIcoords(){
        if (row%2 == 0){
            return new int[] {row, (col*2)+1};
        }
        else{
            return new int[] {row, col*2};
        }
    }

    public void kingCheck(){
        if (type == 1 && row == 7){
            king=true;
            System.out.println("Congrats white checker, you are king");
            makeKing();

        }
        else if (type == 2 && row == 0){
            king=true;
            System.out.println("Congrats black checker, you are king");
            makeKing();
        }
    }
    public void makeKing(){
        for (int i=0; i < 3; i++){
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(new Double[]{
                    10.0, 0.0,  0.0, 10.0,20.0, 10.0
                     });
            polygon.setFill(Color.KHAKI);
            //polygon.setStrokeWidth(0.5);
            //polygon.setStroke(Color.BLACK);
            getChildren().add(polygon);
            polygon.setTranslateX((polygon.getLayoutX()-8)+i*8);
            polygon.setTranslateY(-3);
        }
    }

    public boolean isKing() {
        return king;
    }

    public void setKing(boolean king) {
        this.king = king;
    }
}
