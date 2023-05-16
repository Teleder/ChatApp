package com.example.chatapp.Dtos;

import com.example.chatapp.Model.File.File;

public class UpdateInfoUserDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String bio;
    private File avatar;

    public UpdateInfoUserDto(String firstName, String lastName, String phone, String email, String bio, File avatar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.bio = bio;
        this.avatar = avatar;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
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
}
