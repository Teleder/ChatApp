package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class ForgetPassActivity extends AppCompatActivity {
    EditText input_phone;
    Button btnNext;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        retrofit = retrofitClient.getRetrofit();
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(ForgetPassActivity.this, LoginActivity.class)});
            }
        });
        input_phone = (EditText) findViewById(R.id.input_phone);
        btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input_phone.getText().toString().equals("")) {
                    Toast.makeText(ForgetPassActivity.this, "Hãy nhập số điện thoại của bạn", Toast.LENGTH_SHORT).show();
                    btnNext.setClickable(false);
                    btnNext.setFocusable(false);
                } else {
                    RequestPin(input_phone.getText().toString());
                }
            }
        });
    }

    private void RequestPin(String phone) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.reQuestPin(phone).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), CodeWhenResetActivity.class);
                    intent.putExtra("phone", phone);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                } else {
                    Toast.makeText(ForgetPassActivity.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ForgetPassActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}