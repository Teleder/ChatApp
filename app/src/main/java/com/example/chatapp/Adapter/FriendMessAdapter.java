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
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.List;

public class FriendMessAdapter extends RecyclerView.Adapter<FriendMessAdapter.FriendHolder>{
    private Context context;
    private List<UserBasicDto> arrList;


    SharedPrefManager sharedPrefManager;
    UserProfileDto userProfileDto;

    public FriendMessAdapter(Context context, List<UserBasicDto> arrList) {
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
        UserBasicDto UserBasicDto = arrList.get(position);
        if (UserBasicDto.getAvatar() != null)
            Glide.with(context).load(UserBasicDto.getAvatar()).into(holder.avatar);
        holder.tvDisplayName.setText(UserBasicDto.getDisplayName());
        holder.tvPhone.setText(UserBasicDto.getPhone());
        holder.userId.setText(UserBasicDto.getId());
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
