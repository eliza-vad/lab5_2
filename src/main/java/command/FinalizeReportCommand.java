package command;

import service.ReportService;
import validation.ValidationException;

import java.util.Scanner;
import java.util.UUID;

public class FinalizeReportCommand implements Command {
    private final ReportService service;

    public FinalizeReportCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 2) {
            throw new ValidationException("Ошибка: укажите report_id");
        }

        UUID reportId = UUID.fromString(args[1]);
        service.finalizeReport(reportId);
        System.out.println("OK report " + reportId + " FINAL");
    }

    @Override
    public String getDescription() {
        return "Финализировать отчет";
    }

    @Override
    public String getUsage() {
        return "rep_finalize <report_id>";
    }
}