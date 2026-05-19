package app.repository.jdbc;

import app.domain.User;
import app.repository.UserRepository;

import java.sql.*;

public class UserRepositoryJdbcImpl extends UserRepository {

    public UserRepositoryJdbcImpl() {}

    @Override
    public boolean existsByLogin(String login) {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка проверки логина в БД", e);
        }
    }

    @Override
    public User findByLogin(String login) {
        String sql = "SELECT id, login, password_hash FROM users WHERE login = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password_hash")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя", e);
        }
    }

    @Override
    public Integer nextId() {
        return null;
    }

    @Override
    public void save(User user) {
        if (user.getId() == null || user.getId() == 0) {
            String insertSql = "INSERT INTO users (login, password_hash) VALUES (?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getPasswordHash());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                        System.out.println("Пользователь сохранен. Сгенерированный БД ID: " + user.getId());
                    } else {
                        throw new RuntimeException("Создание пользователя провалилось, БД не вернула ID.");
                    }
                }
            } catch (SQLException e) {
                if ("23505".equals(e.getSQLState())) {
                    throw new RuntimeException("Пользователь с таким логином уже существует.");
                }
                throw new RuntimeException("Ошибка сохранения в базу: " + e.getMessage(), e);
            }
        } else {
            String updateSql = "UPDATE users SET login = ?, password_hash = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getPasswordHash());
                stmt.setInt(3, user.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка обновления: " + e.getMessage(), e);
            }
        }
    }
}