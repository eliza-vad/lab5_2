package app.cli.command;

import app.cli.CommandContext;

public class InfoCommand implements Command {
    @Override
    public String getName() { return "info"; }

    @Override
    public String getDescription() { return "Вывести общую информацию о коллекции (Доступно всем)"; }

    @Override
    public void execute(CommandContext context, String[] args) {
        int count = context.getReportService().getAllReports().size();
        System.out.println("--- Информация о системе ---");
        System.out.println("Тип коллекции: Список отчетов (List)");
        System.out.println("Количество элементов: " + count);

        if (context.getAuthService().isAuthorized()) {
            System.out.println("Текущий пользователь: " + context.getAuthService().getCurrentUser().getLogin());
        } else {
            System.out.println("Статус: Гость (не авторизован)");
        }
    }
}