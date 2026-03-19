package command;

import service.ReportService;
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
            throw new ValidationException("Ошибка: укажите line_id");
        }

        long lineId = Long.parseLong(args[1]);
        service.deleteReportLine(UUID.fromString(String.valueOf(lineId)));
        System.out.println("OK deleted");
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