package com.example.chatapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapter.LastMessageAdapter;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.SocketPayload;
import com.example.chatapp.Dtos.UserOnlineOfflinePayload;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Inteface.ViewModelAPI;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Retrofit.WebSocketManager;
import com.example.chatapp.Utils.CONSTS;
import com.example.chatapp.Utils.MessageObserver;
import com.example.chatapp.Utils.WebSocketService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements MessageObserver {
    static SharedPrefManager sharedPrefManager;
    ImageView imageViewProfile;
    WebSocketManager webSocketManager;
    RecyclerView lastMessages;
    LastMessageAdapter lastMessageAdapter;
    List<Conservation> listConservations = new ArrayList<>();

    private boolean isWebSocketConnected = false;
    private TokenManager tokenManager;
    static private Retrofit retrofit;
    static APIService apiService;
    private static RetrofitClient retrofitClient;
    List<String> groupCode;
    ViewModelAPI viewModel;

    public static Retrofit getRetrofitC() {
        return retrofit;
    }

    public static SharedPrefManager getSharedPrefManager() {
        return sharedPrefManager;
    }

    public static RetrofitClient getRetrofitClient() {
        return retrofitClient;
    }

    public static APIService getApiService() {
        return apiService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        viewModel = new ViewModelProvider(this).get(ViewModelAPI.class);
        UserProfileDto userProfileDto = sharedPrefManager.getUser();


        viewModel.getConversationsLiveData().observe(this, conversations -> {
            if (viewModel.getConversationsLiveData().getValue() == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listConservations = sharedPrefManager.getListConservation();
                        lastMessageAdapter.notifyDataSetChanged();
                    }
                });
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listConservations.clear();
                    listConservations.addAll(viewModel.getConversationsLiveData().getValue());
                    lastMessageAdapter.notifyDataSetChanged();
                }
            });
        });
        viewModel.getGroupsLiveData().observe(this, groups -> {
            if (!isWebSocketConnected) {
                webSocketManager.connect(userProfileDto.getId());
                webSocketManager.subscribeToPrivateMessages(userProfileDto.getId());
                for (String a : sharedPrefManager.getListGroupId()) {
                    webSocketManager.subscribeToGroupMessages(a);
                }
                isWebSocketConnected = true;
            }
        });
        viewModel.fetchConversations();
        viewModel.fetchGroups();

        setContentView(R.layout.activity_main);
        lastMessages = findViewById(R.id.conversationRecyclerView);
        webSocketManager = WebSocketManager.getInstance(MainActivity.this);
        Intent intent = new Intent(this, WebSocketService.class);
        intent.setAction("CONNECT");
        intent.putExtra("user_id", userProfileDto.getId());
        startService(intent);
        apiService = retrofitClient.getRetrofit().create(APIService.class);


        if (userProfileDto.getConservations().size() > 0) {
            lastMessages.setVisibility(View.VISIBLE);
//            fetchConservation();
            lastMessageAdapter = new LastMessageAdapter(MainActivity.this, listConservations);
            lastMessages.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            lastMessages.setLayoutManager(layoutManager);
            lastMessages.setAdapter(lastMessageAdapter);
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
    public void onPause() {
        super.onPause();
        WebSocketManager.getInstance(MainActivity.this).removeObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        WebSocketManager.getInstance(MainActivity.this).addObserver(this);
    }


    public void fetchGroup(UserProfileDto user) {
        apiService.getAllIdConservationGroup().enqueue(new retrofit2.Callback<List<String>>() {
            @Override
            public void onResponse(retrofit2.Call<List<String>> call, retrofit2.Response<List<String>> response) {
                if (response.isSuccessful()) {
                    try {
                        groupCode = response.body();
                        if (!isWebSocketConnected) {
                            webSocketManager.disconnect();
                            webSocketManager.connect(user.getId());
                            webSocketManager.subscribeToPrivateMessages(user.getId());
                            for (String a : groupCode) {
                                webSocketManager.subscribeToGroupMessages(a);
                            }
                            isWebSocketConnected = true;
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "fail send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<String>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessageReceived(String message) {

        Gson gson = new Gson();
        Type socketPayloadType = new TypeToken<SocketPayload<Object>>() {
        }.getType();
        SocketPayload socketPayload;
        try {
            socketPayload = gson.fromJson(message, socketPayloadType);
        }
        catch (Exception e) {
            gson = new Gson();
            Type status = new TypeToken<UserOnlineOfflinePayload>() {
            }.getType();
            UserOnlineOfflinePayload payload = gson.fromJson(message, status);
            if (payload == null)
                return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Contact con : sharedPrefManager.getUser().getList_contact()) {
                        if (payload.getActive() != null && con.getUserId().equals(payload.getId()) && payload.getActive()) {
                            con.getUser().setActive(payload.getActive().booleanValue());
                            con.getUser().getLastActiveAt().setTime(new Date().getTime());
                            lastMessageAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
            return;
        }

        if (socketPayload.getType() != null && socketPayload.getType().equals(CONSTS.NEW_GROUP)) {
            gson = new Gson();
            String json = gson.toJson(socketPayload.getData());
            Conservation conservation = gson.fromJson(json, Conservation.class);
            List<Conservation> cons = sharedPrefManager.getListConservation();
            cons.add(0, conservation);
            List<String> ids = sharedPrefManager.getListGroupId();
            ids.add(conservation.getGroupId());
            sharedPrefManager.saveListConservation(cons);
            sharedPrefManager.saveListGroupId(ids);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webSocketManager.subscribeToGroupMessages(conservation.getGroupId());
                    lastMessageAdapter.notifyDataSetChanged();
                }
            });
        } else if (socketPayload.getType().equals(CONSTS.MESSAGE_PRIVATE) || socketPayload.getType().equals(CONSTS.MESSAGE_GROUP)) {
            List<Conservation> cons = sharedPrefManager.getListConservation();
            gson = new Gson();
            String json = gson.toJson(socketPayload.getData());
            Message mess = gson.fromJson(json, Message.class);
            int pos = 0;
            for (Conservation con : cons) {
                if (con.getCode().equals(mess.getCode())){
                    con.setLastMessage(mess);
                    break;
                }
                pos++;
            }
            Conservation conTmp = cons.get(pos);
            cons.remove(pos);
            cons.add(0, conTmp);
            listConservations.remove(pos);
            listConservations.add(0, conTmp);
            sharedPrefManager.saveListConservation(cons);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lastMessageAdapter.notifyDataSetChanged();
                }
            });
        } else {

        }


    }


    public void fetchConservation() {
//        apiService = retrofitClient.getRetrofit().create(APIService.class);
        if (lastMessageAdapter != null && sharedPrefManager.getListConservation() != null && sharedPrefManager.getListConservation().size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listConservations.clear();
                    listConservations.addAll(sharedPrefManager.getListConservation());
                    lastMessageAdapter.notifyDataSetChanged();
                }
            });
            return;
        }
        apiService.getMyConversations(0, 100).enqueue(new retrofit2.Callback<PagedResultDto<Conservation>>() {
            @Override
            public void onResponse(retrofit2.Call<PagedResultDto<Conservation>> call, retrofit2.Response<PagedResultDto<Conservation>> response) {
                if (response.isSuccessful()) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sharedPrefManager.saveListConservation(response.body().getData());
                                listConservations.clear();
                                listConservations.addAll(response.body().getData());
                                lastMessageAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "fail send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PagedResultDto<Conservation>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}