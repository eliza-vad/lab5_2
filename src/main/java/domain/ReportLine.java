package domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ReportLine {

    @JacksonXmlProperty(isAttribute = true)
    private UUID id;

    @JacksonXmlProperty(isAttribute = true)
    private UUID reportId;

    @JacksonXmlProperty(isAttribute = true)
    private MeasurementParam param;

    @JacksonXmlProperty(isAttribute = true)
    private double value;

    @JacksonXmlProperty(isAttribute = true)
    private String unit;

    @JacksonXmlProperty(isAttribute = true)
    private Instant createdAt;

    @JacksonXmlProperty(isAttribute = true)
    private Instant updatedAt;

    // Пустой конструктор (обязателен для Jackson)
    public ReportLine() {
    }

    // Конструктор со всеми полями
    public ReportLine(UUID id, UUID reportId, MeasurementParam param, double value,
                      String unit, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.reportId = reportId;
        this.param = param;
        this.value = value;
        this.unit = unit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Геттеры и сеттеры
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getReportId() {
        return reportId;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public MeasurementParam getParam() {
        return param;
    }

    public void setParam(MeasurementParam param) {
        this.param = param;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportLine that = (ReportLine) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReportLine{" +
                "id=" + id +
                ", reportId=" + reportId +
                ", param=" + param +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}