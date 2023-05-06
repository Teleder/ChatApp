package com.example.chatapp.Model.User;

public class Contact {
    User user;
    Status status;

    public Contact(User user, Status status) {
        this.user = user;
        this.status = status;
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
