package app.command;

import app.domain.Report;
import app.service.ReportService;

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

        if (reports == null || reports.isEmpty()) {
            System.out.println("Нет созданных отчетов.");
            return;
        }

        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.printf("%-40s | %-20s | %-12s | %-15s | %-8s%n",
                "ID отчета", "Название", "Статус", "Владелец", "Строк");
        System.out.println("----------------------------------------------------------------------------------------------------");

        for (Report r : reports) {
            System.out.printf("%-40s | %-20s | %-12s | %-15s | %-8d%n",
                    r.getId().toString(),
                    truncate(r.getName(), 20),
                    r.getStatus().toString(),
                    truncate(r.getOwnerUsername(), 15),
                    r.getLines() != null ? r.getLines().size() : 0);
        }
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Всего отчетов: " + reports.size());
    }

    @Override
    public String getDescription() {
        return "Показать список всех отчетов";
    }

    @Override
    public String getUsage() {
        return "rep_list";
    }
    private String truncate(String str, int maxLength) {
        if (str == null || str.isEmpty()) {
            return "—";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}