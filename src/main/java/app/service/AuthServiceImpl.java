package app.service;

import app.domain.User;
import app.repository.UserRepository;
import app.util.PasswordHasher;

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CurrentSession currentSession;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentSession = new CurrentSession();
    }

    @Override
    public boolean register(String login, String password) {
        validate(login, password);

        if (userRepository.existsByLogin(login)) {
            return false;
        }

        Integer newId = userRepository.nextId();
        String passwordHash = PasswordHasher.sha256(password);
        User user = new User(newId, login, passwordHash);

        userRepository.save(user);
        return true;
    }

    @Override
    public boolean login(String login, String password) {
        validate(login, password);

        User user = userRepository.findByLogin(login);
        if (user == null) {
            return false;
        }

        String inputHash = PasswordHasher.sha256(password);
        if (!inputHash.equals(user.getPasswordHash())) {
            return false;
        }

        currentSession.setCurrentUser(user);
        return true;
    }

    @Override
    public void logout() {
        currentSession.clear();
    }

    @Override
    public User getCurrentUser() {
        return currentSession.getCurrentUser();
    }

    @Override
    public boolean isAuthorized() {
        return currentSession.isAuthorized();
    }

    private void validate(String login, String password) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
    }
}