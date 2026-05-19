package ui.login;

import app.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginWindow {

    public static void display(AuthService authService, Runnable onSuccess) {
        Stage stage = new Stage();
        stage.setTitle("Авторизация");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);

        TextField loginField = new TextField();
        loginField.setPromptText("Логин");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Пароль");

        Button loginBtn = new Button("Войти");
        Button regBtn = new Button("Регистрация");
        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        loginBtn.setOnAction(e -> {
            try {
                if (authService.login(loginField.getText(), passField.getText())) {
                    stage.close();
                    onSuccess.run();
                } else {
                    messageLabel.setText("Неверный логин или пароль");
                }
            } catch (Exception ex) {
                messageLabel.setText(ex.getMessage());
            }
        });

        regBtn.setOnAction(e -> {
            try {
                if (authService.register(loginField.getText(), passField.getText())) {
                    messageLabel.setTextFill(Color.GREEN);
                    messageLabel.setText("Успешно! Теперь нажмите 'Войти'.");
                } else {
                    messageLabel.setTextFill(Color.RED);
                    messageLabel.setText("Ошибка: логин занят.");
                }
            } catch (Exception ex) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText(ex.getMessage());
            }
        });

        grid.add(new Label("Логин:"), 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(passField, 1, 1);
        grid.add(loginBtn, 0, 2);
        grid.add(regBtn, 1, 2);
        grid.add(messageLabel, 0, 3, 2, 1);

        stage.setScene(new Scene(grid, 350, 200));
        stage.show();
    }
}