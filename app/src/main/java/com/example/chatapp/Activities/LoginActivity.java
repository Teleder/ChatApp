package com.example.chatapp.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.Dtos.LoginDto;
import com.example.chatapp.Dtos.LoginInputDto;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Inteface.OnApiCallsCompletedListener;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Retrofit.WebSocketManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    Button btnLogin;
    APIService apiService;
    EditText email;
    EditText password;
    ProgressBar processBar;
    WebSocketManager webSocketManager;
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    private final boolean isWebSocketConnected = false;
    MutableLiveData<List<Conservation>> conversationsLiveData = new MutableLiveData<>();
    MutableLiveData<List<String>> groupsLiveData = new MutableLiveData<>();

    public LiveData<List<Conservation>> getConversationsLiveData() {
        return conversationsLiveData;
    }

    public LiveData<List<String>> getGroupsLiveData() {
        return groupsLiveData;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.buttonSignIn);
        processBar = findViewById(R.id.processBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        findViewById(R.id.textCreateNewAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(LoginActivity.this, SignUpActivity.class)});
            }
        });
        findViewById(R.id.tvForgetPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(LoginActivity.this, ForgetPassActivity.class)});
            }
        });
    }

    private void login() {
        final String emailuser = email.getText().toString();
        final String pass = password.getText().toString();
        if (TextUtils.isEmpty(emailuser)) {
            email.setError("Hãy nhập email!");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            password.setError("Hãy nhập password!");
            password.requestFocus();
            return;
        }
        apiService = retrofit.create(APIService.class);
        apiService.loginUser(new LoginInputDto(emailuser, pass)).enqueue(new Callback<LoginDto>() {
            @Override
            public void onResponse(Call<LoginDto> call, Response<LoginDto> response) {
                processBar.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    try {
                        sharedPrefManager.clear();
                        tokenManager.saveTokens(response.body().getAccessToken(), response.body().getRefreshToken());
                        sharedPrefManager.saveUser(response.body().getUser());
                        Log.d("Login", "onResponse: " + response.body().getUser().getDisplayName());
                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        createNotificationChannel();
                        finish();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginDto> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Your Channel Name";
            String description = "Your Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("20022023", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void fetchGroup() {
        apiService.getAllIdConservationGroup().enqueue(new retrofit2.Callback<List<String>>() {
            @Override
            public void onResponse(retrofit2.Call<List<String>> call, retrofit2.Response<List<String>> response) {
                if (response.isSuccessful()) {
                    try {
                       sharedPrefManager.saveListGroupId(response.body());
                        groupsLiveData.setValue(response.body());

                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "fail send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<String>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void fetchConservation() {
//        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.getMyConversations(0, 100).enqueue(new retrofit2.Callback<PagedResultDto<Conservation>>() {
            @Override
            public void onResponse(retrofit2.Call<PagedResultDto<Conservation>> call, retrofit2.Response<PagedResultDto<Conservation>> response) {
                if (response.isSuccessful()) {
                    try {
                        conversationsLiveData.setValue(response.body().getData());
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "fail send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PagedResultDto<Conservation>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}