package com.example.chatapp.Model.Message;

import com.example.chatapp.Model.User.User;

import java.util.Date;

public class Emotion {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
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

    public Emotion(String userId, String emotion, Date createAt, Date updateAt) {
        this.userId = userId;
        this.emotion = emotion;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Emotion() {

    }

    private String userId;
    private String emotion;
    private Date createAt = new Date();
    private Date updateAt = new Date();
}

