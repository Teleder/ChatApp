package com.example.chatapp.Dtos;

import com.google.type.DateTime;

public class UserDto extends UpdateUserDto {
    public DateTime createAt;
    public DateTime updateAt;
    private String id;

    public UserDto(String firstName, String lastName, String phone, String email, String bio, String password) {
        super(firstName, lastName, phone, email, bio, password);
    }
}
