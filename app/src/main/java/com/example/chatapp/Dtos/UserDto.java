package com.example.chatapp.Dtos;

import com.example.chatapp.Model.File.File;
import com.google.type.DateTime;

public class UserDto extends UpdateUserDto {
    public DateTime createAt;
    public DateTime updateAt;
    private String id;
    private File qr;
    public UserDto(String firstName, String lastName, String phone, String email, String bio, String password, File avatar) {
        super(firstName, lastName, phone, email, bio, password, avatar);
    }
}
