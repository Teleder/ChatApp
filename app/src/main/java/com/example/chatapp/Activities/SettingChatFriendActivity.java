package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.chatapp.R;

public class SettingChatFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_chat_friend);
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(SettingChatFriendActivity.this, ChatActivity.class)});
            }
        });
        findViewById(R.id.tv_rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaLogReName();
            }
        });
        findViewById(R.id.tv_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaLogBlock();
            }
        });
    }
    private void DiaLogReName() {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_rename_friend);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingChatFriendActivity.this, "Update", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(1200, 900);
        dialog.show();
    }
    private void DiaLogBlock() {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_confirm_block);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnBlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingChatFriendActivity.this, "Block", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(1200, 600);
        dialog.show();
    }
}