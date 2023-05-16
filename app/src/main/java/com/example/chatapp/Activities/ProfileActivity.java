package com.example.chatapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Utils.CONSTS;

import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    TextView tvEmail, tvPhone, tvFirstName, tvLastName, tvBio, tvFullName;
    ImageView imgUser;
    UserProfileDto userProfileDto;
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        userProfileDto = sharedPrefManager.getUser();
        AnhXa(userProfileDto);
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(ProfileActivity.this, MainActivity.class)});
            }
        });
        findViewById(R.id.tv_editprofile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(ProfileActivity.this, EditProfileActivity.class)});
            }
        });
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivities(new Intent[]{new Intent(ProfileActivity.this, LoginActivity.class)});
            }
        });
    }

    private void AnhXa(UserProfileDto userProfileDto) {
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvFirstName = findViewById(R.id.tv_firstname);
        tvLastName = findViewById(R.id.tv_lastname);
        tvBio = findViewById(R.id.tv_bio);
        tvFullName = findViewById(R.id.tv_fullname);
        imgUser = findViewById(R.id.imgUser);
        tvEmail.setText(userProfileDto.getEmail());
        tvBio.setText(userProfileDto.getBio());
        tvFirstName.setText(userProfileDto.getFirstName());
        tvLastName.setText(userProfileDto.getLastName());
        tvFullName.setText(userProfileDto.getDisplayName());
        tvPhone.setText(userProfileDto.getPhone());
        if (userProfileDto.getAvatar() != null)
            Glide.with(getApplicationContext()).load(userProfileDto.getAvatar().getUrl().replace("localhost:8080", "http://" + CONSTS.BASEURL)).into(imgUser);
    }
}