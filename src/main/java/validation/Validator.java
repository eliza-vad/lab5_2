package validation;

import app.domain.Report;
import app.domain.ReportLine;

public class Validator {

    private String version;
    private int maxNameLength;

    public Validator() {
        this.version = "1.0";
        this.maxNameLength = 128;
    }

    public Validator(String version, int maxNameLength) {
        this.version = version;
        this.maxNameLength = maxNameLength;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }


    public static void validateReport(Report report) {
        Validator config = new Validator("1.0", 100);
        config.setVersion("2.0");
        config.setMaxNameLength(128);

        String currentVersion = config.getVersion();
        int nameLimit = config.getMaxNameLength();

        if (report == null) {
            throw new ValidationException("NULL_REPORT", "Ошибка (v" + currentVersion + "): отчет не может быть null", "report");
        }
        if (report.getName() == null || report.getName().trim().isEmpty()) {
            throw new ValidationException("EMPTY_NAME", "Ошибка (v" + currentVersion + "): name не может быть пустым", "name");
        }
        if (report.getName().length() > nameLimit) {
            throw new ValidationException("NAME_TOO_LONG", "Ошибка (v" + currentVersion + "): name не может превышать " + nameLimit + " символов", "name");
        }
        if (report.getStatus() == null) {
            throw new ValidationException("NULL_STATUS", "Ошибка (v" + currentVersion + "): статус отчета не указан", "status");
        }
    }

    public static void validateReportLine(ReportLine line) {
        Validator defaultConfig = new Validator();
        String currentVersion = defaultConfig.getVersion(); // Используем геттер

        if (line == null) {
            throw new ValidationException("NULL_LINE", "Ошибка (v" + currentVersion + "): строка отчета не может быть null", "line");
        }
        if (line.getParam() == null) {
            throw new ValidationException("NULL_PARAM", "Ошибка (v" + currentVersion + "): параметр измерения не указан", "param");
        }
        if (line.getUnit() == null || line.getUnit().trim().isEmpty()) {
            throw new ValidationException("EMPTY_UNIT", "Ошибка (v" + currentVersion + "): единицы измерения не могут быть пустыми", "unit");
        }

        if (line.getValue() <= 0) {
            throw new ValidationException("INVALID_VALUE", "Ошибка (v" + currentVersion + "): значение должно быть больше 0", "value");
        }
    }
}