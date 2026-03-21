package command;

import domain.Report;
import service.ReportService;
import java.util.Scanner;
import java.util.Set;

public class ListReportsCommand implements Command {
    private final ReportService service;

    public ListReportsCommand(ReportService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        Set<Report> reports = service.getAllReports();

        if (reports.isEmpty()) {
            System.out.println("Нет созданных отчетов.");
            return;
        }

        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.printf("%-40s | %-20s | %-12s | %-15s%n", "ID отчета", "Название", "Статус", "Создатель");
        System.out.println("-------------------------------------------------------------------------------------------------");

        for (Report r : reports) {
            System.out.printf("%-40s | %-20s | %-12s | %-15s%n",
                    r.getId().toString(),
                    r.getName(),
                    r.getStatus().toString(),
                    r.getCreatedBy());
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
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