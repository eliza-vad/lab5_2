package service;

import domain.MeasurementParam;
import domain.Report;
import domain.ReportLine;
import java.util.Set;
import java.util.UUID;

public interface ReportService {
    Report createSampleReport(UUID sampleId, String name, String username);
    Set<Report> getAllReports();
    Report getReportById(UUID reportId);

    void deleteReport(UUID reportId);

    ReportLine addReportLine(UUID reportId, MeasurementParam param, double value, String unit);
    Set<ReportLine> getLinesByReportId(UUID reportId);
    void updateReportLine(UUID lineId, MeasurementParam newParam, Double newValue, String newUnit);
    void deleteReportLine(UUID lineId);
    void finalizeReport(UUID reportId);
    void signReport(UUID reportId, String username);
}