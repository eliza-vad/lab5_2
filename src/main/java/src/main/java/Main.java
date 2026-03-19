package src.main.java;

import command.*;
import repository.InMemoryReportLineRepository;
import repository.InMemoryReportRepository;
import repository.ReportLineRepository;
import repository.ReportRepository;
import service.ReportService;
import service.ReportServiceImpl;
import validation.ValidationException;

import java.util.Scanner;

public class Main {

    private static final ReportRepository reportRepo = new InMemoryReportRepository();
    private static final ReportLineRepository lineRepo = new InMemoryReportLineRepository();
    private static final ReportService service = new ReportServiceImpl(reportRepo, lineRepo);

    private static final CommandRegistry registry = new CommandRegistry(service);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Система управления отчетами (Domain 6). Введите help для списка команд.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ");
            String commandName = parts[0];

            try {
                Command command = registry.getCommand(commandName);
                if (command == null) {
                    System.out.println("Ошибка: неизвестная команда. Введите help для списка команд.");
                    continue;
                }

                command.execute(parts, scanner);

            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка формата ввода: проверьте правильность введенных данных (ID или параметров).");
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }
}