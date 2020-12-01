package main.java.backend;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FxDragDropExample3 extends Application
{
    // Create the TextFields
    TextField sourceFld = new TextField("This is the source text");
    TextField targetFld = new TextField("Drag and drop the source text here");

    // Create the LoggingArea
    TextArea loggingArea = new TextArea("");

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        // Create the Labels
        Label headerLbl = new Label("Drag and drop the source text field onto the target text field.");
        Label sourceLbl = new Label("Gesture Source:");
        Label targetLbl = new Label("Gesture Target:");

        // Set the Prompt on the TextFields
        sourceFld.setPromptText("Enter text to drag");
        targetFld.setPromptText("Drag the source text here");

        // Create the GridPane
        GridPane pane = new GridPane();
        pane.setHgap(5);
        pane.setVgap(20);

        // Add the Labels and Fields to the Pane
        pane.add(headerLbl, 0, 0, 2, 1);
        pane.addRow(1, sourceLbl, sourceFld);
        pane.addRow(2, targetLbl, targetFld);

        // Add mouse event handlers for the source
        sourceFld.setOnDragDetected(new EventHandler <MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                writelog("Event on Source: drag detected");
                dragDetected(event);
            }
        });

        sourceFld.setOnDragDone(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                writelog("Event on Source: drag done");
                dragDone(event);
            }
        });

        // Add mouse event handlers for the target
        targetFld.setOnDragOver(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                writelog("Event on Target: drag over");
                dragOver(event);
            }
        });

        targetFld.setOnDragDropped(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                writelog("Event on Target: drag dropped");
                dragDropped(event);
            }
        });

        // Create the VBox
        VBox root = new VBox();
        // Add the Pane and The LoggingArea to the VBox
        root.getChildren().addAll(pane,loggingArea);
        // Set the Style of the VBox
        root.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title
        stage.setTitle("A Drag and Drop Example");
        // Display the Stage
        stage.show();
    }

    private void dragDetected(MouseEvent event)
    {
        // User can drag only when there is text in the source field
        String sourceText = sourceFld.getText();

        if (sourceText == null || sourceText.trim().equals(""))
        {
            event.consume();
            return;
        }

        // Initiate a drag-and-drop gesture
        Dragboard dragboard = sourceFld.startDragAndDrop(TransferMode.COPY_OR_MOVE);

        // Add the source text to the Dragboard
        ClipboardContent content = new ClipboardContent();
        content.putString(sourceText);
        dragboard.setContent(content);
        event.consume();
    }

    private void dragOver(DragEvent event)
    {
        // If drag board has a string, let the event know that
        // the target accepts copy and move transfer modes
        Dragboard dragboard = event.getDragboard();

        if (dragboard.hasString())
        {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }

    private void dragDropped(DragEvent event)
    {
        // Transfer the data to the target
        Dragboard dragboard = event.getDragboard();

        if (dragboard.hasString())
        {
            targetFld.setText(dragboard.getString());

            // Data transfer is successful
            event.setDropCompleted(true);
        }
        else
        {
            // Data transfer is not successful
            event.setDropCompleted(false);
        }

        event.consume();
    }

    private void dragDone(DragEvent event)
    {
        // Check how data was transfered to the target. If it was moved, clear the text in the source.
        TransferMode modeUsed = event.getTransferMode();

        if (modeUsed == TransferMode.MOVE)
        {
            sourceFld.setText("");
        }

        event.consume();
    }

    // Helper Method for Logging
    private void writelog(String text)
    {
        this.loggingArea.appendText(text + "\n");
    }
}