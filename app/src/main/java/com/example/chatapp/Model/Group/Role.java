package com.example.chatapp.Model.Group;

import com.example.chatapp.Model.Permission.Permission;

import java.util.List;

public class Role {
    public Role() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    // theem unique vao
    String name;
    private List<Permission> permissions;
    public Role(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
}
