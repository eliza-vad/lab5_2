package ui.main;

import javafx.geometry.Insets;
import ui.cell.ReportCardCell;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import domain.Report;

public class MainView {

    private final BorderPane root = new BorderPane();

    private final Button refreshButton = new Button("Refresh");
    private final Button createButton = new Button("Create sample");
    private final Button editButton = new Button("Edit name");
    private final Button deleteButton = new Button("Delete");
    private final Button detailsButton = new Button("Details");

    private final Button finalizeButton = new Button("Finalize");
    private final Button signButton = new Button("Sign");

    private final Button showLinesButton = new Button("Show lines");
    private final Button addLineButton = new Button("Add line");
    private final Button editLineButton = new Button("Edit line");
    private final Button deleteLineButton = new Button("Delete line");

    private final Button saveButton = new Button("Save XML");
    private final Button loadButton = new Button("Load XML");

    private final ListView<Report> reportListView;
    private final ProgressIndicator progressIndicator = new ProgressIndicator();

    public MainView(MainUiState uiState) {
        this.reportListView = new ListView<>(uiState.getReports());
        this.reportListView.setCellFactory(list -> new ReportCardCell());

        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(60, 60);

        HBox topBar = new HBox(
                8,
                refreshButton, createButton, editButton, deleteButton, detailsButton,
                new Separator(Orientation.VERTICAL),
                finalizeButton, signButton,
                new Separator(Orientation.VERTICAL),
                showLinesButton, addLineButton, editLineButton, deleteLineButton,
                new Separator(Orientation.VERTICAL),
                saveButton, loadButton
        );
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        StackPane centerPane = new StackPane(reportListView, progressIndicator);
        centerPane.setPadding(new Insets(10));

        root.setTop(topBar);
        root.setCenter(centerPane);
    }

    public BorderPane getRoot() {
        return root;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getDetailsButton() {
        return detailsButton;
    }

    public Button getFinalizeButton() {
        return finalizeButton;
    }

    public Button getSignButton() {
        return signButton;
    }

    public Button getShowLinesButton() {
        return showLinesButton;
    }

    public Button getAddLineButton() {
        return addLineButton;
    }

    public Button getEditLineButton() {
        return editLineButton;
    }

    public Button getDeleteLineButton() {
        return deleteLineButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getLoadButton() {
        return loadButton;
    }

    public ListView<Report> getReportListView() {
        return reportListView;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }
}