package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.Adapter.NewMessageAdapter;
import com.example.chatapp.Adapter.ViewPagerAdapter;
import com.example.chatapp.R;
import com.google.android.material.tabs.TabLayout;

public class NewMessageActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        NewMessageAdapter newMessageAdapter = new NewMessageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(newMessageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(NewMessageActivity.this, MainActivity.class)});
            }
        });

    }
}