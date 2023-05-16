package com.example.chatapp.Retrofit;

import static com.example.chatapp.Utils.CONSTS.BASEURL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.chatapp.Activities.ChatActivity;
import com.example.chatapp.Dtos.PayloadMessage;
import com.example.chatapp.Dtos.SocketPayload;
import com.example.chatapp.R;
import com.example.chatapp.Utils.CONSTS;
import com.example.chatapp.Utils.MessageObserver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebSocketManager {
    private static WebSocketManager instance;
    private StompClient stompClient;
    private List<MessageObserver> observers = new ArrayList<>();
    private String iduser;
    private Context context;
    private SharedPrefManager sharedPrefManager;

    @SuppressLint("CheckResult")
    private WebSocketManager(Context context) {
        sharedPrefManager = SharedPrefManager.getInstance(context);
        this.context = context.getApplicationContext();
        String websocketUrl = "ws://" + BASEURL + "/websocket/websocket";
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, websocketUrl);
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    break;
                case ERROR:
                case CLOSED:
                case FAILED_SERVER_HEARTBEAT:
                    reconnect();
                    break;
            }
        }, throwable -> {
            // Trình xử lý lỗi
            throwable.printStackTrace();
        });
    }

    private void notifyObservers(String message) {
        for (MessageObserver observer : observers) {
            observer.onMessageReceived(message);
        }
        Gson gson = new Gson();
        Type socketPayloadType = new TypeToken<SocketPayload<Object>>() {
        }.getType();
        SocketPayload socketPayload = gson.fromJson(message, socketPayloadType);
        if (socketPayload == null)
            return;
        if (context != null && socketPayload.getType() != null && (socketPayload.getType().equals(CONSTS.MESSAGE_PRIVATE) || socketPayload.getType().equals(CONSTS.MESSAGE_GROUP))) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "20022023")
                    .setSmallIcon(R.drawable.circle)
                    .setContentTitle("New message")
                    .setContentText("You have a new message")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            // Gửi thông báo với ID cố định
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.notify(Integer.parseInt("20022023"), notificationBuilder.build());
        }
    }

    private void reconnect() {
        if (stompClient != null && !stompClient.isConnected()) {
            stompClient.disconnect();
            connect(this.iduser);
           subscribeToPrivateMessages(sharedPrefManager.getUser().getId());
            for (String a : sharedPrefManager.getListGroupId()) {
                subscribeToGroupMessages(a);
            }
        }
    }

    public static synchronized WebSocketManager getInstance(Context context) {
        if (instance == null) {
            instance = new WebSocketManager(context);
        }
        return instance;
    }

    public void connect(String iduser) {
        this.iduser = iduser;
        if (stompClient == null || !stompClient.isConnected()) {
            List<StompHeader> header = new ArrayList<>();
            header.add(new StompHeader("user-id", iduser));
            stompClient.connect(header);
        }
    }

    public void disconnect() {
        if (stompClient != null && stompClient.isConnected()) {
            stompClient.disconnect();
        }
    }

    @SuppressLint("CheckResult")
    public void subscribeToPrivateMessages(String recipientId) {
        String subscriptionPath = "/messages/user." + recipientId;
        stompClient.topic(subscriptionPath).subscribe(response -> {
            String message = response.getPayload();
            notifyObservers(message);
        }, error -> {
            // Handle onError (failure) case here
            error.printStackTrace();
        });
    }

    @SuppressLint("CheckResult")
    public void subscribeToGroupMessages(String groupId) {
        String subscriptionPath = "/messages/group." + groupId;
        stompClient.topic(subscriptionPath).subscribe(response -> {
            String message = response.getPayload();
            notifyObservers(message);
        }, error -> {
            // Handle onError (failure) case here
            error.printStackTrace();
        });
    }

    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }


    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }

}