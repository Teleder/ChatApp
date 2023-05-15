package com.example.chatapp.Model.Permission;

import com.example.chatapp.Model.BaseModel;

public class Permission extends BaseModel {
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Action action;
    private String id;
}
