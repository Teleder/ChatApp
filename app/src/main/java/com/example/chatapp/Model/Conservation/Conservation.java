package com.example.chatapp.Model.Conservation;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.User;

import java.util.List;
import java.util.UUID;

public class Conservation extends BaseModel {
    boolean status = true;
    private String id;
    private String code = UUID.randomUUID().toString();
    private Type type = Type.PERSONAL;
    private List<PinMessage> pinMessage;
    Message lastMessage;
    private User user_1;
    private User user_2;
    private Group group;
    public Conservation(User user_1, User user_2, Group group) {
        this.user_2 = user_2;
        this.user_1 = user_1;
        this.group = group;
    }

    public Conservation() {
    }

    public Conservation(Group group, String code) {
        this.group = group;
        this.code = code;
    }

    public enum Type {
        PERSONAL,
        GROUP
    }
    public class PinMessage {
        User pinBy;
        private List<Message> pinMessage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<PinMessage> getPinMessage() {
        return pinMessage;
    }

    public void setPinMessage(List<PinMessage> pinMessage) {
        this.pinMessage = pinMessage;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public User getUser_1() {
        return user_1;
    }

    public void setUser_1(User user_1) {
        this.user_1 = user_1;
    }

    public User getUser_2() {
        return user_2;
    }

    public void setUser_2(User user_2) {
        this.user_2 = user_2;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}

