package app.repository;

import app.domain.Report;

import java.util.Set;
import java.util.UUID;

public interface ReportRepository {
    Report save(Report report);
    void delete(Report report);
    Report findById(UUID id);
    Set<Report> findAll();

    void clear();
}