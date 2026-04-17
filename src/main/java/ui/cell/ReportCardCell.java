package ui.cell;

import domain.Report;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class ReportCardCell extends ListCell<Report> {

    @Override
    protected void updateItem(Report report, boolean empty) {
        super.updateItem(report, empty);

        if (empty || report == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        Label title = new Label("Отчет: " + safe(getName(report)));
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label id = new Label("ID: " + report.getId());
        Label status = new Label("Статус: " + String.valueOf(report.getStatus()));

        VBox card = new VBox(6, title, id, status);
        card.setPadding(new Insets(12));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );

        setText(null);
        setGraphic(card);
    }

    private String getName(Report report) {
        try {
            return report.getName();
        } catch (Exception e) {
            return "(нет имени)";
        }
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "(нет значения)" : value;
    }
}