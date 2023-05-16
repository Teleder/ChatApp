package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;

public class ForgetPassActivity extends AppCompatActivity {
    EditText input_phone;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
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
                    startActivities(new Intent[]{new Intent(ForgetPassActivity.this, CodeWhenResetActivity.class)});
                }
            }
        });
    }
}