package com.example.chatapp.Model.Group;

import java.util.Date;


public class Block {
    String reason;
    private String user_id;
    private Date createAt = new Date();
    private Date updateAt = new Date();

    public Block(String user_id, String reason) {
        this.reason = reason;
        this.user_id = user_id;
    }
}
