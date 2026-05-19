package app.command;

import app.domain.Report;
import app.service.ReportService;
import app.storage.FileStorage;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SaveCommand implements Command {
    private final FileStorage fileStorage;
    private final ReportService reportService;

    public SaveCommand(FileStorage fileStorage, ReportService reportService) {
        this.fileStorage = fileStorage;
        this.reportService = reportService;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws IOException {
        String filePath = resolveFilePath(args, scanner);

        if (!filePath.toLowerCase().endsWith(".xml")) {
            filePath += ".xml";
        }

        Set<Report> reportSet = reportService.getAllReports();
        List<Report> reports = new ArrayList<>(reportSet);

        fileStorage.save(filePath, reports);

        File savedFile = new File(filePath);

        if (!savedFile.exists() || !savedFile.isFile()) {
            System.out.println(" Файл не был создан: " + filePath);
            return;
        }

        boolean isXml = isValidXml(savedFile);

        System.out.println(" Данные успешно сохранены.");
        System.out.println("   Имя файла: " + savedFile.getName());
        System.out.println("   Полный путь: " + savedFile.getAbsolutePath());
        System.out.println("   Размер: " + savedFile.length() + " байт");
        System.out.println("   Сохранено отчетов: " + reports.size());

        if (isXml) {
            System.out.println("   Проверка формата: файл является корректным XML");
        } else {
            System.out.println("   Проверка формата: файл НЕ является корректным XML");
        }
    }

    private String resolveFilePath(String[] args, Scanner scanner) {
        if (args != null && args.length > 1 && !args[1].isBlank()) {
            return args[1].trim();
        }

        System.out.print("Введите имя файла для сохранения: ");
        return scanner.nextLine().trim();
    }

    private boolean isValidXml(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(file);

            return true;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Сохранить отчеты в XML-файл";
    }

    @Override
    public String getUsage() {
        return "save <имя-файла.xml>";
    }
}