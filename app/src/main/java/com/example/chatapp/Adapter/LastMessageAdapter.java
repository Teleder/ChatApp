package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Activities.ChatActivity;
import com.example.chatapp.Activities.MainActivity;
import com.example.chatapp.Activities.UsersActivity;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.List;

public class LastMessageAdapter extends RecyclerView.Adapter<LastMessageAdapter.LastMessageHolder>{
    private Context context;
    private List<Conservation> arrList;

    SharedPrefManager sharedPrefManager;
    UserProfileDto userProfileDto;

    public LastMessageAdapter(Context context, List<Conservation> arrList) {
        this.context = context;
        this.arrList = arrList;
        sharedPrefManager = new SharedPrefManager(context);
        userProfileDto = sharedPrefManager.getUser();
    }

    @NonNull
    @Override
    public LastMessageAdapter.LastMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.item_last_messages, parent, false);
        return new LastMessageAdapter.LastMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(LastMessageAdapter.LastMessageHolder holder, int position) {
        Conservation conservation = arrList.get(position);
        if(arrList.size()>0)
        {
            if(userProfileDto.getId().equals(conservation.getUserId_1()) && conservation.getGroup()==null)
            {
                holder.tvDisplayName.setText(conservation.getUser_2().getDisplayName());
                holder.tvLastMessages.setText(conservation.getLastMessage().getContent());
                holder.userId.setText(conservation.getUser_2().getId());
                holder.tvTime.setText("3h");
                if (conservation.getUser_2().getAvatar() != null)
                    Glide.with(context).load(conservation.getUser_2().getAvatar()).into(holder.avatar);
            }
            else if(userProfileDto.getId().equals(conservation.getUser_2().getId()) && conservation.getGroup()==null)
            {
                holder.tvDisplayName.setText(conservation.getUser_1().getDisplayName());
                holder.tvLastMessages.setText(conservation.getLastMessage().getContent());
                holder.userId.setText(conservation.getUser_1().getId());
                holder.tvTime.setText("3h");
                if (conservation.getUser_1().getAvatar() != null)
                    Glide.with(context).load(conservation.getUser_2().getAvatar()).into(holder.avatar);
            }
//            holder.avatar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, ChatActivity.class);
//                    context.startActivity(intent);
//                }
//            });
            holder.tvDisplayName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("code",conservation.getCode());
                    sharedPrefManager.saveCurrentConservation(conservation);
                    context.startActivity(intent);
                }
            });
            holder.tvLastMessages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("code",conservation.getCode());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (arrList != null) {
            return arrList.size();
        }
        return 0;
    }

    public class LastMessageHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView tvDisplayName, tvLastMessages, userId, tvTime;

        public LastMessageHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgUser);
            tvDisplayName = itemView.findViewById(R.id.tvNameUser);
            tvLastMessages = itemView.findViewById(R.id.tvLastMessages);
            tvTime = itemView.findViewById(R.id.tv_time_chat);
            userId = itemView.findViewById(R.id.id_User);
        }
    }
}
