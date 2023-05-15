package com.example.chatapp.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.chatapp.Activities.MainActivity;
import com.example.chatapp.Retrofit.WebSocketManager;

public class WebSocketService extends Service {
    private WebSocketManager webSocketManager;

    @Override
    public void onCreate() {
        super.onCreate();
        webSocketManager = WebSocketManager.getInstance(null);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("CONNECT".equals(action)) {
                String userId = intent.getStringExtra("user_id");
                webSocketManager.connect(userId);
            } else if ("DISCONNECT".equals(action)) {
                webSocketManager.disconnect();
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        webSocketManager.disconnect();
        super.onDestroy();
    }
}
