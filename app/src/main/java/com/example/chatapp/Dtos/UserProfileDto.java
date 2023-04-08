package com.example.chatapp.Dtos;

import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.Model.User.Block;
import com.example.chatapp.Model.User.User;

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
    private List<Block> list_block;
    private List<Conservation> conservation;
    private User.Role role;

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

    public List<Block> getList_block() {
        return list_block;
    }

    public void setList_block(List<Block> list_block) {
        this.list_block = list_block;
    }

    public List<Conservation> getConservation() {
        return conservation;
    }

    public void setConservation(List<Conservation> conservation) {
        this.conservation = conservation;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}
