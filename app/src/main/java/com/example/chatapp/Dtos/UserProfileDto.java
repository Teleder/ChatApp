package com.example.chatapp.Dtos;

import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.Model.User.Block;
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.Model.User.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserProfileDto {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String bio;
    private File avatar;
    private File QR;
    private String displayName;
    private List<Block> blocks = new ArrayList<>();
    private List<String>conservations;
    private User.Role role;
    private List<Contact> list_contact = new ArrayList<>();
    private Date lastActiveAt;

    public UserProfileDto(String id, String firstName, String lastName, String phone, String email, String bio, File avatar, File QR, String displayName, List<Block> blocks,
                          List<String> conservations, User.Role role, List<Contact> list_contact, Date lastActiveAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.bio = bio;
        this.avatar = avatar;
        this.QR = QR;
        this.displayName = displayName;
        this.blocks = blocks;
        this.conservations = conservations;
        this.role = role;
        this.list_contact = list_contact;
        this.lastActiveAt = lastActiveAt;
    }

    public Date getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Date lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public List<Contact> getList_contact() {
        return list_contact;
    }

    public void setList_contact(List<Contact> list_contact) {
        this.list_contact = list_contact;
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

    public File getQR() {
        return QR;
    }

    public void setQR(File QR) {
        this.QR = QR;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<String> getConservations() {
        return conservations;
    }

    public void setConservations(List<String> conservations) {
        this.conservations = conservations;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}
