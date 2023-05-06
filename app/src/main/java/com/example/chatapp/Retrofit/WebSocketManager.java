package com.example.chatapp.Retrofit;

import static com.example.chatapp.Utils.CONSTS.BASEURL;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebSocketManager {
    private static WebSocketManager instance;
    private StompClient stompClient;
    private WebSocketManager() {
        String websocketUrl = "ws://"+BASEURL+"/websocket";
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, websocketUrl);
    }

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void connect(String iduser) {
        List<StompHeader> header= new ArrayList<>();
        header.add(new StompHeader("user-id", iduser));
        stompClient.connect(header);
    }

    public void disconnect() {
        stompClient.disconnect();
    }

    public void subscribeToPrivateMessages(String recipientId) {
        String subscriptionPath = "/messages/user." + recipientId;
        stompClient.topic(subscriptionPath).subscribe(response -> {
            String message = response.getPayload();
        }, (@SuppressLint("CheckResult") Throwable error) -> {
            // Handle onError (failure) case here
            error.printStackTrace();
        });
    }
    public void subscribeToGroupMessages(String groupId) {
        String subscriptionPath = "/messages/group." + groupId;
        stompClient.topic(subscriptionPath).subscribe(response -> {
            String message = response.getPayload();
        }, (@SuppressLint("CheckResult") Throwable error) -> {
            // Handle onError (failure) case here
            error.printStackTrace();
        });
    }

    public void send(String destination, String message) {
        stompClient.send(destination, message);
    }

}