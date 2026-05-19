package app.repository;

import app.domain.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String FILE_PATH = "users.csv";
    private final List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    Integer id = Integer.parseInt(parts[0]);
                    String login = parts[1];
                    String passwordHash = parts[2];

                    users.add(new User(id, login, passwordHash));
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла пользователей: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.println(user.getId() + "," + user.getLogin() + "," + user.getPasswordHash());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла пользователей: " + e.getMessage());
        }
    }

    public boolean existsByLogin(String login) {
        return users.stream().anyMatch(u -> u.getLogin().equals(login));
    }

    public User findByLogin(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }

    public Integer nextId() {
        return users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }

    public void save(User user) {
        boolean updated = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                updated = true;
                break;
            }
        }
        if (!updated) {
            users.add(user);
        }
        saveToFile();
    }
}