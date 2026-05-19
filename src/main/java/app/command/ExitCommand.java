package app.command;

import java.util.Scanner;

public class ExitCommand implements Command {
    @Override
    public void execute(String[] args, Scanner scanner) {
        System.out.println("Выход из программы...");
        System.exit(0);
    }

    @Override
    public String getDescription() {
        return "Выйти из программы";
    }

    @Override
    public String getUsage() {
        return "exit";
    }
}