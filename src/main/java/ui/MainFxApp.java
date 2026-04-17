package ui;

import domain.MeasurementParam;
import domain.Report;
import domain.ReportLine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import repository.InMemoryReportLineRepository;
import repository.InMemoryReportRepository;
import repository.ReportLineRepository;
import repository.ReportRepository;
import service.ReportService;
import service.ReportServiceImpl;
import storage.FileStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MainFxApp extends Application {

    private ReportService reportService;
    private FileStorage fileStorage;

    private final ObservableList<Report> reportItems = FXCollections.observableArrayList();

    private ListView<Report> reportListView;
    private ProgressIndicator progressIndicator;

    @Override
    public void start(Stage stage) {
        initServices();

        reportListView = new ListView<>(reportItems);
        reportListView.setCellFactory(list -> new ReportCardCell());

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(60, 60);

        Button refreshButton = new Button("Refresh");
        Button createButton = new Button("Create sample");
        Button editButton = new Button("Edit name");
        Button deleteButton = new Button("Delete");
        Button detailsButton = new Button("Details");

        Button finalizeButton = new Button("Finalize");
        Button signButton = new Button("Sign");

        Button showLinesButton = new Button("Show lines");
        Button addLineButton = new Button("Add line");
        Button editLineButton = new Button("Edit line");
        Button deleteLineButton = new Button("Delete line");

        Button saveButton = new Button("Save XML");
        Button loadButton = new Button("Load XML");

        refreshButton.setOnAction(e -> refreshReports());
        createButton.setOnAction(e -> createSampleReport());
        editButton.setOnAction(e -> editSelectedReport());
        deleteButton.setOnAction(e -> deleteSelectedReport());
        detailsButton.setOnAction(e -> showDetails());

        finalizeButton.setOnAction(e -> finalizeSelectedReport());
        signButton.setOnAction(e -> signSelectedReport());

        showLinesButton.setOnAction(e -> showReportLines());
        addLineButton.setOnAction(e -> addReportLine());
        editLineButton.setOnAction(e -> editReportLine());
        deleteLineButton.setOnAction(e -> deleteReportLine());

        saveButton.setOnAction(e -> saveToXml(stage));
        loadButton.setOnAction(e -> loadFromXml(stage));

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

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 1400, 700);

        stage.setTitle("Система управления отчетами");
        stage.setScene(scene);
        stage.show();

        refreshReports();
    }

    private void initServices() {
        ReportRepository reportRepo = new InMemoryReportRepository();
        ReportLineRepository lineRepo = new InMemoryReportLineRepository();
        reportService = new ReportServiceImpl(reportRepo, lineRepo);
        fileStorage = new FileStorage();
    }

    private void refreshReports() {
        Task<List<Report>> task = new Task<>() {
            @Override
            protected List<Report> call() {
                return new ArrayList<>(reportService.getAllReports());
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event -> {
            reportItems.setAll(task.getValue());
            reportListView.refresh();
        });

        task.setOnFailed(event ->
                showError("Не удалось обновить список отчетов: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private void createSampleReport() {
        TextInputDialog nameDialog = new TextInputDialog("Тестовый отчет");
        nameDialog.setTitle("Создание отчета");
        nameDialog.setHeaderText("Создать тестовый отчет");
        nameDialog.setContentText("Введите название отчета:");

        Optional<String> nameResult = nameDialog.showAndWait();
        if (nameResult.isEmpty() || nameResult.get().isBlank()) {
            return;
        }

        TextInputDialog userDialog = new TextInputDialog("user");
        userDialog.setTitle("Создание отчета");
        userDialog.setHeaderText("Создать тестовый отчет");
        userDialog.setContentText("Введите имя пользователя:");

        Optional<String> userResult = userDialog.showAndWait();
        if (userResult.isEmpty() || userResult.get().isBlank()) {
            return;
        }

        String reportName = nameResult.get().trim();
        String username = userResult.get().trim();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.createSampleReport(UUID.randomUUID(), reportName, username);
                return null;
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event ->
                showInfo("Отчет создан. Нажми Refresh, чтобы увидеть изменения.")
        );

        task.setOnFailed(event ->
                showError("Не удалось создать отчет: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private void editSelectedReport() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog(safe(getName(selected)));
        dialog.setTitle("Редактирование отчета");
        dialog.setHeaderText("Изменить название отчета");
        dialog.setContentText("Новое название:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().isBlank()) {
            return;
        }

        String newName = result.get().trim();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.updateReportName(selected.getId(), newName);
                return null;
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event ->
                showInfo("Название изменено. Нажми Refresh, чтобы обновить список.")
        );

        task.setOnFailed(event ->
                showError("Не удалось изменить отчет: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private void deleteSelectedReport() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText("Удаление отчета");
        confirm.setContentText("Удалить выбранный отчет?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.deleteReport(selected.getId());
                return null;
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event ->
                showInfo("Отчет удален. Нажми Refresh, чтобы обновить список.")
        );

        task.setOnFailed(event ->
                showError("Не удалось удалить отчет: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private void showDetails() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        StringBuilder text = new StringBuilder();
        text.append("ID: ").append(selected.getId()).append("\n");
        text.append("Название: ").append(safe(getName(selected))).append("\n");
        text.append("Статус: ").append(String.valueOf(selected.getStatus())).append("\n");
        text.append("Создан: ").append(String.valueOf(selected.getCreatedAt())).append("\n");
        text.append("Обновлен: ").append(String.valueOf(selected.getUpdatedAt())).append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Подробная информация");
        alert.setHeaderText("Данные отчета");
        alert.setContentText(text.toString());
        alert.showAndWait();
    }

    private void finalizeSelectedReport() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.finalizeReport(selected.getId());
                return null;
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event ->
                showInfo("Отчет финализирован. Нажми Refresh, чтобы обновить список.")
        );

        task.setOnFailed(event ->
                showError("Не удалось финализировать отчет: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private void signSelectedReport() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog("user");
        dialog.setTitle("Подписание отчета");
        dialog.setHeaderText("Подписать отчет");
        dialog.setContentText("Введите имя пользователя:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().isBlank()) {
            return;
        }

        String username = result.get().trim();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.signReport(selected.getId(), username);
                return null;
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event ->
                showInfo("Отчет подписан. Нажми Refresh, чтобы обновить список.")
        );

        task.setOnFailed(event ->
                showError("Не удалось подписать отчет: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private void showReportLines() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        try {
            Set<ReportLine> lines = reportService.getLinesByReportId(selected.getId());

            StringBuilder text = new StringBuilder();
            if (lines.isEmpty()) {
                text.append("У отчета нет строк.");
            } else {
                int i = 1;
                for (ReportLine line : lines) {
                    text.append(i++)
                            .append(") ID=").append(line.getId())
                            .append(", PARAM=").append(line.getParam())
                            .append(", VALUE=").append(line.getValue())
                            .append(", UNIT=").append(line.getUnit())
                            .append("\n");
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Строки отчета");
            alert.setHeaderText("Строки выбранного отчета");
            alert.setContentText(text.toString());
            alert.getDialogPane().setMinHeight(400);
            alert.getDialogPane().setMinWidth(700);
            alert.showAndWait();

        } catch (Exception e) {
            showError("Не удалось показать строки отчета: " + safeMessage(e));
        }
    }

    private void addReportLine() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        MeasurementParam[] params = MeasurementParam.values();
        if (params.length == 0) {
            showError("Справочник MeasurementParam пуст.");
            return;
        }

        ChoiceDialog<MeasurementParam> paramDialog =
                new ChoiceDialog<>(params[0], Arrays.asList(params));
        paramDialog.setTitle("Добавление строки");
        paramDialog.setHeaderText("Выбери параметр");
        paramDialog.setContentText("Параметр:");

        Optional<MeasurementParam> paramResult = paramDialog.showAndWait();
        if (paramResult.isEmpty()) {
            return;
        }

        TextInputDialog valueDialog = new TextInputDialog("0");
        valueDialog.setTitle("Добавление строки");
        valueDialog.setHeaderText("Введите значение");
        valueDialog.setContentText("Value:");

        Optional<String> valueResult = valueDialog.showAndWait();
        if (valueResult.isEmpty() || valueResult.get().isBlank()) {
            return;
        }

        double value;
        try {
            value = Double.parseDouble(valueResult.get().trim());
        } catch (NumberFormatException e) {
            showError("Значение должно быть числом.");
            return;
        }

        TextInputDialog unitDialog = new TextInputDialog("unit");
        unitDialog.setTitle("Добавление строки");
        unitDialog.setHeaderText("Введите единицу измерения");
        unitDialog.setContentText("Unit:");

        Optional<String> unitResult = unitDialog.showAndWait();
        if (unitResult.isEmpty() || unitResult.get().isBlank()) {
            return;
        }

        String unit = unitResult.get().trim();
        MeasurementParam param = paramResult.get();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.addReportLine(selected.getId(), param, value, unit);
                return null;
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event ->
                showInfo("Строка добавлена.")
        );

        task.setOnFailed(event ->
                showError("Не удалось добавить строку: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private void editReportLine() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        try {
            Set<ReportLine> lines = reportService.getLinesByReportId(selected.getId());
            if (lines.isEmpty()) {
                showError("У выбранного отчета нет строк.");
                return;
            }

            List<ReportLine> lineList = new ArrayList<>(lines);

            ChoiceDialog<ReportLine> lineDialog = new ChoiceDialog<>(lineList.get(0), lineList);
            lineDialog.setTitle("Редактирование строки");
            lineDialog.setHeaderText("Выбери строку");
            lineDialog.setContentText("Строка:");

            Optional<ReportLine> lineResult = lineDialog.showAndWait();
            if (lineResult.isEmpty()) {
                return;
            }

            ReportLine selectedLine = lineResult.get();

            MeasurementParam[] params = MeasurementParam.values();
            if (params.length == 0) {
                showError("Справочник MeasurementParam пуст.");
                return;
            }

            ChoiceDialog<MeasurementParam> paramDialog =
                    new ChoiceDialog<>(selectedLine.getParam(), Arrays.asList(params));
            paramDialog.setTitle("Редактирование строки");
            paramDialog.setHeaderText("Выбери новый параметр");
            paramDialog.setContentText("Параметр:");

            Optional<MeasurementParam> paramResult = paramDialog.showAndWait();
            if (paramResult.isEmpty()) {
                return;
            }

            TextInputDialog valueDialog = new TextInputDialog(String.valueOf(selectedLine.getValue()));
            valueDialog.setTitle("Редактирование строки");
            valueDialog.setHeaderText("Введите новое значение");
            valueDialog.setContentText("Value:");

            Optional<String> valueResult = valueDialog.showAndWait();
            if (valueResult.isEmpty() || valueResult.get().isBlank()) {
                return;
            }

            double newValue;
            try {
                newValue = Double.parseDouble(valueResult.get().trim());
            } catch (NumberFormatException e) {
                showError("Значение должно быть числом.");
                return;
            }

            TextInputDialog unitDialog = new TextInputDialog(selectedLine.getUnit());
            unitDialog.setTitle("Редактирование строки");
            unitDialog.setHeaderText("Введите новую единицу измерения");
            unitDialog.setContentText("Unit:");

            Optional<String> unitResult = unitDialog.showAndWait();
            if (unitResult.isEmpty() || unitResult.get().isBlank()) {
                return;
            }

            String newUnit = unitResult.get().trim();
            MeasurementParam newParam = paramResult.get();

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    reportService.updateReportLine(selectedLine.getId(), newParam, newValue, newUnit);
                    return null;
                }
            };

            bindProgress(task);

            task.setOnSucceeded(event ->
                    showInfo("Строка изменена.")
            );

            task.setOnFailed(event ->
                    showError("Не удалось изменить строку: " + safeMessage(task.getException()))
            );

            startTask(task);

        } catch (Exception e) {
            showError("Не удалось открыть редактирование строки: " + safeMessage(e));
        }
    }

    private void deleteReportLine() {
        Report selected = getSelectedReportOrShowError();
        if (selected == null) {
            return;
        }

        try {
            Set<ReportLine> lines = reportService.getLinesByReportId(selected.getId());
            if (lines.isEmpty()) {
                showError("У выбранного отчета нет строк.");
                return;
            }

            List<ReportLine> lineList = new ArrayList<>(lines);

            ChoiceDialog<ReportLine> lineDialog = new ChoiceDialog<>(lineList.get(0), lineList);
            lineDialog.setTitle("Удаление строки");
            lineDialog.setHeaderText("Выбери строку для удаления");
            lineDialog.setContentText("Строка:");

            Optional<ReportLine> lineResult = lineDialog.showAndWait();
            if (lineResult.isEmpty()) {
                return;
            }

            ReportLine selectedLine = lineResult.get();

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Подтверждение");
            confirm.setHeaderText("Удаление строки отчета");
            confirm.setContentText("Удалить выбранную строку?");

            Optional<ButtonType> confirmResult = confirm.showAndWait();
            if (confirmResult.isEmpty() || confirmResult.get() != ButtonType.OK) {
                return;
            }

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    reportService.deleteReportLine(selectedLine.getId());
                    return null;
                }
            };

            bindProgress(task);

            task.setOnSucceeded(event ->
                    showInfo("Строка удалена.")
            );

            task.setOnFailed(event ->
                    showError("Не удалось удалить строку: " + safeMessage(task.getException()))
            );

            startTask(task);

        } catch (Exception e) {
            showError("Не удалось удалить строку: " + safeMessage(e));
        }
    }

    private void saveToXml(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранить XML");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        chooser.setInitialFileName("reports.xml");

        File file = chooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                List<Report> reports = new ArrayList<>(reportService.getAllReports());
                fileStorage.save(file.getAbsolutePath(), reports);
                return null;
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event ->
                showInfo("XML сохранен: " + file.getName())
        );

        task.setOnFailed(event ->
                showError("Не удалось сохранить XML: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private void loadFromXml(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Открыть XML");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));

        File file = chooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                List<Report> reports = fileStorage.load(file.getAbsolutePath());
                reportService.replaceAll(reports);
                return null;
            }
        };

        bindProgress(task);

        task.setOnSucceeded(event ->
                showInfo("XML загружен: " + file.getName() + ". Нажми Refresh, чтобы обновить список.")
        );

        task.setOnFailed(event ->
                showError("Не удалось загрузить XML: " + safeMessage(task.getException()))
        );

        startTask(task);
    }

    private Report getSelectedReportOrShowError() {
        Report selected = reportListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Сначала выбери отчет.");
            return null;
        }
        return selected;
    }

    private void bindProgress(Task<?> task) {
        progressIndicator.visibleProperty().unbind();
        progressIndicator.visibleProperty().bind(task.runningProperty());
    }

    private void startTask(Task<?> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Операция не выполнена");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private String getName(Report report) {
        try {
            return report.getName();
        } catch (Exception e) {
            return "(нет имени)";
        }
    }

    private String safe(String text) {
        return text == null || text.isBlank() ? "(нет значения)" : text;
    }

    private String safeMessage(Throwable e) {
        if (e == null || e.getMessage() == null || e.getMessage().isBlank()) {
            return "Неизвестная ошибка";
        }
        return e.getMessage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}