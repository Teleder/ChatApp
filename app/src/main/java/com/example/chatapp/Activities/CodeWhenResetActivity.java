package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.Dtos.TokenResetPass;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class CodeWhenResetActivity extends AppCompatActivity {
    EditText input_phone;
    Button btnNext;
    EditText text1, text2, text3, text4, text5, text6;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_when_reset);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        retrofit = retrofitClient.getRetrofit();
        text1 = (EditText) findViewById(R.id.editCode1);
        text2 = (EditText) findViewById(R.id.editCode2);
        text3 = (EditText) findViewById(R.id.editCode3);
        text4 = (EditText) findViewById(R.id.editCode4);
        text5 = (EditText) findViewById(R.id.editCode5);
        text6 = (EditText) findViewById(R.id.editCode6);
        String code = "";
        code = text1.getText().toString() + text2.getText().toString() + text3.getText().toString() + text4.getText().toString() + text5.getText().toString() + text6.getText().toString();
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(CodeWhenResetActivity.this, LoginActivity.class)});
            }
        });
        input_phone = (EditText) findViewById(R.id.input_phone);
        btnNext = (Button) findViewById(R.id.buttonNext);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateCode(phone, text1.getText().toString() + text2.getText().toString() + text3.getText().toString() + text4.getText().toString() + text5.getText().toString() + text6.getText().toString());
//                startActivities(new Intent[]{new Intent(CodeWhenResetActivity.this, NewPassActivity.class)});
            }
        });
    }

    private void ValidateCode(String phone, String code) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        TokenResetPass tokenResetPass = new TokenResetPass(code);
        apiService.validatePin(phone, tokenResetPass).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), NewPassActivity.class);
                    try {
                        intent.putExtra("token", response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(CodeWhenResetActivity.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CodeWhenResetActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}