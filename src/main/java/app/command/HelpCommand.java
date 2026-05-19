package app.command;

import app.storage.FileStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;
    private final FileStorage fileStorage;

    public HelpCommand(Map<String, Command> commands, FileStorage fileStorage) {
        this.commands = commands;
        this.fileStorage = fileStorage;
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        System.out.println("\n--- Доступные команды ---");

        List<String> commandNames = new ArrayList<>(commands.keySet());
        Collections.sort(commandNames);

        for (String name : commandNames) {
            Command cmd = commands.get(name);
            System.out.printf("%-20s - %s%n", name, cmd.getDescription());
            System.out.printf("  Использование: %s%n", cmd.getUsage());
            System.out.println();
        }

        System.out.println("--- Сохранённые XML-файлы ---");

        List<String> xmlFiles = fileStorage.getSavedXmlDisplayNames();
        if (xmlFiles.isEmpty()) {
            System.out.println("Нет сохранённых XML-файлов.");
        } else {
            for (String item : xmlFiles) {
                System.out.println("- " + item);
            }
        }

        System.out.println("-------------------------");
    }

    @Override
    public String getDescription() {
        return "Показать справку по командам и сохранённым XML-файлам";
    }

    @Override
    public String getUsage() {
        return "help";
    }
}