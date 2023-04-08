package com.example.chatapp.Model.User;

public class Contact {
    User user;
    Status status;

    public Contact(User user, Status status) {
        this.user = user;
        this.status = status;
    }

    public enum Status {
        ACCEPT,
        WAITING,
        REQUEST
    }
}
