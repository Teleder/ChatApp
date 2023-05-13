package com.example.chatapp.Model.Group;

import com.example.chatapp.Model.User.User;

import java.util.Date;

public class Member {
    Role role;
    Status status;
    private String userId;
    private Date createAt = new Date();
    private Date updateAt = new Date();
    private String addedByUserId;
    User user;
    public Member(String userId, String addedByUserId, Status status) {
        this.userId = userId;
        this.addedByUserId = addedByUserId;
        this.status = status;
    }
    public Member(User user, String addedByUserId, Status status) {
        this.user = user;
        this.addedByUserId = addedByUserId;
        this.status = status;
    }
    public enum Status {
        ACCEPT,
        WAITING,
        REQUEST
    }
}