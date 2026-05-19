package app.cli.command;

import app.cli.CommandContext;

public class HelpCommand implements Command {
    @Override
    public String getName() { return "help"; }

    @Override
    public String getDescription() { return "Вывести справку по командам (Доступно всем)"; }

    @Override
    public void execute(CommandContext context, String[] args) {
        System.out.println("Доступные команды:");
        for (Command cmd : context.getCommands().values()) {
            System.out.printf(" %-10s - %s%n", cmd.getName(), cmd.getDescription());
        }
    }
}