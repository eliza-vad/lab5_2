package app.cli.command;

import app.cli.CommandContext;
import app.domain.Report;

public class ShowCommand implements Command {
    @Override
    public String getName() { return "show"; }

    @Override
    public String getDescription() { return "Показать все отчеты (Доступно всем)"; }

    @Override
    public void execute(CommandContext context, String[] args) {
        var reports = context.getReportService().getAllReports();
        if (reports.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        reports.forEach(System.out::println);
    }
}