package ui.main;

import app.domain.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainUiState {

    private final ObservableList<Report> reports = FXCollections.observableArrayList();

    public ObservableList<Report> getReports() {
        return reports;
    }
}