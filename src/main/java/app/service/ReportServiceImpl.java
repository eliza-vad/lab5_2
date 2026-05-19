package app.service;

import app.domain.MeasurementParam;
import app.domain.Report;
import app.domain.ReportLine;
import app.domain.ReportStatus;
import app.repository.ReportLineRepository;
import app.repository.ReportRepository;
import validation.ValidationException;
import validation.Validator;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportLineRepository reportLineRepository;

    private final Set<Report> memoryCache;

    public ReportServiceImpl(ReportRepository reportRepository, ReportLineRepository reportLineRepository) {
        this.reportRepository = reportRepository;
        this.reportLineRepository = reportLineRepository;
        this.memoryCache = new HashSet<>();

        Set<Report> fromDb = reportRepository.findAll();
        if (fromDb != null) {
            for (Report r : fromDb) {
                this.memoryCache.add(r);
            }
        }
    }

    @Override
    public Report createSampleReport(UUID sampleId, String name, String username) {
        if (sampleId == null) {
            throw new ValidationException("Ошибка: sampleId не может быть null");
        }

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
        memoryCache.add(report);

        return report;
    }

    @Override
    public Set<Report> getAllReports() {
        return memoryCache;
    }

    @Override
    public Report getReportById(UUID reportId) {
        if (reportId == null) {
            throw new ValidationException("Ошибка: reportId не может быть null");
        }

        Report found = null;
        for (Report r : memoryCache) {
            if (r.getId().equals(reportId)) {
                found = r;
                break;
            }
        }

        if (found == null) {
            throw new ValidationException("Ошибка: отчет с id=" + reportId + " не найден");
        }

        return found;
    }

    @Override
    public void deleteReport(UUID id) {
        Report report = getReportById(id);

        reportRepository.delete(report);
        memoryCache.remove(report);
    }

    @Override
    public void updateReportName(UUID id, String newName) {
        if (id == null) {
            throw new ValidationException("Ошибка: id не может быть null");
        }

        if (newName == null || newName.isBlank()) {
            throw new ValidationException("Ошибка: название отчета не может быть пустым");
        }

        Report report = getReportById(id);

        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ValidationException("Ошибка: можно изменять название только у отчета со статусом DRAFT.");
        }

        report.setName(newName.trim());
        report.setUpdatedAt(Instant.now());

        Validator.validateReport(report);

        reportRepository.save(report);
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

        reportRepository.save(report);
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

        reportRepository.save(report);
    }

    @Override
    public void replaceAll(List<Report> reports) {
        Set<ReportLine> allLines = reportLineRepository.findAll();
        for (ReportLine line : allLines) {
            reportLineRepository.delete(line);
        }

        reportRepository.clear();
        memoryCache.clear();

        if (reports != null) {
            for (Report report : reports) {
                reportRepository.save(report);
                memoryCache.add(report);

                if (report.getLines() != null) {
                    for (ReportLine line : report.getLines()) {
                        line.setReportId(report.getId());
                        reportLineRepository.save(line);
                    }
                }
            }
        }
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
        reportRepository.save(report);
        return line;
    }

    @Override
    public Set<ReportLine> getLinesByReportId(UUID reportId) {
        getReportById(reportId);
        return reportLineRepository.findByReportId(reportId);
    }

    private ReportLine getLineById(UUID lineId) {
        if (lineId == null) {
            throw new ValidationException("Ошибка: lineId не может быть null");
        }

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

        if (newParam != null) {
            line.setParam(newParam);
        }
        if (newValue != null) {
            line.setValue(newValue);
        }
        if (newUnit != null) {
            line.setUnit(newUnit);
        }

        line.setUpdatedAt(Instant.now());
        Validator.validateReportLine(line);

        report.setUpdatedAt(Instant.now());
        reportRepository.save(report);
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
        reportRepository.save(report);
    }
}