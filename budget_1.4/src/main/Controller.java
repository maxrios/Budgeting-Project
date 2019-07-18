package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * @author Maximiliano Merced Rios
 */
public class Controller {

    @FXML private Button exitButton;
    @FXML private ListView<String> summaryList;
    @FXML private ListView<String> entryList;

    /**
     * Closes main window.
     */
    @FXML
    private void exitButtonAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();

        stage.close();
    }

    /**
     * Adds summary
     */
    @FXML
    private void summaryAddButtonAction() {
        Driver.buildSummary();
        setSummaryList();
    }

    /**
     * Removes summary
     */
    @FXML
    private void summaryRemoveButtonAction() {
        int summaryIndex = summaryList.getSelectionModel().getSelectedIndex();
        if (summaryIndex < 0)
            return;
        else {
            Driver.removeSummary(summaryIndex);
            setSummaryList();
            entryList.getItems().clear();
        }
    }

    /**
     * Checks which summary is selected
     */
    @FXML
    private void summaryListClickedAction() {
        int summaryIndex = summaryList.getSelectionModel().getSelectedIndex();
        if(summaryIndex < 0)
            return;
        else
            setEntryList(Driver.getActiveSummaries().get(summaryIndex));
    }

    /**
     * Adds entry
     */
    @FXML
    private void entryAddButtonAction() {
        int summaryIndex = summaryList.getSelectionModel().getSelectedIndex();
        if(summaryIndex < 0)
            return;
        else {
            Driver.buildEntry(summaryIndex);
            setEntryList(Driver.getActiveSummaries().get(summaryIndex));
        }
    }

    /**
     * Removes entry
     */
    @FXML
    private void entryRemoveButtonAction() {
        int entryIndex = entryList.getSelectionModel().getSelectedIndex();
        if (entryIndex < 0)
            return;
        else {
            int entryListIndex =  entryList.getSelectionModel().getSelectedIndex();
            if(entryListIndex < 0)
                return;
            else {
                Driver.removeEntry(summaryList.getSelectionModel().getSelectedIndex(), entryListIndex);
                setEntryList(Driver.getActiveSummaries().get(summaryList.getSelectionModel().getSelectedIndex()));
            }
        }
    }

    /**
     * Initializes entry list
     * @param summary
     */
    void setEntryList(Summary summary) {
        ObservableList<String> items = FXCollections.observableArrayList(summary.getEntries());
        entryList.getItems().clear();
        entryList.setItems(items);
    }

    /**
     * Initializes summary list
     */
    void setSummaryList() {
        ArrayList<String> activeSummaryNames = new ArrayList<>();
        for(Summary s : Driver.getActiveSummaries())
            activeSummaryNames.add(s.getName());
        ObservableList<String> items = FXCollections.observableArrayList(activeSummaryNames);
        summaryList.getItems().clear();
        summaryList.setItems(items);
    }
}