package com.example.chatapp.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.TokenManager;

import java.util.List;

import retrofit2.Retrofit;

public class DetailChatActivity extends AppCompatActivity {

    private Button btn_block;
    private Button btn_delete;    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;

    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_detail);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();

        btn_block = findViewById(R.id.btn_block);
        btn_delete = findViewById(R.id.btn_delete);
    }
    public void fetchGroup() {
        apiService.getAllIdConservationGroup().enqueue(new retrofit2.Callback<List<String>>() {
            @Override
            public void onResponse(retrofit2.Call<List<String>> call, retrofit2.Response<List<String>> response) {
                if (response.isSuccessful()) {
                    try {


                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DetailChatActivity.this, "fail send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<String>> call, Throwable t) {
                Toast.makeText(DetailChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
