package domain;

import java.time.Instant;
import java.util.UUID;

public class ReportLine {

    private UUID id;
    private UUID reportId;
    private MeasurementParam param;
    private double value;
    private String unit;
    private Instant createdAt;
    private Instant updatedAt;

    // Пустой конструктор
    public ReportLine() {
    }

    public ReportLine(UUID id, UUID reportId, MeasurementParam param, double value, String unit, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.reportId = reportId;
        this.param = param;
        this.value = value;
        this.unit = unit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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
}