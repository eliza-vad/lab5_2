package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.main.MainController;

public class MainFxApp extends Application {

    @Override
    public void start(Stage stage) {
        AppBootstrap bootstrap = new AppBootstrap();
        MainController controller = bootstrap.createMainController(stage);

        Scene scene = new Scene(controller.getView().getRoot(), 1400, 700);
        stage.setTitle("Система управления отчетами");
        stage.setScene(scene);
        stage.show();

        controller.onStart();
    }

    public static void main(String[] args) {
        launch(args);
    }
}