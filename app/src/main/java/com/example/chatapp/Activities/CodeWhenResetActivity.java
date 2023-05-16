package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;

public class CodeWhenResetActivity extends AppCompatActivity {
    EditText input_phone;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_when_reset);
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(CodeWhenResetActivity.this, LoginActivity.class)});
            }
        });
        input_phone = (EditText) findViewById(R.id.input_phone);
        btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivities(new Intent[]{new Intent(CodeWhenResetActivity.this, NewPassActivity.class)});
            }
        });
    }
}