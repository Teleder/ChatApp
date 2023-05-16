package com.example.chatapp.Model.Group;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
public class Group extends BaseModel {
    String name;
    String bio;
    String QR;
    Set<Role> roles = new HashSet<>();
    Set<Member> members = new HashSet<>();
    boolean isPublic;
    Set<Block> block_list = new HashSet<>();
    private String id;
    private String code = UUID.randomUUID().toString();
    private List<Message> pinMessage;
    private User user_own;
    private String avatarGroup;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Message> getPinMessage() {
        return pinMessage;
    }

    public void setPinMessage(List<Message> pinMessage) {
        this.pinMessage = pinMessage;
    }

    public User getUser_own() {
        return user_own;
    }

    public void setUser_own(User user_own) {
        this.user_own = user_own;
    }

    public String getAvatarGroup() {
        return avatarGroup;
    }

    public void setAvatarGroup(String avatarGroup) {
        this.avatarGroup = avatarGroup;
    }


}
