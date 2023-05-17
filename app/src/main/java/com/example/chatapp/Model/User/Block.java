package com.example.chatapp.Model.User;
import java.util.Date;

public class Block {
    String reason;
    private String userId;
    private Date createAt = null;
    private Date updateAt  = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Block(String userId, String reason) {
        this.userId = userId;
        this.reason = reason;
    }
}
