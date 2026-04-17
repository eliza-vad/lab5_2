package ui.dialog;

import java.util.List;
import java.util.Optional;

public interface DialogService {
    void showInfo(String message);
    void showError(String message);
    boolean confirm(String title, String header, String message);
    Optional<String> askText(String title, String header, String content, String defaultValue);
    <T> Optional<T> askChoice(String title, String header, String content, List<T> choices, T defaultValue);
}