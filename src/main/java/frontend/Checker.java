package main.java.frontend;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


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

        setOnMousePressed(event -> {
            mx = event.getSceneX();
            my = event.getSceneY();
        });

        setOnMouseDragged(event -> {
            //relocate(event.getSceneX() - mx + originx, event.getSceneY() - my + originy);
            relocate(event.getSceneX() - size/2, event.getSceneY());
        });
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
        col = (int)x;
        row = (int)y;

        System.out.println("Moving");

        if (row%2 == 0){
            x = (x*2)+1;
        }
        else{
            x = x*2;
        }
        //System.out.println("Coords: "+row+" "+x);

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

        }
        else if (type == 2 && row == 0){
            king=true;
            System.out.println("Congrats black checker, you are king");
        }
    }

    public boolean isKing() {
        return king;
    }

    public void setKing(boolean king) {
        this.king = king;
    }
}
