package com.example.chatapp.Dtos;


import com.example.chatapp.Model.Group.Block;
import com.example.chatapp.Model.Group.Member;
import com.example.chatapp.Model.Group.Role;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupDto {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Block> getBlock_list() {
        return block_list;
    }

    public void setBlock_list(List<Block> block_list) {
        this.block_list = block_list;
    }

    public List<Message> getPinMessage() {
        return pinMessage;
    }

    public void setPinMessage(List<Message> pinMessage) {
        this.pinMessage = pinMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserBasicDto getUser_own() {
        return user_own;
    }

    public void setUser_own(UserBasicDto user_own) {
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

    public List<Member> getMember() {
        return member;
    }

    public void setMember(List<Member> member) {
        this.member = member;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getAvatarGroup() {
        return avatarGroup;
    }

    public void setAvatarGroup(String avatarGroup) {
        this.avatarGroup = avatarGroup;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String code;
    String QR;
    List<Role> roles = new ArrayList<>();
    List<Member> members = new ArrayList<>();
    List<Block> block_list = new ArrayList<>();
    private List<Message> pinMessage = new ArrayList<>();
    private String id;
    private UserBasicDto user_own;
    boolean isDeleted;
    private Date createAt;
    private Date updateAt;
    List<Member> member = new ArrayList<>();
    boolean isPublic;
    private String avatarGroup;
    private String bio;
    private String name;
}
