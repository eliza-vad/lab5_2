package app;

import javafx.stage.Stage;
import ui.main.MainUiState;
import repository.InMemoryReportLineRepository;
import repository.InMemoryReportRepository;
import repository.ReportLineRepository;
import repository.ReportRepository;
import service.ReportService;
import service.ReportServiceImpl;
import storage.FileStorage;
import ui.dialog.DialogService;
import ui.dialog.JavaFxDialogService;
import ui.file.FileDialogService;
import ui.main.MainActionHandler;
import ui.main.MainController;
import ui.main.MainUiState;
import ui.main.MainView;
import ui.task.FxTaskRunner;

public class AppBootstrap {

    public MainController createMainController(Stage stage) {
        ReportRepository reportRepository = new InMemoryReportRepository();
        ReportLineRepository reportLineRepository = new InMemoryReportLineRepository();

        ReportService reportService = new ReportServiceImpl(reportRepository, reportLineRepository);
        FileStorage fileStorage = new FileStorage();

        DialogService dialogService = new JavaFxDialogService();
        FileDialogService fileDialogService = new FileDialogService(stage);
        FxTaskRunner taskRunner = new FxTaskRunner(dialogService);

        MainUiState uiState = new MainUiState();
        MainView view = new MainView(uiState);
        MainActionHandler actionHandler =
                new MainActionHandler(reportService, fileStorage, dialogService, fileDialogService, taskRunner, uiState);

        return new MainController(view, actionHandler);
    }
}