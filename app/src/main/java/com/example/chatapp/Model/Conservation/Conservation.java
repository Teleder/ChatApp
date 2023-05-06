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

}

