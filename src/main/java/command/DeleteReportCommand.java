package command;

import service.ReportService;
import validation.ValidationException;

import java.util.Scanner;
import java.util.UUID;

public class DeleteReportCommand implements Command {
    private final ReportService service;

    public DeleteReportCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 2) {
            throw new ValidationException("Ошибка: укажите report_id для удаления");
        }

        UUID reportId = UUID.fromString(args[1]);
        service.deleteReport(reportId);

        System.out.println("OK: Отчет " + reportId + " успешно удален.");
    }

    @Override
    public String getDescription() {
        return "Удалить отчет целиком (со всеми строками)";
    }

    @Override
    public String getUsage() {
        return "rep_delete <report_id>";
    }
}