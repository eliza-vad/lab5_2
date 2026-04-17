package repository;

import domain.ReportLine;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class InMemoryReportLineRepository implements ReportLineRepository {

    private final Set<ReportLine> lines = new LinkedHashSet<>();

    @Override
    public ReportLine save(ReportLine line) {
        if (line == null) {
            return null;
        }

        lines.removeIf(l -> l.getId().equals(line.getId()));
        lines.add(line);
        return line;
    }

    @Override
    public void delete(ReportLine line) {
        if (line == null) {
            return;
        }
        lines.removeIf(l -> l.getId().equals(line.getId()));
    }

    @Override
    public ReportLine findById(UUID id) {
        if (id == null) {
            return null;
        }

        for (ReportLine line : lines) {
            if (id.equals(line.getId())) {
                return line;
            }
        }
        return null;
    }

    @Override
    public Set<ReportLine> findAll() {
        return new LinkedHashSet<>(lines);
    }

    @Override
    public Set<ReportLine> findByReportId(UUID reportId) {
        Set<ReportLine> result = new LinkedHashSet<>();

        if (reportId == null) {
            return result;
        }

        for (ReportLine line : lines) {
            if (reportId.equals(line.getReportId())) {
                result.add(line);
            }
        }

        return result;
    }

    @Override
    public void clear() {
        lines.clear();
    }
}