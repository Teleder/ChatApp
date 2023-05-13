package com.example.chatapp.Model.Group;

import com.example.chatapp.Model.Permission.Permission;

import java.util.List;

public class Role {
    // theem unique vao
    String name;
    private List<Permission> permissions;
    public Role(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
}
