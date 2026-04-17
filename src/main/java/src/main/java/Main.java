package src.main.java;

import command.Command;
import command.CommandRegistry;
import command.ExitCommand;
import domain.Report;
import repository.InMemoryReportLineRepository;
import repository.InMemoryReportRepository;
import repository.ReportLineRepository;
import repository.ReportRepository;
import service.ReportService;
import service.ReportServiceImpl;
import storage.FileStorage;
import storage.ValidationException;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ReportRepository reportRepo = new InMemoryReportRepository();
        ReportLineRepository lineRepo = new InMemoryReportLineRepository();
        ReportService reportService = new ReportServiceImpl(reportRepo, lineRepo);
        FileStorage fileStorage = new FileStorage();

        if (args.length > 0) {
            loadInitialData(args[0], fileStorage, reportService);
        }

        CommandRegistry registry = new CommandRegistry(reportService, fileStorage);
        runMainLoop(registry);
    }

    private static void loadInitialData(String filePath, FileStorage fileStorage, ReportService reportService) {
        try {
            List<Report> reports = fileStorage.load(filePath);
            reportService.replaceAll(reports);
            System.out.println("✅ Загружены данные из файла: " + filePath);
            System.out.println("   Загружено отчетов: " + reports.size());
        } catch (Exception e) {
            System.err.println("⚠️ Не удалось загрузить файл: " + e.getMessage());
            System.err.println("   Продолжаем с пустой коллекцией");
        }
    }

    private static void runMainLoop(CommandRegistry registry) {
        try (Scanner scanner = new Scanner(System.in)) {
            printWelcomeMessage();

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    continue;
                }

                String[] parts = input.split("\\s+");
                String commandName = parts[0].toLowerCase();

                Command command = registry.getCommand(commandName);

                if (command == null) {
                    System.err.println("❌ Ошибка: неизвестная команда. Введите 'help' для списка команд.");
                    continue;
                }

                if (command instanceof ExitCommand) {
                    System.out.println("👋 До свидания!");
                    break;
                }

                executeCommand(command, parts, scanner);
            }
        }
    }

    private static void executeCommand(Command command, String[] parts, Scanner scanner) {
        try {
            command.execute(parts, scanner);
        } catch (ValidationException e) {
            handleValidationException(e);
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Ошибка формата ввода: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Произошла системная ошибка: " + e.getMessage());
        }
    }

    private static void handleValidationException(ValidationException e) {
        String code = e.getErrorCode();
        String msg = e.getUserMessage();
        String field = e.getFieldName();

        if (code != null && !"GENERAL_ERROR".equals(code) && field != null && !field.isBlank()) {
            System.err.println("❌ Ошибка валидации в поле [" + field + "]: " + msg + " (Код: " + code + ")");
        } else {
            System.err.println("❌ " + msg);
        }
    }

    private static void printWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║         СИСТЕМА УПРАВЛЕНИЯ ОТЧЕТАМИ (DOMAIN 6)               ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  Введите 'help' для списка команд                            ║");
        System.out.println("║  Команды save/load доступны для работы с XML файлами         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}