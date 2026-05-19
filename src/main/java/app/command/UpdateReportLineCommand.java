package app.command;

import app.domain.MeasurementParam;
import app.service.ReportService;
import validation.ValidationException;

import java.util.Scanner;
import java.util.UUID;

public class UpdateReportLineCommand implements Command {
    private final ReportService service;

    public UpdateReportLineCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 2) {
            throw new ValidationException("Ошибка: укажите line_id");
        }

        UUID lineId = UUID.fromString(args[1]);

        MeasurementParam param = null;
        Double value = null;
        String unit = null;

        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("--param") && i + 1 < args.length) {
                try {
                    param = MeasurementParam.valueOf(args[i + 1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ValidationException("Ошибка: неверный параметр. Допустимые значения: TEMPERATURE, PH, VISCOSITY, DENSITY");
                }
                i++;
            } else if (args[i].equals("--val") && i + 1 < args.length) {
                try {
                    value = Double.parseDouble(args[i + 1]);
                } catch (NumberFormatException e) {
                    throw new ValidationException("Ошибка: значение должно быть числом");
                }
                i++;
            } else if (args[i].equals("--unit") && i + 1 < args.length) {
                unit = args[i + 1];
                i++;
            }
        }

        if (param == null && value == null && unit == null) {
            throw new ValidationException("Ошибка: не указаны параметры для обновления (используйте --param, --val, --unit)");
        }

        service.updateReportLine(lineId, param, value, unit);
        System.out.println("OK report line " + lineId + " UPDATED");
    }

    @Override
    public String getDescription() {
        return "Обновить строку отчета";
    }

    @Override
    public String getUsage() {
        return "rep_upd_line <line_id> [--param <PARAM>] [--val <VALUE>] [--unit <UNIT>]";
    }
}