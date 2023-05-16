package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.Dtos.resetPass;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;

import retrofit2.Retrofit;

public class NewPassActivity extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;
    EditText password, confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        retrofit = retrofitClient.getRetrofit();
        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        password = (EditText) findViewById(R.id.input_new_password);
        confirmPassword = (EditText) findViewById(R.id.input_confirm_Password);
        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPass(token);
//                startActivities(new Intent[]{new Intent(NewPassActivity.this, LoginActivity.class)});
            }
        });
    }
    private void ResetPass(String token) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        final String pass = password.getText().toString();
        final String confirm_pass = confirmPassword.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Hãy nhập password!");
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirm_pass)) {
            confirmPassword.setError("Hãy nhập xác nhận password!");
            confirmPassword.requestFocus();
            return;
        }
        if (!pass.equals(confirm_pass)) {
            confirmPassword.setError("Hãy nhập đúng mật khẩu");
            confirmPassword.requestFocus();
            return;
        }
        resetPass resetPass = new resetPass(pass,token);
        apiService.resetPassword(resetPass).enqueue(new retrofit2.Callback<Boolean>() {
            @Override
            public void onResponse(retrofit2.Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (response.isSuccessful()) {
                    startActivities(new Intent[]{new Intent(NewPassActivity.this, LoginActivity.class)});
                } else {
                    Toast.makeText(NewPassActivity.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Boolean> call, Throwable t) {
                Toast.makeText(NewPassActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}