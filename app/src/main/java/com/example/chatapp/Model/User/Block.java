package com.example.chatapp.Model.User;
import java.util.Date;

public class Block {
    String reason;
    private User user;
    private Date createAt = new Date();
    private Date updateAt = new Date();

    public Block(User user, String reason) {
        this.user = user;
        this.reason = reason;
    }
}
