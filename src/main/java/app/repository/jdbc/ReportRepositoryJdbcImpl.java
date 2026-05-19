package app.repository.jdbc;

import app.domain.Report;
import app.domain.ReportStatus;
import app.repository.ReportRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReportRepositoryJdbcImpl implements ReportRepository {

    @Override
    public Set<Report> findAll() {
        Set<Report> reports = new HashSet<>();
        String sql = "SELECT * FROM reports";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reports.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения отчетов: " + e.getMessage(), e);
        }
        return reports;
    }

    @Override
    public Report findById(UUID id) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска отчета: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Report save(Report report) {
        boolean exists = report.getId() != null && findById(report.getId()) != null;

        if (exists) {
            String sql = "UPDATE reports SET name = ?, status = ?, owner_id = ?, signed_by = ?, updated_at = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, report.getName());
                stmt.setString(2, report.getStatus().name());
                if (report.getOwnerId() != null) stmt.setInt(3, report.getOwnerId()); else stmt.setNull(3, Types.INTEGER);
                stmt.setString(4, report.getSignedBy());
                stmt.setTimestamp(5, Timestamp.from(report.getUpdatedAt()));
                stmt.setObject(6, report.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка обновления отчета: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO reports (name, sample_id, experiment_id, status, owner_username, owner_id, signed_by, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, report.getName());
                stmt.setObject(2, report.getSampleId());
                stmt.setObject(3, report.getExperimentId());
                stmt.setString(4, report.getStatus().name());
                stmt.setString(5, report.getOwnerUsername());
                if (report.getOwnerId() != null) stmt.setInt(6, report.getOwnerId()); else stmt.setNull(6, Types.INTEGER);
                stmt.setString(7, report.getSignedBy());
                stmt.setTimestamp(8, Timestamp.from(report.getCreatedAt()));
                stmt.setTimestamp(9, Timestamp.from(report.getUpdatedAt()));
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        report.setId(UUID.fromString(rs.getString("id")));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка сохранения отчета: " + e.getMessage(), e);
            }
        }
        return report;
    }

    @Override
    public void delete(Report report) {
        if (report == null || report.getId() == null) return;
        String sql = "DELETE FROM reports WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, report.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления отчета: " + e.getMessage(), e);
        }
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM reports";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка очистки отчетов: " + e.getMessage(), e);
        }
    }

    private Report mapRow(ResultSet rs) throws SQLException {
        Report r = new Report();
        r.setId(UUID.fromString(rs.getString("id")));
        r.setName(rs.getString("name"));

        String sampleId = rs.getString("sample_id");
        if (sampleId != null) r.setSampleId(UUID.fromString(sampleId));

        String expId = rs.getString("experiment_id");
        if (expId != null) r.setExperimentId(UUID.fromString(expId));

        r.setStatus(ReportStatus.valueOf(rs.getString("status")));
        r.setOwnerUsername(rs.getString("owner_username"));

        int ownerId = rs.getInt("owner_id");
        r.setOwnerId(rs.wasNull() ? null : ownerId);

        r.setSignedBy(rs.getString("signed_by"));
        r.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        r.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
        return r;
    }
}