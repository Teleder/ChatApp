package com.example.chatapp.Dtos;

public class SocketPayload<T> {
    T data;
    String type;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SocketPayload(T data, String type) {
        this.data = data;
        this.type = type;
    }
    public static <T> SocketPayload create(T data, String type) {
        return new SocketPayload(data, type);
    }
}

