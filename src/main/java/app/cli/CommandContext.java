package app.cli;

import app.cli.command.Command;
import app.service.AuthService;
import app.service.ReportService;

import java.util.Map;
import java.util.Scanner;

public class CommandContext {
    private final Scanner scanner;
    private final AuthService authService;
    private final ReportService reportService;
    private final Map<String, Command> commands;

    public CommandContext(Scanner scanner, AuthService authService, ReportService reportService, Map<String, Command> commands) {
        this.scanner = scanner;
        this.authService = authService;
        this.reportService = reportService;
        this.commands = commands;
    }

    public Scanner getScanner() { return scanner; }
    public AuthService getAuthService() { return authService; }
    public ReportService getReportService() { return reportService; }
    public Map<String, Command> getCommands() { return commands; }
}