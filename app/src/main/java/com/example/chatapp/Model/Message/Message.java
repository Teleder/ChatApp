package com.example.chatapp.Model.Message;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.Model.User.User;
import com.example.chatapp.Utils.CONSTS;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message extends BaseModel {
    private String id;
    private String content;
    private String code;
    private String type;
    private String userId_send;
    private String userId_receive;
    private String groupId;
    private File file;
    String idParent = null;
    Date readAt = null;
    Date deliveredAt =null;

    public Message(String id, String content, String code, String type, String userId_send, String userId_receive, String groupId, File file, String idParent, Date readAt, Date deliveredAt, List<Message> replyMessages, List<Emotion> list_emotion, List<HistoryChange> historyChanges) {
        this.id = id;
        this.content = content;
        this.code = code;
        this.type = type;
        this.userId_send = userId_send;
        this.userId_receive = userId_receive;
        this.groupId = groupId;
        this.file = file;
        this.idParent = idParent;
        this.readAt = readAt;
        this.deliveredAt = deliveredAt;
        this.replyMessages = replyMessages;
        this.list_emotion = list_emotion;
        this.historyChanges = historyChanges;
    }

    public String getUserId_send() {
        return userId_send;
    }

    public void setUserId_send(String userId_send) {
        this.userId_send = userId_send;
    }

    public String getUserId_receive() {
        return userId_receive;
    }

    public void setUserId_receive(String userId_receive) {
        this.userId_receive = userId_receive;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getIdParent() {
        return idParent;
    }

    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    public Date getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Date deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public List<Message> getReplyMessages() {
        return replyMessages;
    }

    public void setReplyMessages(List<Message> replyMessages) {
        this.replyMessages = replyMessages;
    }

    List<Message> replyMessages = new ArrayList<>();
    private List<Emotion> list_emotion = new ArrayList<>();
    private List<HistoryChange> historyChanges = new ArrayList<>();

    public Message(String content, String code, String type){
        this.code =code;
        this.content = content;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
