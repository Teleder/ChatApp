package com.example.chatapp.Dtos;


import com.example.chatapp.Model.Group.Block;
import com.example.chatapp.Model.Group.Role;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GroupDto extends UpdateGroupDto {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public Set<Block> getBlock_list() {
        return block_list;
    }

    public void setBlock_list(Set<Block> block_list) {
        this.block_list = block_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Message> getPinMessage() {
        return pinMessage;
    }

    public void setPinMessage(Set<Message> pinMessage) {
        this.pinMessage = pinMessage;
    }

    public User getUser_own() {
        return user_own;
    }

    public void setUser_own(User user_own) {
        this.user_own = user_own;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    private String code;
    Set<Role> roles = new HashSet<>();
    String QR;
    Set<Block> block_list = new HashSet<>();
    private String id;
    private Set<Message> pinMessage = new HashSet<>();
    private User user_own;
    boolean isDeleted;
    private Date createAt;
    private Date updateAt;
}
