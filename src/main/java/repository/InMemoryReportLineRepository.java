package repository;

import domain.ReportLine;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class InMemoryReportLineRepository implements ReportLineRepository {
    private final Set<ReportLine> reportLines = new HashSet<>();

    @Override
    public void save(ReportLine line) {
        reportLines.add(line);
    }

    @Override
    public void delete(ReportLine line) {
        reportLines.remove(line);
    }

    @Override
    public ReportLine findById(UUID id) {
        for (ReportLine line : reportLines) {
            if (Objects.equals(line.getId(), id)) {
                return line;
            }
        }
        return null;
    }

    @Override
    public Set<ReportLine> findByReportId(UUID reportId) {
        Set<ReportLine> result = new HashSet<>();
        for (ReportLine line : reportLines) {
            if (Objects.equals(line.getReportId(), reportId)) {
                result.add(line);
            }
        }
        return result;
    }
}