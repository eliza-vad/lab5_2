package app.service;

import app.domain.User;

public class CurrentSession {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void clear() {
        this.currentUser = null;
    }

    public boolean isAuthorized() {
        return currentUser != null;
    }
}