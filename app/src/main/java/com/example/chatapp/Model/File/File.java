package com.example.chatapp.Model.File;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.User.User;

public class File extends BaseModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileCategory getFile_type() {
        return file_type;
    }

    public void setFile_type(FileCategory file_type) {
        this.file_type = file_type;
    }

    public double getFile_size() {
        return file_size;
    }

    public void setFile_size(double file_size) {
        this.file_size = file_size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser_own() {
        return user_own;
    }

    public void setUser_own(User user_own) {
        this.user_own = user_own;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    String name;
    FileCategory file_type;
    double file_size;
    String url;
    String code;
    private String id;
    private User user_own;
    private boolean deleted;

    public File(String name, FileCategory file_type, double file_size, String url, String code) {
        this.name = name;
        this.file_size = file_size;
        this.file_type = file_type;
        this.url = url;
        this.code = code;
    }

    public File(String name, FileCategory file_type, double file_size, String url, String code, String id, User user_own, boolean deleted) {
        this.name = name;
        this.file_type = file_type;
        this.file_size = file_size;
        this.url = url;
        this.code = code;
        this.id = id;
        this.user_own = user_own;
        this.deleted = deleted;
    }

    public File() {
    }
}
