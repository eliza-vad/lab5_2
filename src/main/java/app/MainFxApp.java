package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.main.MainController;

public class MainFxApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        app.AppBootstrap bootstrap = new app.AppBootstrap();
        app.service.AuthService authService = bootstrap.getAuthService();

        ui.login.LoginWindow.display(authService, () -> {
            ui.main.MainController controller = bootstrap.createMainController(primaryStage);

            Scene scene = new Scene(controller.getView().getRoot(), 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Система отчетов (БД PostgreSQL)");
            primaryStage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}