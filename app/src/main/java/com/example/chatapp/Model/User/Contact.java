package com.example.chatapp.Model.User;

public class Contact {
    String userId;
    Status status;
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Contact(String userId, Status status) {
        this.userId = userId;
        this.status = status;
    }
    public Contact() {

    }
    public enum Status {
        ACCEPT,
        WAITING,
        REQUEST
    }
}
