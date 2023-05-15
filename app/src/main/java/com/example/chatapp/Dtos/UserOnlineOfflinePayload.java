package com.example.chatapp.Dtos;

public class UserOnlineOfflinePayload {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    String id;
    Boolean active;

    public UserOnlineOfflinePayload(String id, boolean active) {
        this.id = id;
        this.active = active;
    }
}

