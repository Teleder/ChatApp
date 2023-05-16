package com.example.chatapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.chatapp.Activities.CreateGroupActivity;
import com.example.chatapp.Activities.ListContactActivity;
import com.example.chatapp.Activities.ListFriendActivity;
import com.example.chatapp.Activities.ListGroupActivity;
import com.example.chatapp.Activities.WaitingAcceptContactActivity;
import com.example.chatapp.Dialog.CreateGroupFragment;

public class NewMessageAdapter extends FragmentStatePagerAdapter {
    public NewMessageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new CreateGroupFragment();
            default:
                return new ListFriendActivity();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Bạn bè";
                break;
            case 1:
                title = "Nhóm";
                break;
        }
        return title;
    }
}
