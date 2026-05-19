package app.cli.command;

import app.cli.CommandContext;
import app.domain.Report;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class UpdateCommand implements Command {
    @Override
    public String getName() { return "update"; }

    @Override
    public String getDescription() { return "Обновить название отчета (Только для авторизованных)"; }

    @Override
    public void execute(CommandContext context, String[] args) {
        if (!context.getAuthService().isAuthorized()) {
            System.out.println("Ошибка: Команда доступна только авторизованным пользователям.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Ошибка: Укажите ID отчета (пример: update <UUID>)");
            return;
        }

        try {
            UUID id = UUID.fromString(args[1]);

            Optional<Report> targetReport = context.getReportService().getAllReports().stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst();

            if (targetReport.isEmpty()) {
                System.out.println("Ошибка: Отчет с таким ID не найден.");
                return;
            }
            Integer currentUserId = context.getAuthService().getCurrentUser().getId();
            if (!currentUserId.equals(targetReport.get().getOwnerId())) {
                System.out.println("Ошибка: у вас нет прав на изменение этого объекта.");
                return;
            }

            System.out.print("Введите новое название отчета: ");
            String newName = context.getScanner().nextLine().trim();

            context.getReportService().updateReportName(id, newName);
            targetReport.get().setUpdatedAt(Instant.now());

            System.out.println("Название отчета успешно обновлено.");
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }
    }
}