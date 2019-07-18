package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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

/**
 *  Budgeting application that saves object states for long-term use.
 *  @author Maximiliano Merced Rios
 */
public class Driver extends Application {

    private static ArrayList<Summary> activeSummaries;
    private double xOffset = 0;
    private double yOffset = 0;
    private static final String PATH_TO_WINDOW = "resources/mainWindow.fxml",
                                PATH_TO_BOOT = "summaries.dat";


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TO_WINDOW));
        Scene root = loader.load();
        primaryStage.setTitle("Budget_1.3");
        primaryStage.setResizable(false);
        primaryStage.setScene(root);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Controller controller = loader.getController();
        controller.setSummaryList();

        root.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent ->
            {xOffset = mouseEvent.getSceneX(); yOffset = mouseEvent.getSceneY();});

        root.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent ->
            {primaryStage.setX(mouseEvent.getScreenX() - xOffset); primaryStage.setY(mouseEvent.getScreenY() - yOffset);});

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
        } catch(StreamCorruptedException e) { //If file is illegally modified, this deletes and creates a new file.
            System.out.println("File is corrupt. Rebuilding boot...");
            new File(PATH_TO_BOOT).delete();
            try {
                rebuildBoot();
                boot();
            } catch (IOException f) {
                f.printStackTrace();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    /**
     * Boots the program by creating new Summary objects, and adding them to the activeSummaries list.
     * @throws IOException
     */
    private static void boot() throws IOException {
        activeSummaries = new ArrayList<>();

        ObjectInputStream oi = new ObjectInputStream(
                new FileInputStream(
                        new File(PATH_TO_BOOT)));

        while(true) {
            try {
                activeSummaries.add((Summary) oi.readObject());
            } catch(Exception e) {
                break;
            }
        }

        oi.close();
    }

    /**
     * Creates a new boot file in case the original was removed.
     * @throws IOException
     */
    private static void rebuildBoot() throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(
                        new File(PATH_TO_BOOT)));

        os.close();
    }

    /**
     * Saves content of activeSummaries to the storage file.
     * @throws IOException
     */
    private static void reformatFile() throws IOException{
        ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(
                        new File(PATH_TO_BOOT), false));

        for(Summary s : activeSummaries) {
            os.writeObject(s);
        }

        os.close();
    }

    /**
     * Creates prompt for summary input requirements. If nothing is entered, default values are used.
     */
    static void buildSummary() {
        Summary summary = new Summary();
        TextInputDialog td = new TextInputDialog();
        td.setTitle("New Summary");
        td.setHeaderText(null);
        td.setGraphic(null);
        td.setContentText("Summary Name:");

        td.showAndWait().ifPresent(summary::setName);
        addSummary(summary);

        try {
            reformatFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates prompt for entry input requirements. If nothing is entered, default values are used.
     * @param summaryIndex
     */
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

        //Only allows numerical values to be entered into the prompt.
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

        try {
            reformatFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds summary to activeSummaries
     * @param summary
     * @return If summary was added
     */
    private static boolean addSummary(Summary summary) {
        return activeSummaries.add(summary);
    }

    /**
     * Removes summary from active summaries using an index of its position.
     * @param index
     * @return Removed summary
     */
    static Summary removeSummary(int index) {
        Summary summary = activeSummaries.remove(index);

        try {
            reformatFile();
        } catch(IOException e) {
            e.printStackTrace();
        }


        return summary;
    }

    /**
     * Removes entry from chosen summary using an index of its position, and its position in the summary.
     * @param summaryIndex
     * @param entryIndex
     * @return String of entry
     */
    static String removeEntry(int summaryIndex, int entryIndex) {
        String entry = activeSummaries.get(summaryIndex).removeEntry(entryIndex);

        try {
            reformatFile();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return entry;
    }

    /**
     * @return activeSummaries
     */
    static ArrayList<Summary> getActiveSummaries() {
        return new ArrayList<>(activeSummaries);
    }
}