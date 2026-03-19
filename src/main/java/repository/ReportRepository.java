package repository;

import domain.Report;
import java.util.Set;
import java.util.UUID;

public interface ReportRepository {
    void save(Report report);
    Report findById(UUID id);
    Set<Report> findAll();
}