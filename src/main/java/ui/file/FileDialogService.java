package ui.file;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class FileDialogService {

    private final Stage stage;

    public FileDialogService(Stage stage) {
        this.stage = stage;
    }

    public Optional<File> chooseSaveXmlFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранить XML");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        chooser.setInitialFileName("reports.xml");
        return Optional.ofNullable(chooser.showSaveDialog(stage));
    }

    public Optional<File> chooseOpenXmlFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Открыть XML");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        return Optional.ofNullable(chooser.showOpenDialog(stage));
    }
}