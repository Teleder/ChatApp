package com.example.chatapp.Dtos;

import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.Model.User.Block;

import java.util.List;

public class UpdateUserDto extends CreateUserDto{
    private File avatar;
    private File QR;
    private List<Block> list_block;
    private List<Conservation> conservation;

    public UpdateUserDto(String firstName, String lastName, String phone, String email, String bio, String password) {
        super(firstName, lastName, phone, email, bio, password);
    }
}
