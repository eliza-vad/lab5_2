package app.cli.command;

import app.cli.CommandContext;
import app.domain.Report;

import java.util.List;
import java.util.stream.Collectors;

public class ClearCommand implements Command {
    @Override
    public String getName() { return "clear"; }

    @Override
    public String getDescription() { return "Удалить все ВАШИ отчеты (Только для авторизованных)"; }

    @Override
    public void execute(CommandContext context, String[] args) {
        if (!context.getAuthService().isAuthorized()) {
            System.out.println("Ошибка: Команда доступна только авторизованным пользователям.");
            return;
        }

        Integer currentUserId = context.getAuthService().getCurrentUser().getId();
        List<Report> myReports = context.getReportService().getAllReports().stream()
                .filter(r -> currentUserId.equals(r.getOwnerId()))
                .collect(Collectors.toList());

        if (myReports.isEmpty()) {
            System.out.println("У вас нет отчетов для удаления.");
            return;
        }
        for (Report report : myReports) {
            context.getReportService().deleteReport(report.getId());
        }

        System.out.println("Успешно удалено ваших отчетов: " + myReports.size());
    }
}