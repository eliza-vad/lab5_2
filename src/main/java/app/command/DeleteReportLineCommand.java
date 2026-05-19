package app.command;

import app.service.ReportService;
import validation.ValidationException;
import java.util.Scanner;
import java.util.UUID;

public class DeleteReportLineCommand implements Command {
    private final ReportService service;

    public DeleteReportLineCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 2) {
            throw new ValidationException("Ошибка: укажите line_id (в формате UUID)");
        }

        UUID lineId = UUID.fromString(args[1]);
        service.deleteReportLine(lineId);

        System.out.println("OK: строка " + lineId + " удалена.");
    }

    @Override
    public String getDescription() {
        return "Удалить строку отчета";
    }

    @Override
    public String getUsage() {
        return "rep_delline <line_id>";
    }
}