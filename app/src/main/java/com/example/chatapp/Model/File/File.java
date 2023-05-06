package com.example.chatapp.Model.File;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.User.User;

public class File extends BaseModel {

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
