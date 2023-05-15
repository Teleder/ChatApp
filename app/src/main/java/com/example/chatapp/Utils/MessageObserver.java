package com.example.chatapp.Utils;

public interface MessageObserver {
    void onMessageReceived(String message);

    void onPause();

    void onResume();
}