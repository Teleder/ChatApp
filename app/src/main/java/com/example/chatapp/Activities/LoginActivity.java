package com.example.chatapp.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.Dtos.LoginDto;
import com.example.chatapp.Dtos.LoginInputDto;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Retrofit.WebSocketManager;

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
    }

    private void login() {
        final String emailuser = email.getText().toString();
        final String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(emailuser)) {
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }
        apiService = retrofit.create(APIService.class);
        apiService.loginUser(new LoginInputDto(emailuser, pass)).enqueue(new Callback<LoginDto>() {
            @Override
            public void onResponse(Call<LoginDto> call, Response<LoginDto> response) {
                processBar.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    try {
                        tokenManager.saveTokens(response.body().getAccessToken(), response.body().getRefreshToken());
                        sharedPrefManager.saveUser(response.body().getUser());
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
}