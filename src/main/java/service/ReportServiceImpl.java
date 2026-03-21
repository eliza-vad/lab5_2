package service;

import domain.MeasurementParam;
import domain.Report;
import domain.ReportLine;
import domain.ReportStatus;
import repository.ReportLineRepository;
import repository.ReportRepository;
import validation.ValidationException;
import validation.Validator;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportLineRepository reportLineRepository;

    public ReportServiceImpl(ReportRepository reportRepository, ReportLineRepository reportLineRepository) {
        this.reportRepository = reportRepository;
        this.reportLineRepository = reportLineRepository;
    }

    @Override
    public Report createSampleReport(UUID sampleId, String name, String username) {
        if (sampleId == null) throw new ValidationException("Ошибка: sampleId не может быть null");

        Report report = new Report(
                UUID.randomUUID(),
                name,
                sampleId,
                ReportStatus.DRAFT,
                username,
                Instant.now(),
                Instant.now()
        );

        Validator.validateReport(report);
        reportRepository.save(report);
        return report;
    }

    @Override
    public Set<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public Report getReportById(UUID reportId) {
        if (reportId == null) throw new ValidationException("Ошибка: reportId не может быть null");
        Report report = reportRepository.findById(reportId);
        if (report == null) {
            throw new ValidationException("Ошибка: отчет с id=" + reportId + " не найден");
        }
        return report;
    }
    @Override
    public void deleteReport(UUID reportId) {
        Report report = getReportById(reportId);

        Set<ReportLine> lines = getLinesByReportId(reportId);
        for (ReportLine line : lines) {
            reportLineRepository.delete(line);
        }

        reportRepository.delete(report);
    }

    @Override
    public ReportLine addReportLine(UUID reportId, MeasurementParam param, double value, String unit) {
        Report report = getReportById(reportId);

        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ValidationException("Ошибка: можно добавлять строки только в отчет со статусом DRAFT.");
        }

        ReportLine line = new ReportLine(
                UUID.randomUUID(),
                reportId,
                param,
                value,
                unit,
                Instant.now(),
                Instant.now()
        );

        Validator.validateReportLine(line);
        reportLineRepository.save(line);
        report.setUpdatedAt(Instant.now());
        return line;
    }

    @Override
    public Set<ReportLine> getLinesByReportId(UUID reportId) {
        getReportById(reportId); // Проверяем, существует ли отчет
        return reportLineRepository.findByReportId(reportId);
    }

    private ReportLine getLineById(UUID lineId) {
        if (lineId == null) throw new ValidationException("Ошибка: lineId не может быть null");
        ReportLine line = reportLineRepository.findById(lineId);
        if (line == null) {
            throw new ValidationException("Ошибка: строка с id=" + lineId + " не найдена");
        }
        return line;
    }

    @Override
    public void updateReportLine(UUID lineId, MeasurementParam newParam, Double newValue, String newUnit) {
        ReportLine line = getLineById(lineId);
        Report report = getReportById(line.getReportId());

        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ValidationException("Ошибка: можно изменять строки только в отчете со статусом DRAFT.");
        }

        if (newParam != null) line.setParam(newParam);
        if (newValue != null) line.setValue(newValue);
        if (newUnit != null) line.setUnit(newUnit);

        line.setUpdatedAt(Instant.now());
        Validator.validateReportLine(line);
        report.setUpdatedAt(Instant.now());
    }

    @Override
    public void deleteReportLine(UUID lineId) {
        ReportLine line = getLineById(lineId);
        Report report = getReportById(line.getReportId());

        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ValidationException("Ошибка: можно удалять строки только в отчете со статусом DRAFT.");
        }

        reportLineRepository.delete(line);
        report.setUpdatedAt(Instant.now());
    }

    @Override
    public void finalizeReport(UUID reportId) {
        Report report = getReportById(reportId);
        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ValidationException("Ошибка: можно финализировать только DRAFT отчет.");
        }
        report.setStatus(ReportStatus.FINAL);
        report.setUpdatedAt(Instant.now());
        Validator.validateReport(report);
    }

    @Override
    public void signReport(UUID reportId, String username) {
        Report report = getReportById(reportId);
        if (report.getStatus() == ReportStatus.DRAFT) {
            throw new ValidationException("Ошибка: сначала сделайте finalize.");
        }
        if (report.getStatus() == ReportStatus.SIGNED) {
            throw new ValidationException("Ошибка: отчет уже подписан.");
        }
        report.setStatus(ReportStatus.SIGNED);
        report.setSignedBy(username);
        report.setUpdatedAt(Instant.now());
        Validator.validateReport(report);
    }
}