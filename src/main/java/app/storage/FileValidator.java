package app.storage;

import app.domain.Report;
import app.domain.ReportLine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FileValidator {

    public void validate(List<Report> reports) throws ValidationException {
        if (reports == null) {
            throw new ValidationException("Список отчетов равен null");
        }

        Set<UUID> reportIds = new HashSet<>();
        Set<UUID> lineIds = new HashSet<>();

        for (Report report : reports) {
            validateReport(report, reportIds);
            validateReportLines(report, lineIds);
        }
    }

    private void validateReport(Report report, Set<UUID> reportIds) throws ValidationException {
        if (report.getId() == null) {
            throw new ValidationException("Отчет имеет null ID");
        }
        if (!reportIds.add(report.getId())) {
            throw new ValidationException("Дубликат ID отчета: " + report.getId());
        }

        if (report.getName() == null || report.getName().isBlank()) {
            throw new ValidationException("Пустое имя у отчета id=" + report.getId());
        }
        if (report.getName().length() > 255) {
            throw new ValidationException("Имя отчета слишком длинное (>255): " + report.getId());
        }

        if (report.getStatus() == null) {
            throw new ValidationException("Отсутствует статус у отчета id=" + report.getId());
        }

        if (report.getOwnerUsername() == null || report.getOwnerUsername().isBlank()) {
            throw new ValidationException("Пустое имя владельца у отчета id=" + report.getId());
        }

        if (report.getCreatedAt() == null) {
            throw new ValidationException("Отсутствует дата создания у отчета id=" + report.getId());
        }
    }

    private void validateReportLines(Report report, Set<UUID> lineIds) throws ValidationException {
        if (report.getLines() == null) {
            return;
        }

        for (ReportLine line : report.getLines()) {
            if (line.getId() == null) {
                throw new ValidationException("Строка отчета имеет null ID (отчет id=" + report.getId() + ")");
            }
            if (!lineIds.add(line.getId())) {
                throw new ValidationException("Дубликат ID строки отчета: " + line.getId());
            }

            if (line.getReportId() == null) {
                throw new ValidationException("Строка отчета имеет null reportId (строка id=" + line.getId() + ")");
            }
            if (!line.getReportId().equals(report.getId())) {
                throw new ValidationException(
                        "Неверный reportId у строки: " + line.getId() +
                                " ожидался " + report.getId() + ", получен " + line.getReportId()
                );
            }

            if (line.getParam() == null) {
                throw new ValidationException("Отсутствует param у строки id=" + line.getId());
            }

            if (Double.isNaN(line.getValue()) || Double.isInfinite(line.getValue())) {
                throw new ValidationException("Некорректное значение у строки id=" + line.getId());
            }

            if (line.getUnit() == null || line.getUnit().isBlank()) {
                throw new ValidationException("Пустая единица измерения у строки id=" + line.getId());
            }

            if (line.getCreatedAt() == null) {
                throw new ValidationException("Отсутствует дата создания у строки id=" + line.getId());
            }
        }
    }
}