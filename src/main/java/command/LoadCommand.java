package command;

import domain.Report;
import service.ReportService;
import storage.FileStorage;
import storage.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LoadCommand implements Command {
    private final FileStorage storage;
    private final ReportService reportService;

    public LoadCommand(FileStorage storage, ReportService reportService) {
        this.storage = storage;
        this.reportService = reportService;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 1) {
            System.err.println(" Укажите путь к файлу: load <path>");
            System.err.println("   Пример: load reports.xml");
            return;
        }

        String path = args[0];

        try {
            List<Report> loaded = storage.load(path);
            reportService.replaceAll(loaded);
            System.out.println(" Загружено " + loaded.size() + " отчетов из файла: " + path);
        } catch (IOException e) {
            System.err.println(" Ошибка чтения файла: " + e.getMessage());
            System.err.println("   Проверьте, что файл существует и доступен для чтения");
        } catch (ValidationException e) {
            System.err.println(" Ошибка валидации данных: " + e.getMessage());
            System.err.println(" Данные в памяти НЕ были изменены");
        }
    }

    @Override
    public String getDescription() {
        return "Загрузить отчеты из XML файла";
    }

    @Override
    public String getUsage() {
        return "load <путь_к_файлу>";
    }
}