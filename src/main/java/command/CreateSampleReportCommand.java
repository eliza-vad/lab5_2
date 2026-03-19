package command;

import domain.Report;
import service.ReportService;
import validation.ValidationException;

import java.util.Scanner;
import java.util.UUID;

public class CreateSampleReportCommand implements Command {
    private final ReportService service;

    public CreateSampleReportCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 3) {
            throw new ValidationException("Ошибка: укажите sample_id и username");
        }

        UUID sampleId = UUID.fromString(args[1]);
        String username = args[2];

        System.out.print("Название отчёта: ");
        String name = scanner.nextLine().trim();

        Report report = service.createSampleReport(sampleId, name, username);
        System.out.println("OK report_id=" + report.getId());
    }

    @Override
    public String getDescription() {
        return "Создать отчет по образцу";
    }

    @Override
    public String getUsage() {
        return "rep_create_sample <sample_id> <username>";
    }
}