package command;

import domain.Report;
import service.ReportService;
import validation.ValidationException;
import java.util.Scanner;
import java.util.UUID;

public class ShowReportCommand implements Command {
    private final ReportService service;

    public ShowReportCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 2) {
            throw new ValidationException("Ошибка: укажите report_id");
        }

        UUID reportId = UUID.fromString(args[1]);
        Report report = service.getReportById(reportId);
        int linesCount = service.getLinesByReportId(reportId).size();

        System.out.println("Report #" + report.getId());
        System.out.println("name: " + report.getName());
        System.out.println("status: " + report.getStatus());
        System.out.println("lines: " + linesCount);
        if (report.getSignedBy() != null) {
            System.out.println("signed by: " + report.getSignedBy());
        }
    }

    @Override
    public String getDescription() {
        return "Показать отчет";
    }

    @Override
    public String getUsage() {
        return "rep_show <report_id>";
    }
}