package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

public class Client extends Application{

    private static final int PORT = 8778;
    private static ArrayList<Summary> activeSummaries;
    private double xOffset = 0, yOffset = 0;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static Socket socket;

    private static final String PATH_TO_MAIN_WINDOW = "resources/mainWindow.fxml";
    private static final String PATH_TO_INFO_WINDOW = "resources/clientInfoWindow.fxml";

    private static Scene root, clientInfoScene;
    private static Stage stage;
    private static FXMLLoader loaderMain, loaderInfo;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loaderMain = new FXMLLoader(getClass().getResource(PATH_TO_MAIN_WINDOW));
        loaderInfo = new FXMLLoader(getClass().getResource(PATH_TO_INFO_WINDOW));
        clientInfoScene = loaderInfo.load();
        root = loaderMain.load();
        stage = primaryStage;

        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setScene(clientInfoScene);

        root.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {xOffset = mouseEvent.getSceneX(); yOffset = mouseEvent.getSceneY();});

        root.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent ->
        {stage.setX(mouseEvent.getScreenX() - xOffset); stage.setY(mouseEvent.getScreenY() - yOffset);});

        stage.show();

    }

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    static void buildSummary() {
        Summary summary = new Summary();
        TextInputDialog td = new TextInputDialog();
        td.initStyle(StageStyle.TRANSPARENT);
        td.setTitle("New Summary");
        td.setHeaderText(null);
        td.setGraphic(null);
        td.setContentText("Summary Name:");

        td.showAndWait().ifPresent(summary::setName);
        activeSummaries.add(summary);

        try {
            out.writeObject(activeSummaries);
            out.flush();
            out.reset();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    static void removeSummary(int summaryIndex) {
        activeSummaries.remove(summaryIndex);

        try {
            out.writeObject(activeSummaries);
            out.flush();
            out.reset();
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
        td.initStyle(StageStyle.TRANSPARENT);
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
            out.writeObject(activeSummaries);
            out.flush();
            out.reset();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes entry from chosen summary using an index of its position, and its position in the summary.
     * @param summaryIndex
     * @param entryIndex
     */
    static void removeEntry(int summaryIndex, int entryIndex) {
        activeSummaries.get(summaryIndex).removeEntry(entryIndex);

        try {
            out.writeObject(activeSummaries);
            out.flush();
            out.reset();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    static void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<Summary> getActiveSummaries() {
        return new ArrayList<>(activeSummaries);
    }

    static void clientInfo(String firstName, String lastName, String password) throws IOException{
        out.writeInt((firstName + lastName + password).hashCode());
        out.flush();
        try {
            activeSummaries = (ArrayList<Summary>) in.readObject();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        Controller controller = loaderMain.getController();
        controller.setSummaryList();
        stage.setScene(root);
    }
}
