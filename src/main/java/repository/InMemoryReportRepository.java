package repository;

import domain.Report;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class InMemoryReportRepository implements ReportRepository {
    private final Set<Report> reports = new HashSet<>();

    @Override
    public void save(Report report) {
        reports.add(report);
    }

    @Override
    public Report findById(UUID id) {
        for (Report r : reports) {
            if (Objects.equals(r.getId(), id)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public Set<Report> findAll() {
        return reports;
    }

    @Override
    public void delete(Report report) {
        reports.remove(report);
    }
}