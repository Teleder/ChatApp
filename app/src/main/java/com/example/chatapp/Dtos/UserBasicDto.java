package com.example.chatapp.Dtos;

import com.example.chatapp.Model.File.File;
import java.util.Date;

public class UserBasicDto {
    public Date createAt;
    public Date updateAt;
    private String id;
    private String firstName;
    private String displayName;
    private String lastName;
    private String phone;
    private String bio;
    private File avatar;
    public boolean isActive ;
    Date lastActiveAt;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Date lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }


    public UserBasicDto(Date createAt, Date updateAt, String id, String firstName, String displayName, String lastName, String phone, String bio, File avatar) {
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.id = id;
        this.firstName = firstName;
        this.displayName = displayName;
        this.lastName = lastName;
        this.phone = phone;
        this.bio = bio;
        this.avatar = avatar;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
    }
}
