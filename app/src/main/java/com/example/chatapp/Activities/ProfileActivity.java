package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;

import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    SharedPrefManager sharedPrefManager;
    TextView tvEmail, tvPhone, tvFirstName, tvLastName, tvBio, tvFullName;
    ImageView imgUser;
    UserProfileDto userProfileDto;
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
    }
    private void AnhXa(UserProfileDto userProfileDto)
    {
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvFirstName = (TextView) findViewById(R.id.tv_firstname);
        tvLastName = (TextView) findViewById(R.id.tv_lastname);
        tvBio = (TextView) findViewById(R.id.tv_bio);
        tvFullName = (TextView) findViewById(R.id.tv_fullname);
        imgUser = (ImageView) findViewById(R.id.imgUser);
        tvEmail.setText(userProfileDto.getEmail());
        tvBio.setText(userProfileDto.getBio());
        tvFirstName.setText(userProfileDto.getFirstName());
        tvLastName.setText(userProfileDto.getLastName());
        tvFullName.setText(userProfileDto.getDisplayName());
        tvPhone.setText(userProfileDto.getPhone());
        if (userProfileDto.getAvatar() == null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Glide.with(getApplicationContext()).load(getApplicationContext().getDrawable(R.drawable.user)).into(imgUser);
            }
            else {
                Glide.with(getApplicationContext()).load(userProfileDto.getAvatar()).into(imgUser);
            }
    }
}