package app.cli.command;

import app.cli.CommandContext;
import app.domain.Report;
import app.domain.ReportStatus;

import java.time.Instant;
import java.util.UUID;

public class AddCommand implements Command {
    @Override
    public String getName() { return "add"; }

    @Override
    public String getDescription() { return "Добавить новый отчет (Только для авторизованных)"; }

    @Override
    public void execute(CommandContext context, String[] args) {
        if (!context.getAuthService().isAuthorized()) {
            System.out.println("Ошибка: Команда доступна только авторизованным пользователям. Выполните login.");
            return;
        }

        System.out.print("Введите название отчета: ");
        String name = context.getScanner().nextLine().trim();
        Integer currentUserId = context.getAuthService().getCurrentUser().getId();
        String currentLogin = context.getAuthService().getCurrentUser().getLogin();

        Report report = new Report(
                UUID.randomUUID(), name, null, ReportStatus.DRAFT,
                currentLogin, Instant.now(), Instant.now()
        );
        report.setOwnerId(currentUserId);

        context.getReportService().getAllReports().add(report);

        System.out.println("Отчет успешно добавлен. Владелец: " + currentLogin);
    }
}