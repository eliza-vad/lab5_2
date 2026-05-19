package app.command;

import app.domain.ReportLine;
import app.service.ReportService;
import validation.ValidationException;

import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class ShowReportLinesCommand implements Command {
    private final ReportService service;

    public ShowReportLinesCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 2) {
            throw new ValidationException("Ошибка: укажите report_id");
        }

        UUID reportId = UUID.fromString(args[1]);
        Set<ReportLine> lines = service.getLinesByReportId(reportId);

        System.out.println("ID | Param | Value | Unit | Создано | Обновлено");
        for (ReportLine l : lines) {
            System.out.println(l.getId() + " | "
                    + l.getParam() + " | "
                    + l.getValue() + " | "
                    + l.getUnit() + " | "
                    + l.getCreatedAt() + " | "
                    + l.getUpdatedAt());
        }
    }

    @Override
    public String getDescription() {
        return "Показать строки отчета";
    }

    @Override
    public String getUsage() {
        return "rep_lines <report_id>";
    }
}