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

    // вписываем зависимости классов и интерфуйсов
    private static final ReportRepository reportRepo = new InMemoryReportRepository();
    private static final ReportLineRepository lineRepo = new InMemoryReportLineRepository();
    private static final ReportService service = new ReportServiceImpl(reportRepo, lineRepo);

    private static final CommandRegistry registry = new CommandRegistry();

    public static void main(String[] args) {

        registry.register("rep_show", new ShowReportCommand(service));
        registry.register("rep_sign", new SignReportCommand(service));
        registry.register("rep_addline", new AddReportLineCommand(service));
        registry.register("rep_lines", new ShowReportLinesCommand(service));
        registry.register("rep_delline", new DeleteReportLineCommand(service));
        registry.register("rep_upd_line", new UpdateReportLineCommand(service));
        registry.register("rep_finalize", new FinalizeReportCommand(service));
        registry.register("rep_export", new ExportReportCommand(service));
        registry.register("rep_create_sample", new CreateSampleReportCommand(service));
        registry.register("rep_list", new ListReportsCommand(service));
        registry.register("rep_delete", new DeleteReportCommand(service));
        registry.register("exit", new ExitCommand());
        registry.register("help", new HelpCommand(registry.getAllCommands()));

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
                String code = e.getErrorCode();
                String msg = e.getUserMessage();
                String field = e.getFieldName();

                if (code != null && !code.equals("GENERAL_ERROR")) {
                    System.out.println("Ошибка валидации в поле [" + field + "]: " + msg + " (Код: " + code + ")");
                } else {
                    System.out.println(msg);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка формата ввода: проверьте правильность введенных данных (ID должен быть в формате UUID).");
            } catch (Exception e) {
                System.out.println("Произошла системная ошибка: " + e.getMessage());
            }
        }
    }
}