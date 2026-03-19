package repository;

import domain.ReportLine;
import java.util.Set;
import java.util.UUID;

public interface ReportLineRepository {
    void save(ReportLine line);
    void delete(ReportLine line);
    ReportLine findById(UUID id);
    Set<ReportLine> findByReportId(UUID reportId);
}