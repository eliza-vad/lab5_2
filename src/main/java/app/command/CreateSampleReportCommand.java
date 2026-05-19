package app.command;

import app.domain.Report;
import app.service.ReportService;

import java.util.Scanner;
import java.util.UUID;

public class CreateSampleReportCommand implements Command {
    private final ReportService service;

    public CreateSampleReportCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        System.out.println("=== Интерактивное создание отчета ===");

        UUID sampleId = UUID.randomUUID();

        System.out.print("Введите ваше имя (username): ");
        String username = scanner.nextLine().trim();

        System.out.print("Введите название отчёта: ");
        String name = scanner.nextLine().trim();

        Report report = service.createSampleReport(sampleId, name, username);
        System.out.println("OK: Отчет успешно создан!");
        System.out.println("ID вашего нового отчета: " + report.getId());
        System.out.println("Связанный sample_id: " + sampleId);
    }

    @Override
    public String getDescription() {
        return "Создать отчет (интерактивный режим)";
    }

    @Override
    public String getUsage() {
        return "rep_create_sample";
    }
}