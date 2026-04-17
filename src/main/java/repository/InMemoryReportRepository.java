package repository;

import domain.Report;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class InMemoryReportRepository implements ReportRepository {

    private final Set<Report> reports = new LinkedHashSet<>();

    @Override
    public Report save(Report report) {
        if (report == null) {
            return null;
        }

        reports.removeIf(r -> r.getId().equals(report.getId()));
        reports.add(report);
        return report;
    }

    @Override
    public void delete(Report report) {
        if (report == null) {
            return;
        }
        reports.removeIf(r -> r.getId().equals(report.getId()));
    }

    @Override
    public Report findById(UUID id) {
        if (id == null) {
            return null;
        }

        for (Report report : reports) {
            if (id.equals(report.getId())) {
                return report;
            }
        }
        return null;
    }

    @Override
    public Set<Report> findAll() {
        return new LinkedHashSet<>(reports);
    }

    @Override
    public void clear() {
        reports.clear();
    }
}