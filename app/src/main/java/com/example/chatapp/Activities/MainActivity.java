package com.example.chatapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapter.LastMessageAdapter;
import com.example.chatapp.Dtos.UserOnlineOfflinePayload;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Retrofit.WebSocketManager;
import com.example.chatapp.Utils.MessageObserver;
import com.example.chatapp.Utils.WebSocketService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements MessageObserver {
    SharedPrefManager sharedPrefManager;
    ImageView imageViewProfile;
    WebSocketManager webSocketManager;
    RecyclerView lastMessages;
    LastMessageAdapter lastMessageAdapter;
    List<Conservation> listConservations;
    private RetrofitClient retrofitClient;
    private boolean isWebSocketConnected = false;
    private TokenManager tokenManager;
    private Retrofit retrofit;

    @Override
    protected void onStart() {

        super.onStart();
        webSocketManager = WebSocketManager.getInstance(MainActivity.this);
        UserProfileDto userProfileDto = sharedPrefManager.getUser();
        Intent intent = new Intent(this, WebSocketService.class);
        intent.setAction("CONNECT");
        intent.putExtra("user_id", userProfileDto.getId());
        startService(intent);
        if (!isWebSocketConnected) {
            webSocketManager.connect(userProfileDto.getId());
            webSocketManager.subscribeToPrivateMessages(userProfileDto.getId());
            Log.d("currentId", userProfileDto.getId());
            for (Conservation a : userProfileDto.getConservations()) {
                if (a.getGroupId() != null && !a.getGroupId().equals("")) {
                    webSocketManager.subscribeToGroupMessages(a.getCode());
                }
            }
            isWebSocketConnected = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        UserProfileDto userProfileDto = sharedPrefManager.getUser();
        lastMessages = findViewById(R.id.conversationRecyclerView);
        if (userProfileDto.getConservations().size() > 0) {
            lastMessages.setVisibility(View.VISIBLE);
            listConservations = userProfileDto.getConservations();
            lastMessageAdapter = new LastMessageAdapter(MainActivity.this, listConservations);
            lastMessages.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            lastMessages.setLayoutManager(layoutManager);
            lastMessages.setAdapter(lastMessageAdapter);
            lastMessageAdapter.notifyDataSetChanged();
            findViewById(R.id.processBar).setVisibility(View.GONE);
        }
        findViewById(R.id.fabNewChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(MainActivity.this, NewMessageActivity.class)});
            }
        });
        findViewById(R.id.imageProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivities(new Intent[]{new Intent(MainActivity.this, ProfileActivity.class)});
            }
        });
        findViewById(R.id.imageSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivities(new Intent[]{new Intent(MainActivity.this, ContactActivity.class)});
            }
        });
        imageViewProfile = findViewById(R.id.imageProfile);
        if (userProfileDto.getAvatar() == null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Glide.with(getApplicationContext()).load(getApplicationContext().getDrawable(R.drawable.user)).into(imageViewProfile);
            } else {
                Glide.with(getApplicationContext()).load(userProfileDto.getAvatar()).into(imageViewProfile);
            }
    }

    @Override
    public void onMessageReceived(String message) {
        Gson gson = new Gson();
        Type status = new TypeToken<UserOnlineOfflinePayload>() {
        }.getType();
        UserOnlineOfflinePayload socketPayload = gson.fromJson(message, status);
        if (socketPayload == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Contact con : sharedPrefManager.getUser().getList_contact()) {
                    if (socketPayload.getActive() != null && con.getUserId().equals(socketPayload.getId()) && socketPayload.getActive()) {
                        con.getUser().setActive(socketPayload.getActive().booleanValue());
                        con.getUser().getLastActiveAt().setTime(new Date().getTime());
                        lastMessageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        WebSocketManager.getInstance(MainActivity.this).removeObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        WebSocketManager.getInstance(MainActivity.this).addObserver(this);
    }

}