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
import app.domain.Report;

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

        this.reportListView.setStyle(
                "-fx-selection-bar: #D3E3FD; " +
                        "-fx-selection-bar-non-focused: #E8F0FE; " +
                        "-fx-selection-bar-text: #000000; " +
                        "-fx-background-color: transparent;"
        );

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

        topBar.setStyle(
                "-fx-background-color: #0ABAB5; " +
                        "-fx-border-color: #FFA0C0; " +
                        "-fx-border-width: 6px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        styleButtons(
                refreshButton, createButton, editButton, deleteButton, detailsButton,
                finalizeButton, signButton, showLinesButton, addLineButton,
                editLineButton, deleteLineButton, saveButton, loadButton
        );

        StackPane centerPane = new StackPane(reportListView, progressIndicator);
        centerPane.setPadding(new Insets(10));

        centerPane.setStyle(
                "-fx-background-color: #0ABAB5; " +
                        "-fx-border-color: #FFA0C0; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-padding: 5px;"
        );

        root.setStyle("-fx-background-color: #F4F4F4;");

        root.setPadding(new Insets(10));

        root.setTop(topBar);
        root.setCenter(centerPane);

        BorderPane.setMargin(topBar, new Insets(0, 0, 10, 0));
    }

    private void styleButtons(Button... buttons) {
        String buttonStyle =
                "-fx-background-color: #FFFFFF; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-border-color: #DDDDDD; " +
                        "-fx-border-radius: 3px; " +
                        "-fx-background-radius: 3px; " +
                        "-fx-cursor: hand;";

        for (Button btn : buttons) {
            btn.setStyle(buttonStyle);
            btn.setOnMouseEntered(e -> btn.setStyle(buttonStyle.replace("#FFFFFF", "#F0F0F0")));
            btn.setOnMouseExited(e -> btn.setStyle(buttonStyle));
        }
    }

    public BorderPane getRoot() { return root; }
    public Button getRefreshButton() { return refreshButton; }
    public Button getCreateButton() { return createButton; }
    public Button getEditButton() { return editButton; }
    public Button getDeleteButton() { return deleteButton; }
    public Button getDetailsButton() { return detailsButton; }
    public Button getFinalizeButton() { return finalizeButton; }
    public Button getSignButton() { return signButton; }
    public Button getShowLinesButton() { return showLinesButton; }
    public Button getAddLineButton() { return addLineButton; }
    public Button getEditLineButton() { return editLineButton; }
    public Button getDeleteLineButton() { return deleteLineButton; }
    public Button getSaveButton() { return saveButton; }
    public Button getLoadButton() { return loadButton; }
    public ListView<Report> getReportListView() { return reportListView; }
    public ProgressIndicator getProgressIndicator() { return progressIndicator; }
}