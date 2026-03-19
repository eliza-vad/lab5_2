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

    public void setName(String name) {
        this.name = name;
    }

    public UUID getSampleId() {
        return sampleId;
    }

    public void setSampleId(UUID sampleId) {
        this.sampleId = sampleId;
    }

    public UUID getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(UUID experimentId) {
        this.experimentId = experimentId;
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

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
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
        Report report = (Report) o;
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}