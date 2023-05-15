package com.example.chatapp.Dtos;

import com.example.chatapp.Model.Permission.Action;

import java.util.List;

public class RoleDto {
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Action> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Action> permissions) {
        this.permissions = permissions;
    }

    String roleName;
    List<Action> permissions;
}