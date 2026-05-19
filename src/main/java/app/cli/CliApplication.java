package app.cli;

import app.cli.command.Command;
import app.service.AuthService;
import app.service.ReportService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class CliApplication {

    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final CommandContext context;

    public CliApplication(AuthService authService, ReportService reportService, Scanner scanner) {
        this.context = new CommandContext(scanner, authService, reportService, commands);
    }

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void run() {
        System.out.println("=== Система управления отчетами (CLI) ===");
        System.out.println("Введите 'help' для просмотра доступных команд или 'exit' для выхода.");

        Scanner scanner = context.getScanner();

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Завершение работы...");
                break;
            }

            String[] args = input.split("\\s+");
            String commandName = args[0].toLowerCase();

            Command command = commands.get(commandName);
            if (command != null) {
                command.execute(context, args);
            } else {
                System.out.println("Неизвестная команда. Введите 'help' для справки.");
            }
        }
    }
}