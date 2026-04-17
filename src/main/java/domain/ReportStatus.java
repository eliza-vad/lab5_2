package domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportStatus {
    DRAFT("DRAFT", "Черновик"),
    FINAL("FINAL", "Финальный"),
    SIGNED("SIGNED", "Подписан");

    private final String value;
    private final String description;

    ReportStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ReportStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (ReportStatus status : ReportStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус: " + value);
    }

    public static ReportStatus fromString(String name) {
        if (name == null) {
            return null;
        }
        try {
            return ReportStatus.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестный статус: " + name);
        }
    }

    public boolean isDraft() {
        return this == DRAFT;
    }

    public boolean isFinal() {
        return this == FINAL;
    }

    public boolean isSigned() {
        return this == SIGNED;
    }

    public boolean canBeEdited() {
        return this == DRAFT;
    }

    public boolean canBeSigned() {
        return this == FINAL;
    }

    @Override
    public String toString() {
        return value;
    }
}