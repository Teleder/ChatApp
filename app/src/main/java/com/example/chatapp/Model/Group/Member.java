package com.example.chatapp.Model.Group;

import com.example.chatapp.Model.User.User;

import java.util.Date;

public class Member {
    Role role;
    Status status;
    private String userId;
    private Date createAt = new Date();
    private Date updateAt = new Date();
    private User addedBy;

    User user;
    public Member(String userId, User addedBy, Status status) {
        this.userId = userId;
        this.addedBy = addedBy;
        this.status = status;
    }
    public Member(User user, User addedBy, Status status) {
        this.user = user;
        this.addedBy = addedBy;
        this.status = status;
    }
    public enum Status {
        ACCEPT,
        WAITING,
        REQUEST
    }
}