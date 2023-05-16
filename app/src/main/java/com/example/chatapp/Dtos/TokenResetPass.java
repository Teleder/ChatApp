package com.example.chatapp.Dtos;

public class TokenResetPass {
    String token;

    public TokenResetPass(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
