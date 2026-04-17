package storage;

import domain.Report;
import domain.ReportLine;

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
        // Проверка ID отчета
        if (report.getId() == null) {
            throw new ValidationException("Отчет имеет null ID");
        }
        if (!reportIds.add(report.getId())) {
            throw new ValidationException("Дубликат ID отчета: " + report.getId());
        }

        // Проверка обязательных полей
        if (report.getName() == null || report.getName().isBlank()) {
            throw new ValidationException("Пустое имя у отчета id=" + report.getId());
        }
        if (report.getName().length() > 255) {
            throw new ValidationException("Имя отчета слишком длинное (>255): " + report.getId());
        }

        // Проверка статуса
        if (report.getStatus() == null) {
            throw new ValidationException("Отсутствует статус у отчета id=" + report.getId());
        }

        // Проверка владельца
        if (report.getOwnerUsername() == null || report.getOwnerUsername().isBlank()) {
            throw new ValidationException("Пустое имя владельца у отчета id=" + report.getId());
        }

        // Проверка даты создания
        if (report.getCreatedAt() == null) {
            throw new ValidationException("Отсутствует дата создания у отчета id=" + report.getId());
        }
    }

    private void validateReportLines(Report report, Set<UUID> lineIds) throws ValidationException {
        if (report.getLines() == null) {
            return;
        }

        for (ReportLine line : report.getLines()) {
            // Проверка ID строки
            if (line.getId() == null) {
                throw new ValidationException("Строка отчета имеет null ID (отчет id=" + report.getId() + ")");
            }
            if (!lineIds.add(line.getId())) {
                throw new ValidationException("Дубликат ID строки отчета: " + line.getId());
            }

            // Проверка связи с отчетом
            if (line.getReportId() == null) {
                throw new ValidationException("Строка отчета имеет null reportId (строка id=" + line.getId() + ")");
            }
            if (!line.getReportId().equals(report.getId())) {
                throw new ValidationException(
                        "Неверный reportId у строки: " + line.getId() +
                                " ожидался " + report.getId() + ", получен " + line.getReportId()
                );
            }

            // Проверка параметра измерения
            if (line.getParam() == null) {
                throw new ValidationException("Отсутствует param у строки id=" + line.getId());
            }

            // Проверка значения
            if (Double.isNaN(line.getValue()) || Double.isInfinite(line.getValue())) {
                throw new ValidationException("Некорректное значение у строки id=" + line.getId());
            }

            // Проверка единицы измерения
            if (line.getUnit() == null || line.getUnit().isBlank()) {
                throw new ValidationException("Пустая единица измерения у строки id=" + line.getId());
            }

            // Проверка даты создания
            if (line.getCreatedAt() == null) {
                throw new ValidationException("Отсутствует дата создания у строки id=" + line.getId());
            }
        }
    }
}