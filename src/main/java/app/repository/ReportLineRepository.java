package app.repository;

import app.domain.ReportLine;

import java.util.Set;
import java.util.UUID;

public interface ReportLineRepository {
    ReportLine save(ReportLine line);
    void delete(ReportLine line);
    ReportLine findById(UUID id);
    Set<ReportLine> findAll();
    Set<ReportLine> findByReportId(UUID reportId);

    void clear();
}