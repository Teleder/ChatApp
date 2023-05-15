package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Activities.ChatActivity;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Dtos.UserSearchDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.List;

public class FriendMessAdapter extends RecyclerView.Adapter<FriendMessAdapter.FriendHolder>{
    private Context context;
    private List<UserSearchDto> arrList;


    SharedPrefManager sharedPrefManager;
    UserProfileDto userProfileDto;

    public FriendMessAdapter(Context context, List<UserSearchDto> arrList) {
        this.context = context;
        this.arrList = arrList;
        sharedPrefManager = new SharedPrefManager(context);
        userProfileDto = sharedPrefManager.getUser();
    }

    @NonNull
    @Override
    public FriendMessAdapter.FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.item_friend_mess, parent, false);
        return new FriendMessAdapter.FriendHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendMessAdapter.FriendHolder holder, int position) {
        UserSearchDto userSearchDto = arrList.get(position);
        if (userSearchDto.getAvatar() != null)
            Glide.with(context).load(userSearchDto.getAvatar()).into(holder.avatar);
        holder.tvDisplayName.setText(userSearchDto.getDisplayName());
        holder.tvPhone.setText(userSearchDto.getPhone());
        holder.userId.setText(userSearchDto.getId());
    }

    @Override
    public int getItemCount() {
        if (arrList != null) {
            return arrList.size();
        }
        return 0;
    }

    public class FriendHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView tvDisplayName, tvPhone, userId;

        public FriendHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgUser);
            tvDisplayName = itemView.findViewById(R.id.tvNameUser);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            userId = itemView.findViewById(R.id.id_User);
        }
    }
}
