package com.example.chatapp.Model.Conservation;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.User;
import com.example.chatapp.Utils.CONSTS;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Conservation extends BaseModel {
    public void setType(String type) {
        this.type = type;
    }

    public String getUserId_1() {
        return userId_1;
    }

    public void setUserId_1(String userId_1) {
        this.userId_1 = userId_1;
    }

    public String getUserId_2() {
        return userId_2;
    }

    public void setUserId_2(String userId_2) {
        this.userId_2 = userId_2;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    boolean status = true;
    private String id;
    private String code = UUID.randomUUID().toString();
    private String type = CONSTS.MESSAGE_PRIVATE;
    private List<PinMessage> pinMessage = new ArrayList<>();
    Message lastMessage;
    private String userId_1;
    private String userId_2;
    private String groupId;
    private User user_1;
    private User user_2;
    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getType() {
        return type;
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

    public Conservation(String userId_1, String userId_2, String groupIdId) {
        this.userId_2 = userId_2;
        this.userId_1 = userId_1;
        this.groupId = groupIdId;
    }

    public Conservation() {
    }

    public Conservation(String groupId, String code) {
        this.groupId = groupId;
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

}

