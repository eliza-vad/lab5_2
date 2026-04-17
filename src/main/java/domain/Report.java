package domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Report {

    @JacksonXmlProperty(isAttribute = true)
    private UUID id;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private UUID sampleId;

    @JacksonXmlProperty(isAttribute = true)
    private UUID experimentId;

    @JacksonXmlProperty(isAttribute = true)
    private ReportStatus status;

    @JacksonXmlProperty(isAttribute = true)
    private String ownerUsername;

    @JacksonXmlProperty(isAttribute = true)
    private String signedBy;

    @JacksonXmlProperty(isAttribute = true)
    private Instant createdAt;

    @JacksonXmlProperty(isAttribute = true)
    private Instant updatedAt;

    @JacksonXmlElementWrapper(localName = "lines")
    @JacksonXmlProperty(localName = "line")
    private List<ReportLine> lines;

    // Пустой конструктор (обязателен для Jackson)
    public Report() {
        this.lines = new ArrayList<>();
    }

    // Конструктор с основными полями
    public Report(UUID id, String name, UUID sampleId, ReportStatus status,
                  String ownerUsername, Instant createdAt, Instant updatedAt) {
        this();
        this.id = id;
        this.name = name;
        this.sampleId = sampleId;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Полный конструктор
    public Report(UUID id, String name, UUID sampleId, UUID experimentId,
                  ReportStatus status, String ownerUsername, String signedBy,
                  Instant createdAt, Instant updatedAt, List<ReportLine> lines) {
        this.id = id;
        this.name = name;
        this.sampleId = sampleId;
        this.experimentId = experimentId;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.signedBy = signedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lines = lines != null ? lines : new ArrayList<>();
    }

    // Геттеры и сеттеры
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

    public List<ReportLine> getLines() {
        return lines;
    }

    public void setLines(List<ReportLine> lines) {
        this.lines = lines != null ? lines : new ArrayList<>();
    }

    // Вспомогательные методы для работы с линиями
    public void addLine(ReportLine line) {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(line);
        line.setReportId(this.id);
    }

    public void removeLine(ReportLine line) {
        if (lines != null) {
            lines.remove(line);
        }
    }

    public void removeLine(UUID lineId) {
        if (lines != null) {
            lines.removeIf(line -> line.getId().equals(lineId));
        }
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

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", linesCount=" + (lines != null ? lines.size() : 0) +
                '}';
    }
}