package com.example.chatapp.Model.User;

public class Contact {
    User user;
    Status status;
    String userId;

    public Contact(User user, Status status, String userId) {
        this.user = user;
        this.status = status;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        ACCEPT,
        WAITING,
        REQUEST
    }
}
