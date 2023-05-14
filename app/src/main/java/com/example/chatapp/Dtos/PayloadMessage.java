package com.example.chatapp.Dtos;

import com.example.chatapp.Model.File.File;

public class PayloadMessage {
    public PayloadMessage(String content, String code, String type, String parentMessageId, String group, File file) {
        this.content = content;
        this.code = code;
        this.type = type;
        this.parentMessageId = parentMessageId;
        this.group = group;
        this.file = file;
    }
    private String content;
    private String code;
    private String type;
    private String parentMessageId;
    private String group;
    private File file = null;

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

    public String getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(String parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
