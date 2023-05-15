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
    private   User user;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getAddedByUserId() {
        return addedByUserId;
    }

    public void setAddedByUserId(String addedByUserId) {
        this.addedByUserId = addedByUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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