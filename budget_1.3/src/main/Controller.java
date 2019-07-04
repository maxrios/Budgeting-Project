package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.InnerShadow;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Controller {

    @FXML private Button exitButton;
    @FXML private ListView<String> summaryList;
    @FXML private ListView<String> entryList;
    @FXML private Button entryAddButton;

    @FXML
    private void exitButtonAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();

        stage.close();
    }

    @FXML
    private void summaryAddButtonAction() {
        Main.buildSummary();
        setSummaryList();
    }

    @FXML
    private void summaryRemoveButtonAction() {
        int summaryIndex = summaryList.getSelectionModel().getSelectedIndex();
        if (summaryIndex < 0)
            return;
        else {
            Main.removeSummary(summaryIndex);
            setSummaryList();
            entryList.getItems().clear();
        }
    }

    @FXML
    private void summaryListClickedAction() {
        int summaryIndex = summaryList.getSelectionModel().getSelectedIndex();
        if(summaryIndex < 0)
            return;
        else
            setEntryList(Main.getActiveSummaries().get(summaryIndex));
    }

    @FXML
    private void entryAddButtonAction() {
        int summaryIndex = summaryList.getSelectionModel().getSelectedIndex();
        if(summaryIndex < 0)
            return;
        else {
            Main.buildEntry(summaryIndex);
            setEntryList(Main.getActiveSummaries().get(summaryIndex));
        }
    }

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
                Main.removeEntry(summaryList.getSelectionModel().getSelectedIndex(), entryListIndex);
                setEntryList(Main.getActiveSummaries().get(summaryList.getSelectionModel().getSelectedIndex()));
            }
        }
    }

    void setEntryList(Summary summary) {
        ObservableList<String> items = FXCollections.observableArrayList(summary.getEntries());
        entryList.getItems().clear();
        entryList.setItems(items);
    }

    void setSummaryList() {
        ArrayList<String> activeSummaryNames = new ArrayList<>();
        for(Summary s : Main.getActiveSummaries())
            activeSummaryNames.add(s.getName());
        ObservableList<String> items = FXCollections.observableArrayList(activeSummaryNames);
        summaryList.getItems().clear();
        summaryList.setItems(items);
    }
}
