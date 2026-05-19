package app.command;

import app.domain.MeasurementParam;
import app.domain.ReportLine;
import app.service.ReportService;
import validation.ValidationException;

import java.util.Scanner;
import java.util.UUID;

public class AddReportLineCommand implements Command {
    private final ReportService service;

    public AddReportLineCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 2) {
            throw new ValidationException("Ошибка: укажите report_id");
        }

        UUID reportId = UUID.fromString(args[1]);

        System.out.print("Параметр (PH/CONDUCTIVITY/TURBIDITY/NITRATE): ");
        String paramStr = scanner.nextLine().trim().toUpperCase();
        MeasurementParam param = MeasurementParam.valueOf(paramStr);

        System.out.print("Значение: ");
        double value = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Единицы: ");
        String unit = scanner.nextLine().trim();

        ReportLine line = service.addReportLine(reportId, param, value, unit);
        System.out.println("OK line_id=" + line.getId());
    }

    @Override
    public String getDescription() {
        return "Добавить строку в отчет";
    }

    @Override
    public String getUsage() {
        return "rep_addline <report_id>";
    }
}