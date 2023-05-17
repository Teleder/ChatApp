package com.example.chatapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.User.Block;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;

import java.util.List;

import retrofit2.Retrofit;

public class DetailChatActivity extends AppCompatActivity {

    private Button btn_block;
    private Button btn_delete;
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    SharedPrefManager sharedPrefManager;

    String contact;
    APIService apiService;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_detail);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        btn_block = findViewById(R.id.btn_block);
        btn_delete = findViewById(R.id.btn_delete);
        List<Block> blocks = sharedPrefManager.getUser().getBlocks();


        contact = sharedPrefManager.getCurrentConservation().getUserId_1().equals(sharedPrefManager.getUser().getId())
                ? sharedPrefManager.getCurrentConservation().getUserId_2() : sharedPrefManager.getCurrentConservation().getUserId_1();
        for (Block block : blocks) {
            if (block.getUserId().equals(contact)) {
                btn_block.setText("Unblock");
                flag = true;
                btn_block.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UnBlock();
                    }
                });
                break;
            }
        }
        if (!flag) {
            btn_block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BlockUser();
                }
            });
        }
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete();
            }
        });
    }

    public void Delete() {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.deleteConservation(sharedPrefManager.getCurrentConservation().getCode()).enqueue(new retrofit2.Callback<Boolean>() {
            @Override
            public void onResponse(retrofit2.Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        sharedPrefManager.getListConservation().remove(sharedPrefManager.getCurrentConservation());
                        startActivities(new Intent[]{new Intent(DetailChatActivity.this, MainActivity.class)});
                        finish();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DetailChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Boolean> call, Throwable t) {
                Toast.makeText(DetailChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UnBlock() {
        apiService = retrofitClient.getRetrofit().create(APIService.class);

        apiService.removeBlock(contact).enqueue(new retrofit2.Callback<Boolean>() {
            @Override
            public void onResponse(retrofit2.Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        sharedPrefManager.getCurrentConservation().setStatus(false);
                        if (sharedPrefManager.getUser().getBlocks().size() > 0) {
                            int pos = 0;
                            for (Block block : sharedPrefManager.getUser().getBlocks()) {
                                if (block.getUserId().equals(contact)) {
                                    break;
                                }
                                pos++;
                            }
                            UserProfileDto user = sharedPrefManager.getUser();
                            user.getBlocks().remove(pos);
                            sharedPrefManager.saveUser(user);
                        }
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DetailChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Boolean> call, Throwable t) {
                Toast.makeText(DetailChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void BlockUser() {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.blockContact(contact, "Doan xem").enqueue(new retrofit2.Callback<Boolean>() {
            @Override
            public void onResponse(retrofit2.Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        btn_block.setText("Block");

                        Conservation cons = sharedPrefManager.getCurrentConservation();
                        cons.setStatus(false);
                        sharedPrefManager.saveCurrentConservation(cons);

                        UserProfileDto user = sharedPrefManager.getUser();
                        user.getBlocks().add(new Block(contact, "Doan xem"));
                        sharedPrefManager.saveUser(user);

                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    ;
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Boolean> call, Throwable t) {
                Toast.makeText(DetailChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
