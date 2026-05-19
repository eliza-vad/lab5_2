package app.cli.command;

import app.cli.CommandContext;

public interface Command {
    String getName();
    String getDescription();
    void execute(CommandContext context, String[] args);
}