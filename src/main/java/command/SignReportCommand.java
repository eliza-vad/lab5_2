package command;

import service.ReportService;
import validation.ValidationException;

import java.util.Scanner;
import java.util.UUID;

public class SignReportCommand implements Command {
    private final ReportService service;

    public SignReportCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) throws Exception {
        if (args.length < 3) {
            throw new ValidationException("Ошибка: укажите report_id и username");
        }

        UUID reportId = UUID.fromString(args[1]);
        String signer = args[2];

        service.signReport(reportId, signer);
        System.out.println("OK report " + reportId + " SIGNED by " + signer);
    }

    @Override
    public String getDescription() {
        return "Подписать отчет";
    }

    @Override
    public String getUsage() {
        return "rep_sign <report_id> <username>";
    }
}