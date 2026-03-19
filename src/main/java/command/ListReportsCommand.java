package command;

import domain.Report;
import service.ReportService;
import java.util.Scanner;

public class ListReportsCommand implements Command {
    private final ReportService service;

    public ListReportsCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        System.out.println("ID Name Status");
        for (Report r : service.getAllReports()) {
            System.out.println(r.getId() + " " + r.getName() + " " + r.getStatus());
        }
    }

    @Override
    public String getDescription() {
        return "Показать список всех отчетов";
    }

    @Override
    public String getUsage() {
        return "rep_list";
    }
}