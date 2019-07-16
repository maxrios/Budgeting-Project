package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

public class Driver extends Application {

    private static ArrayList<Summary> activeSummaries;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/sample.fxml"));
        Scene root = loader.load();
        primaryStage.setTitle("Budget_1.3");
        primaryStage.setResizable(false);
        primaryStage.setScene(root);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Controller controller = loader.getController();
        controller.setSummaryList();

        root.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        primaryStage.show();

    }


    public static void main(String[] args) {
        System.out.println("Building boot...");
        try {
            boot();
        } catch(FileNotFoundException e) {
            //If file is not found, create a new one
            try {
                System.out.println("Rebuilding boot...");
                rebuildBoot();
                boot();
            } catch(IOException f) {
                e.printStackTrace();
            }
        } catch(StreamCorruptedException e) {
            System.out.println("File is corrupt. Rebuilding boot...");
            new File("summaries.dat").delete();
            try {
                rebuildBoot();
                boot();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }

        launch(args);
    }

    private static void boot() throws IOException {
        activeSummaries = new ArrayList<>();

        ObjectInputStream oi = new ObjectInputStream(
                new FileInputStream(
                        new File("summaries.dat")));

        while(true) {
            try {
                activeSummaries.add((Summary) oi.readObject());
            } catch(Exception e) {
                break;
            }
        }

        oi.close();
    }

    private static void rebuildBoot() throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(
                        new File("summaries.dat")));

        os.close();
    }

    private static void reformatFile() throws IOException{
        ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(
                        new File("summaries.dat"), false));

        for(Summary s : activeSummaries) {
            os.writeObject(s);
        }

        os.close();
    }

    static void buildSummary() {
        Summary summary = new Summary();
        TextInputDialog td = new TextInputDialog();
        td.setTitle("New Summary");
        td.setHeaderText(null);
        td.setGraphic(null);
        td.setContentText("Summary Name:");

        td.showAndWait().ifPresent(summary::setName);
        addSummary(summary);

        //Helps to prevent lost progress
        try {
            reformatFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    static void buildEntry(int summaryIndex) {
        Entry entry = new Entry();

        TextInputDialog td = new TextInputDialog();
        td.setTitle("New Entry");
        td.setHeaderText(null);
        td.setGraphic(null);
        td.setContentText("Entry Name:");

        td.showAndWait().ifPresent(entry::setName);
        td.getEditor().clear();
        DecimalFormat format = new DecimalFormat( "#.00" );
        td.getEditor().setTextFormatter( new TextFormatter<>(c ->
        {
            if ( c.getControlNewText().isEmpty() )
            {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );

            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
            {
                return null;
            }
            else
            {
                return c;
            }
        }));
        td.setContentText("Entry Amount:");
        String amount = td.showAndWait().get();
        if(amount.isEmpty())
            amount = "0";
        entry.setAmount(Double.parseDouble(amount));
        activeSummaries.get(summaryIndex).addEntry(entry);

        //Helps to prevent lost progress
        try {
            reformatFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean addSummary(Summary summary) {
        return activeSummaries.add(summary);
    }

    static Summary removeSummary(int index) {
        Summary summary = activeSummaries.remove(index);

        //Helps to prevent lost progress
        try {
            reformatFile();
        } catch(IOException e) {
            e.printStackTrace();
        }


        return summary;
    }

    static String removeEntry(int summaryIndex, int entryIndex) {
        String entry = activeSummaries.get(summaryIndex).removeEntry(entryIndex);

        //Helps to prevent lost progress
        try {
            reformatFile();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return entry;
    }

    static ArrayList<Summary> getActiveSummaries() {
        return new ArrayList<>(activeSummaries);
    }

}
