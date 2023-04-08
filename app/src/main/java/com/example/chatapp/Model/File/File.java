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

    public File(String name, FileCategory file_type, double file_size, String url, String code) {
        this.name = name;
        this.file_size = file_size;
        this.file_type = file_type;
        this.url = url;
        this.code = code;
    }
}
