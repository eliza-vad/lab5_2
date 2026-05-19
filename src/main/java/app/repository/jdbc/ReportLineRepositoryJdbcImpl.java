package app.repository.jdbc;

import app.domain.MeasurementParam;
import app.domain.ReportLine;
import app.repository.ReportLineRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReportLineRepositoryJdbcImpl implements ReportLineRepository {

    @Override
    public Set<ReportLine> findAll() {
        Set<ReportLine> lines = new HashSet<>();
        String sql = "SELECT * FROM report_lines";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lines.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки строк: " + e.getMessage(), e);
        }
        return lines;
    }

    @Override
    public Set<ReportLine> findByReportId(UUID reportId) {
        Set<ReportLine> lines = new HashSet<>();
        String sql = "SELECT * FROM report_lines WHERE report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, reportId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lines.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки строк отчета: " + e.getMessage(), e);
        }
        return lines;
    }

    @Override
    public void clear() {

    }

    @Override
    public ReportLine findById(UUID id) {
        String sql = "SELECT * FROM report_lines WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска строки: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public ReportLine save(ReportLine line) {
        boolean exists = line.getId() != null && findById(line.getId()) != null;

        if (exists) {
            String sql = "UPDATE report_lines SET param = ?, value = ?, unit = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, line.getParam().name());
                stmt.setDouble(2, line.getValue());
                stmt.setString(3, line.getUnit());
                stmt.setObject(4, line.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка обновления строки: " + e.getMessage(), e);
            }
        } else {
            String sql = "INSERT INTO report_lines (report_id, param, value, unit) VALUES (?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setObject(1, line.getReportId());
                stmt.setString(2, line.getParam().name());
                stmt.setDouble(3, line.getValue());
                stmt.setString(4, line.getUnit());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        line.setId(UUID.fromString(rs.getString("id")));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка сохранения строки: " + e.getMessage(), e);
            }
        }
        return line;
    }
    @Override
    public void delete(ReportLine line) {
        if (line == null || line.getId() == null) return;
        String sql = "DELETE FROM report_lines WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, line.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления строки: " + e.getMessage(), e);
        }
    }

    private ReportLine mapRow(ResultSet rs) throws SQLException {
        ReportLine line = new ReportLine();
        line.setId(UUID.fromString(rs.getString("id")));
        line.setReportId(UUID.fromString(rs.getString("report_id")));
        line.setParam(MeasurementParam.valueOf(rs.getString("param")));
        line.setValue(rs.getDouble("value"));
        line.setUnit(rs.getString("unit"));
        return line;
    }
}