package ui.task;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import ui.dialog.DialogService;

public class FxTaskRunner {

    private final DialogService dialogService;

    public FxTaskRunner(DialogService dialogService) {
        this.dialogService = dialogService;
    }

    public void run(Task<?> task, ProgressIndicator indicator, Runnable onSuccess) {
        indicator.visibleProperty().unbind();
        indicator.visibleProperty().bind(task.runningProperty());

        task.setOnSucceeded(e -> onSuccess.run());
        task.setOnFailed(e -> dialogService.showError(safeMessage(task.getException())));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private String safeMessage(Throwable e) {
        if (e == null || e.getMessage() == null || e.getMessage().isBlank()) {
            return "Неизвестная ошибка";
        }
        return e.getMessage();
    }
}