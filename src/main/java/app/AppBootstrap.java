package app;

import app.cli.CliApplication;
import app.cli.command.*;
import app.repository.InMemoryReportLineRepository;
import app.repository.InMemoryReportRepository;
import app.repository.ReportLineRepository;
import app.repository.ReportRepository;
import app.repository.UserRepository;
import app.repository.jdbc.DatabaseConnection;
import app.repository.jdbc.ReportLineRepositoryJdbcImpl;
import app.repository.jdbc.ReportRepositoryJdbcImpl;
import app.repository.jdbc.UserRepositoryJdbcImpl;
import app.service.AuthService;
import app.service.AuthServiceImpl;
import app.service.ReportService;
import app.service.ReportServiceImpl;
import app.storage.FileStorage;

import javafx.stage.Stage;
import ui.dialog.DialogService;
import ui.dialog.JavaFxDialogService;
import ui.file.FileDialogService;
import ui.main.MainActionHandler;
import ui.main.MainController;
import ui.main.MainUiState;
import ui.main.MainView;
import ui.task.FxTaskRunner;

import java.util.Scanner;

public class AppBootstrap {

    private final AuthService authService;

    public AppBootstrap() {
        UserRepository userRepository = new UserRepositoryJdbcImpl();
        this.authService = new AuthServiceImpl(userRepository);
    }
    public MainController createMainController(Stage stage) {
        ReportRepository reportRepository = new ReportRepositoryJdbcImpl();
        ReportLineRepository reportLineRepository = new ReportLineRepositoryJdbcImpl();

        ReportService reportService = new ReportServiceImpl(reportRepository, reportLineRepository);

        FileStorage fileStorage = new FileStorage();

        DialogService dialogService = new JavaFxDialogService();
        FxTaskRunner taskRunner = new FxTaskRunner(dialogService);

        MainUiState uiState = new MainUiState();
        MainView view = new MainView(uiState);

        MainActionHandler actionHandler = new MainActionHandler(
                reportService, dialogService, taskRunner, uiState, this.authService
        );

        return new MainController(view, actionHandler);
    }

    public void runCli() {
        try {
            try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
                System.out.println("Подключение к базе данных установлено успешно.");
            }

            ReportRepository reportRepository = new ReportRepositoryJdbcImpl();
            app.repository.ReportLineRepository reportLineRepository = new ReportLineRepositoryJdbcImpl();
            ReportService reportService = new ReportServiceImpl(reportRepository, reportLineRepository);

            Scanner scanner = new Scanner(System.in);
            CliApplication cliApp = new CliApplication(this.authService, reportService, scanner);

            cliApp.registerCommand(new HelpCommand());
            cliApp.registerCommand(new InfoCommand());
            cliApp.registerCommand(new ShowCommand());
            cliApp.registerCommand(new RegisterCommand());
            cliApp.registerCommand(new LoginCommand());
            cliApp.registerCommand(new AddCommand());
            cliApp.registerCommand(new RemoveCommand());
            cliApp.registerCommand(new UpdateCommand());
            cliApp.registerCommand(new ClearCommand());

            cliApp.run();

        } catch (java.sql.SQLException e) {
            System.err.println("==================================================");
            System.err.println("КРИТИЧЕСКАЯ ОШИБКА: База данных PostgreSQL недоступна!");
            System.err.println("Проверьте, что сервер запущен и данные в application.properties верны.");
            System.err.println("Детали ошибки: " + e.getMessage());
            System.err.println("==================================================");
            System.exit(1);
        }
    }
    public AuthService getAuthService() {
        return this.authService;
    }
}