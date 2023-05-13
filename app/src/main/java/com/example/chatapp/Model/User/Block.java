package com.example.chatapp.Model.User;
import java.util.Date;

public class Block {
    String reason;
    private String userId;
    private Date createAt = new Date();
    private Date updateAt = new Date();

    public Block(String userId, String reason) {
        this.userId = userId;
        this.reason = reason;
    }
}
