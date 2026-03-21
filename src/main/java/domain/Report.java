package domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Report {
    private UUID id;
    private String name;
    private UUID sampleId;
    private UUID experimentId;
    private ReportStatus status;
    private String ownerUsername;
    private String signedBy;
    private Instant createdAt;
    private Instant updatedAt;

    // Пустой конструктор (оставляем для совместимости)
    public Report() {
    }

    public Report(UUID id, String name, UUID sampleId, ReportStatus status, String ownerUsername, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.sampleId = sampleId;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UUID getSampleId() {
        return sampleId;
    }

    public UUID getExperimentId() {
        return experimentId;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
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
        Report report = (Report) o;
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Object getCreatedBy() {
        return null;
    }
}