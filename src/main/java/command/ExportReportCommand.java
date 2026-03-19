package command;

import domain.Report;
import domain.ReportLine;
import service.ReportService;
import validation.ValidationException;

import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class ExportReportCommand implements Command {
    private final ReportService service;

    public ExportReportCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 2) {
            throw new ValidationException("Ошибка: укажите report_id");
        }

        UUID reportId = UUID.fromString(args[1]);

        Report report = service.getReportById(reportId);

        System.out.println("ID: " + report.getId());
        System.out.println("Название: " + report.getName());
        System.out.println("ID пробы (sampleId): " + report.getSampleId());
        System.out.println("ID эксперимента (experimentId): " + report.getExperimentId());
        System.out.println("Статус: " + report.getStatus());
        System.out.println("Владелец: " + report.getOwnerUsername());
        System.out.println("Подписант: " + (report.getSignedBy() != null ? report.getSignedBy() : "не подписан"));
        System.out.println("Создан: " + report.getCreatedAt());
        System.out.println("Обновлен: " + report.getUpdatedAt());

        Set<ReportLine> lines = service.getLinesByReportId(reportId);
        System.out.println("Строки отчета");
        if (lines.isEmpty()) {
            System.out.println("Строк отчета нет");
        } else {
            System.out.println("ID Параметр Значение Единицы");
            for (ReportLine line : lines) {
                System.out.println(line.getId() + " " + line.getParam() + " " + line.getValue() + " " + line.getUnit());
            }
        }
    }

    @Override
    public String getDescription() {
        return "Экспортировать отчет";
    }

    @Override
    public String getUsage() {
        return "rep_export <report_id>";
    }
}