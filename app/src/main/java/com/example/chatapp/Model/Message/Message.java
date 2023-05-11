package com.example.chatapp.Model.Message;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.Model.User.User;
import com.example.chatapp.Utils.CONSTS;

import java.util.ArrayList;
import java.util.List;

public class Message extends BaseModel {
    private String id;
    private String content;
    private String code;
    private String TYPE = CONSTS.MESSAGE_PRIVATE;
    private User user_send;
    private User user_receive;
    private Group group;
    private List<Emotion> list_emotion = new ArrayList<>();
    private List<HistoryChange> historyChanges = new ArrayList<>();

    public Message(String content, String code, String TYPE){
        this.code =code;
        this.content = content;
        this.TYPE = TYPE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public User getUser_send() {
        return user_send;
    }

    public void setUser_send(User user_send) {
        this.user_send = user_send;
    }

    public User getUser_receive() {
        return user_receive;
    }

    public void setUser_receive(User user_receive) {
        this.user_receive = user_receive;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Emotion> getList_emotion() {
        return list_emotion;
    }

    public void setList_emotion(List<Emotion> list_emotion) {
        this.list_emotion = list_emotion;
    }

    public List<HistoryChange> getHistoryChanges() {
        return historyChanges;
    }

    public void setHistoryChanges(List<HistoryChange> historyChanges) {
        this.historyChanges = historyChanges;
    }
}
