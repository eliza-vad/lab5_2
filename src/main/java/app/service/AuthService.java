package app.service;

import app.domain.User;

public interface AuthService {

    boolean register(String login, String password);

    boolean login(String login, String password);

    void logout();

    User getCurrentUser();

    boolean isAuthorized();
}