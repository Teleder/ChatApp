package com.example.chatapp.Model.User;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.File.File;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class User extends BaseModel {
    private String id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String phone;
    private String email;
    private String bio;

    public Date getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Date lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    Date lastActiveAt;

    private File avatar;

    private String QR;
    private List<Block> list_block;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public User(String id, String firstName, String lastName, String phone, String email, String bio, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.bio = bio;
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public List<Block> getList_block() {
        return list_block;
    }

    public void setList_block(List<Block> list_block) {
        this.list_block = list_block;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Conservation> getConservations() {
        return conservations;
    }

    public void setConservations(List<Conservation> conservations) {
        this.conservations = conservations;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Contact> getList_contact() {
        return list_contact;
    }

    public void setList_contact(List<Contact> list_contact) {
        this.list_contact = list_contact;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private String password;
    private List<Conservation> conservations;
    private Role role = Role.USER;
    private List<Contact> list_contact;
    boolean isActive = false;

    public String getRole() {
        return role.name();
    }

    public enum Role {
        ADMIN,
        USER
    }
}
