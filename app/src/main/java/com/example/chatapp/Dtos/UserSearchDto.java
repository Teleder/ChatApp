package com.example.chatapp.Dtos;

import com.example.chatapp.Model.File.File;
import com.google.type.DateTime;

public class UserSearchDto {
    public DateTime createAt;
    public DateTime updateAt;
    private String id;
    private String firstName;
    private String displayName;
    private String lastName;
    private String phone;
    private String bio;
    private File avatar;

    public UserSearchDto(DateTime createAt, DateTime updateAt, String id, String firstName, String displayName, String lastName, String phone, String bio, File avatar) {
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

    public DateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

    public DateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(DateTime updateAt) {
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
