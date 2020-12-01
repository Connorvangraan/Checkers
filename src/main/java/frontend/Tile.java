package main.java.frontend;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    int row, col;
    boolean real;
    Checker checker;

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

    }

    public void occupy(Checker c){
        checker = c;
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

}
