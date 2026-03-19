package command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        System.out.println("\n--- Доступные команды ---");

        // Сортируем названия команд по алфавиту
        List<String> commandNames = new ArrayList<>(commands.keySet());
        Collections.sort(commandNames);

        for (String name : commandNames) {
            Command cmd = commands.get(name);
            // %-20s делает отступ ровно в 20 символов, выравнивая текст по левому краю
            System.out.printf("%-20s - %s%n", name, cmd.getDescription());
            System.out.printf("  Использование: %s%n", cmd.getUsage());
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    @Override
    public String getDescription() {
        return "Показать справку по командам";
    }

    @Override
    public String getUsage() {
        return "help";
    }
}