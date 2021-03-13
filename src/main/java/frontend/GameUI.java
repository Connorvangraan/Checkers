package main.java.frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.java.backend.Game;
import main.java.backend.MiniMax;
import main.java.frontend.Checker;
import main.java.frontend.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameUI extends Application {
    public static int width = 8, height = 8, tilesize = 80;
    Tile[][] b = new Tile[width][height];
    Group tiles = new Group();
    Group checkers = new Group();
    Game game;
    int player, cpu, difficulty;
    String name;
    int[] ready = new int[3];
    boolean guide = true;
    double my,mx;
    boolean hint = false;


    /**
     * 2A
     *  Creates the UI board
     * @return returns the created board
     * @throws InterruptedException due to use of the time sleep functio
     */
    private Parent createBoard() {
        Pane root = new Pane();
        root.setPrefSize(height * tilesize, width * tilesize);
        root.getChildren().addAll(tiles, checkers);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Tile t;
                Checker c = null;
                if (row % 2 != col % 2) {
                    t = new Tile(true, row, col, tilesize);
                    if (row < 3) {
                        c = makeNewChecker(1, row, col);
                    } else if (row > 4) {
                        c = makeNewChecker(2, row, col);
                    }
                } else {
                    t = new Tile(false, row, col, tilesize);
                }
                tiles.getChildren().add(t);
                b[row][col] = t;
                if (c != null) {
                    t.occupy(c);
                    checkers.getChildren().add(c);
                }
            }
        }
        return root;
    }

    /**
     * 1A
     *
     * makes new checker at the given coordinates of the given type, returns the checker
     *
     * @param type the colour of the checker, white = 1 & black = 2
     * @param row the row of the checker
     * @param col the column of the checker
     * @return returns the new checker
     */
    public Checker makeNewChecker(int type, int row, int col) {
        Checker checker = new Checker(type, row, col, tilesize);
        checker.setOnMousePressed(event -> {
            mx = event.getSceneX();
            my = event.getSceneY();

            ArrayList<int[][]> moves = game.getValidMoves(false);
            for (int[][] move : moves) {
                if (move[0][0] == checker.getCoords()[0] && move[0][1] == checker.getCoords()[1]) {
                    //System.out.println("" + move[1][0] + " " + move[1][1]);
                    int[] target = game.convertCoord(move[1]);
                    //System.out.println("" + move[0] + move[1]);
                    if (guide) {
                        b[target[0]][target[1]].markTarget(Color.BLUEVIOLET);
                    }
                }
            }
        });

        checker.setOnMouseDragged(event -> {
            checker.setLayoutX(event.getSceneX() - tilesize / 2);
            checker.setLayoutY(event.getSceneY());
            //relocate(event.getSceneX() - tilesize/2, event.getSceneY());
        });

        checker.setOnMouseReleased(e -> {
            clearMarks();
            ArrayList<int[][]> moves = game.getValidMoves(false);
            boolean availableMove = false;
            for (int[][] move : moves) {
                //System.out.println("Move: " + move[0][0] + move[0][1]);
                //System.out.println("checker: " + checker.getCoords()[0] + checker.getCoords()[1]);
                if (checker.getCoords()[0] == move[0][0] && checker.getCoords()[1] == move[0][1]) {
                    availableMove = true;
                }
            }
            if (game.getPlayer() == checker.getType() && availableMove) {
                double targetx = checker.getLayoutX(); //toBoard(checker.getLayoutX());
                double targety = checker.getLayoutY(); //toBoard(checker.getLayoutY());
                int coordy = getCoord(targety);
                int coordx;
                if (coordy % 2 == 0) {
                    coordx = (getCoord(targetx) - 1) / 2;
                } else {
                    coordx = (getCoord(targetx)) / 2;
                }
                try {
                    moveChecker(checker, new int[]{coordy, coordx});
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            } else {
                checker.cancelMove();
            }

            if (hint){
                int[][] bestmove = game.getUserMove();
                if (bestmove != null){
                    int[] bestpiece = bestmove[0];
                    b[bestpiece[0]][game.convertCoord(bestpiece)[1]].markTarget(Color.FORESTGREEN);
                }

            }
        });
        return checker;
    }

    /**
     *1A
     * @param c the checker being moved
     * @param newCoords the coordinates the checker is being moved to
     * @throws InterruptedException due to use of the time sleep function
     */
    private void moveChecker(Checker c, int[] newCoords) throws InterruptedException {
        //System.out.println("Source: " + c.getCoords()[0] + " " + c.getCoords()[1]);
        //System.out.println("Target: " + newCoords[0] + " " + newCoords[1]);

        int[] origin = new int[]{c.getCoords()[0], c.getCoords()[1]};
        int[][] move = new int[][]{origin, newCoords};
        boolean moveAvailable = false;
        for (int[][] m : game.getValidMoves(false)){
            if (m[0][0] == move[0][0] && m[0][1] == move[0][1] && m[1][1] == move[1][1]){
                    moveAvailable=true;
            }
        }
        if (moveAvailable) {
            if (game.checkLegalMove(move)) {
                //System.out.println("Moving");
                game.makeMove(move);
                c.move(newCoords[0], newCoords[1]);
                b[c.getUIcoords()[0]][c.getUIcoords()[1]].occupy(c);
                b[origin[0]][game.convertCoord(origin)[1]].occupy(null);

                if (!game.checkVictory()) {
                    cpuMove();
                }
                if (game.checkVictory()) {
                    endScreen();
                }

            } else if (game.checkLegalCapture(move)) {
                //System.out.println("Capturing");
                int[] captured = game.makeCapture(move);
                c.move(newCoords[0], newCoords[1]);
                boolean kingcaptured = removeChecker(captured);
                if (kingcaptured) {
                    c.setKing(true);
                }
                b[c.getUIcoords()[0]][c.getUIcoords()[1]].occupy(c);
                b[origin[0]][game.convertCoord(origin)[1]].occupy(null);
                game.getValidMoves(false);

                if (!game.checkVictory() && (!game.possibleCaptures() || kingcaptured)) {
                    cpuMove();
                }
                if (game.checkVictory()) {
                    endScreen();
                }

            }
            else {
                c.cancelMove();
                popup(game.getError(move, player));
            }
        } else {
            c.cancelMove();
            popup(game.getError(move, player));
        }
        c.kingCheck();
    }

    /**
     * 1A, 2B
     * Run the cpu move, called after a user move
     * @throws InterruptedException due to use of the time sleep functio
     */
    public void cpuMove() throws InterruptedException {
        game.setCurrentPlayer(cpu);
        TimeUnit.MILLISECONDS.sleep(50);
        if (game.getValidMoves(false).size() > 0) {
            int[][] move = game.getcpuMove();

            if (move[1][0] == move[0][0] + 2 || move[1][0] == move[0][0] - 2) {
                int[] captured = game.makeCapture(move);
                Checker c = b[move[0][0]][game.convertCoord(move[0])[1]].getChecker();
                c.move(move[1][0], move[1][1]);
                b[game.convertCoord(move[1])[0]][game.convertCoord(move[1])[1]].occupy(c);
                b[game.convertCoord(move[0])[0]][game.convertCoord(move[0])[1]].occupy(null);
                boolean kingcapture = removeChecker(captured);
                if (kingcapture) {
                    c.setKing(true);
                }
                c.kingCheck();
                game.getValidMoves(false);
                if (game.possibleCaptures()) {
                    cpuMove();
                }
            } else {
                game.makeMove(move);
                Checker c = b[move[0][0]][game.convertCoord(move[0])[1]].getChecker();
                if (c == null) {
                    System.out.println("checker null at: " + move[0][0] + game.convertCoord(move[0])[1]);
                }
                assert c != null;
                c.move(move[1][0], move[1][1]);
                b[c.getUIcoords()[0]][game.convertCoord(move[1])[1]].occupy(c);
                b[move[0][0]][game.convertCoord(move[0])[1]].occupy(null);
                c.kingCheck();
            }
        }

        if (game.checkVictory()) {
            endScreen();
        }
        game.setCurrentPlayer(player);

    }

    /**
     * Removes the checker at the given coords from the board
     * @param captured the coordinates of the checker to remove
     * @return returns true if the captured checker was a king
     */
    public boolean removeChecker(int[] captured) {
        int col;
        if (captured[0] % 2 == 0) {
            col = (captured[1] * 2) + 1;
        } else {
            col = captured[1] * 2;
        }
        Checker removed = b[captured[0]][col].getChecker();
        boolean k = removed.isKing();
        checkers.getChildren().remove(removed);
        return k;
    }

    /**
     * Divides the value by tilesize to find the coordinate where the checker is dropped by the user
     * @param x the value to be scaled down
     * @return the scaled down value
     */
    private int getCoord(double x) {
        double coord = x / tilesize;
        return (int) Math.round(coord);
    }

    /**
     * Clears tile guidance marks when the user has made a move
     */
    public void clearMarks() {
        for (int t = 0; t < tiles.getChildren().size(); t++) {
            Tile tile = (Tile) tiles.getChildren().get(t);
            tile.unmark();
        }
    }

    /**
     * Creates popup window that alerts the user of the nature of the invalidity of their move
     * @param message details the issue
     */
    public void popup(String message){
        Stage s = new Stage();
        Label l = new Label(message);
        l.setStyle("-fx-font-weight: bold; -fx-font-size: 10pt;");
        Button b = new Button("OK");
        b.setStyle("-fx-font-weight: bold;");
        b.setOnAction(e -> {
            List<Window> windows = Window.getWindows();
            windows.get(1).hide();
        });
        VBox v = new VBox(l,b);
        Scene scene = new Scene(v);
        s.setScene(scene);
        s.setTitle("Error");
        s.show();
    }

    /**
     * 1B
     * The following all create a new window
     * Start, rules, game and end
     * Game is the board GUI
     *
     */
    public void startScreen(Stage s) {
        VBox v = new VBox();
        Button startb = new Button("Start");
        startb.setDisable(true);

        v.getChildren().add(startb);

        TextField text = new TextField("Enter name");
        //text.setOnKeyTyped();
        text.setOnKeyTyped(e -> {
            if (text.getCharacters().length() > 0) {
                ready[2] = 1;
                if (ready[0] == 1 && ready[1] == 1) {
                    startb.setDisable(false);
                }
            }
        });
        v.getChildren().add(text);

        HBox colourchoice = new HBox();
        Button whiteb = new Button("White");
        Button blackb = new Button("Black");

        whiteb.setOnAction(e -> {
            whiteb.setDisable(true);
            blackb.setDisable(false);
            player = 1;
            cpu = 2;
            ready[0] = 1;
            if (ready[1] == 1 && ready[2] == 1) {
                startb.setDisable(false);
            }
        });
        blackb.setOnAction(e -> {
            whiteb.setDisable(false);
            blackb.setDisable(true);
            player = 2;
            cpu = 1;
            ready[0] = 1;
            if (ready[1] == 1 && ready[2] == 1) {
                startb.setDisable(false);
            }
        });

        colourchoice.getChildren().addAll(whiteb, blackb);
        v.getChildren().add(colourchoice);

        ChoiceBox diffbox = new ChoiceBox();
        diffbox.getItems().addAll("Easy", "Medium", "Hard", "Very Hard");
        diffbox.setOnAction(e -> {
            ready[1] = 1;
            if (ready[0] == 1 && ready[2] == 1) {
                startb.setDisable(false);
            }
        });

        startb.setOnAction(e -> {
            if (player != 0) {
                String diff = (String) diffbox.getValue();
                switch (diff) {
                    case "Easy":
                        difficulty = 0;
                        break;
                    case "Medium":
                        difficulty = 4;
                        break;
                    case "Hard":
                        difficulty = 6;
                        break;
                    case "Very Hard":
                        difficulty = 10;
                        break;
                }
                try {
                    gameScreen();
                    s.close();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });

        ToggleButton guidelines = new ToggleButton("Click for movement guidelines");
        guidelines.setOnAction(e -> {
            guide = !guide;
        });

        ToggleButton hints = new ToggleButton("Click for hints");
        guidelines.setOnAction(e -> {
            hint = !hint;
        });

        Button rules = new Button("Rules");
        rules.setOnAction(e -> rulesScreen());

        Button quitb = new Button("Quit");
        quitb.setOnAction(e -> Platform.exit());
        v.getChildren().addAll(diffbox, guidelines, hints, rules, quitb);
        Scene scene = new Scene(v);
        s.setTitle("Checkers");
        s.setScene(scene);
        s.show();
    }

    public void rulesScreen() {
        Stage s = new Stage();
        Label title = new Label("Rules of checkers");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20pt;");
        String r = "- The objective is to eliminate all opposing checkers, or block you opponent from making any more moves\n" +
                "- Black moves first and play alternates\n" +
                "- Two types of moves: capturing and non-capturing\n" +
                "- On a non capturing move, a checker can be moved forward one square diagonally\n" +
                "- On a capturing move, a piece can make multiple jumps if more  capturing moves are available\n" +
                "- When a player is in position to make a capture move, he must take it" +
                "- When a piece gets to the other end of the board, or captures a king, it becomes a king, and can move and take in both directions\n" +
                "- When a king is captured, the turn ends\n";
        Label rules = new Label(r);
        rules.setWrapText(true);
        rules.setStyle("-fx-font-size: 12pt;");
        VBox v = new VBox(title, rules);
        Scene scene = new Scene(v, 600, 600);
        s.setScene(scene);
        s.setTitle("Rules");
        s.show();
    }

    public void gameScreen() throws InterruptedException {
        Stage gameStage = new Stage();
        Scene scene = new Scene(createBoard());
        gameStage.setTitle("Checkers");
        gameStage.setScene(scene);
        gameStage.show();
        game = new Game(name);
        game.setDifficulty(difficulty);

        if (player == 1) {
            game.setCurrentPlayer(cpu);
            game.setPlayer(1);
            cpuMove();
        } else {
            game.setCurrentPlayer(player);
            game.setPlayer(2);
        }
    }

    public void endScreen() {
        List<Window> windows = Window.getWindows();
        windows.get(0).hide();

        Stage s = new Stage();
        String winner = name;
        int x = 0;
        if (game.getVictor() == cpu) {
            winner = "The computer";
        }
        Label l = new Label(winner + " has won!");
        l.setStyle("-fx-font-weight: bold; -fx-font-size: 20pt;");
        Button playagain = new Button("Play again");
        playagain.setOnAction(e -> {
            b = new Tile[width][height];
            tiles = new Group();
            checkers = new Group();
            startScreen(new Stage());
            s.close();
        });
        Button quitb = new Button("Quit");
        quitb.setOnAction(e -> Platform.exit());

        HBox h = new HBox(playagain, quitb);
        VBox v = new VBox(l, h);
        v.setAlignment(Pos.CENTER);
        v.setSpacing(10);
        h.setAlignment(Pos.CENTER);
        Scene scene = new Scene(v, 600, 600);
        s.setScene(scene);
        s.setTitle("Game Over");
        s.show();
    }

    @Override
    public void start(Stage primaryStage) {
        startScreen(primaryStage);
    }

    public static void main(String[] args){
        launch(args);
    }

}
