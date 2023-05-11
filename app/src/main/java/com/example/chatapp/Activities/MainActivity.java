package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapter.LastMessageAdapter;
import com.example.chatapp.Adapter.WaitingAcceptContactAdapter;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Retrofit.WebSocketManager;

import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    SharedPrefManager sharedPrefManager;
    ImageView imageViewprofile;
    WebSocketManager webSocketManager;
    RecyclerView lastMessages;
    LastMessageAdapter lastMessageAdapter;
    List<Conservation> listConservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        UserProfileDto userProfileDto = sharedPrefManager.getUser();
        lastMessages = (RecyclerView) findViewById(R.id.conversationRecyclerView);
        if(userProfileDto.getConservations().size() > 0)
        {
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
        webSocketManager = WebSocketManager.getInstance();
        webSocketManager.connect(userProfileDto.getId());
        //webSocketManager.subscribeToPrivateMessages(userProfileDto.getId());
        findViewById(R.id.fabNewChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(MainActivity.this, UsersActivity.class)});
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
        imageViewprofile = findViewById(R.id.imageProfile);
        if (userProfileDto.getAvatar() == null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Glide.with(getApplicationContext()).load(getApplicationContext().getDrawable(R.drawable.user)).into(imageViewprofile);
            }
        else {
                Glide.with(getApplicationContext()).load(userProfileDto.getAvatar()).into(imageViewprofile);
            }
    }
}