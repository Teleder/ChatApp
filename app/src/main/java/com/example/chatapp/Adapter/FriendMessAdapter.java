package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Activities.ChatActivity;
import com.example.chatapp.Activities.ChatGroupActivity;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Utils.CONSTS;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FriendMessAdapter extends RecyclerView.Adapter<FriendMessAdapter.FriendHolder>{
    private Context context;
    private List<UserBasicDto> arrList;


    SharedPrefManager sharedPrefManager;
    UserProfileDto userProfileDto;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;

    String code;

    public FriendMessAdapter(Context context, List<UserBasicDto> arrList) {
        this.context = context;
        this.arrList = arrList;
        retrofitClient = RetrofitClient.getInstance(context);
        retrofit = retrofitClient.getRetrofit();
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
        if (UserBasicDto != null && UserBasicDto.getAvatar() != null && UserBasicDto.getAvatar().getUrl() != null)
            Glide.with(context).load(UserBasicDto.getAvatar().getUrl().replace("localhost:8080", "http://" + CONSTS.BASEURL)).into(holder.avatar);
        holder.tvDisplayName.setText(UserBasicDto.getDisplayName());
        holder.tvPhone.setText(UserBasicDto.getPhone());
        holder.userId.setText(UserBasicDto.getId());
        holder.tvDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNewMessage(holder.userId.getText().toString());
            }
        });
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
            avatar = itemView.findViewById(R.id.image_User);
            tvDisplayName = itemView.findViewById(R.id.tvNameUser);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            userId = itemView.findViewById(R.id.id_User);
        }
    }
    public void handleNewMessage(String userId) {
//        Intent intent = new Intent(context, ChatActivity.class);
//        intent.putExtra("userId", userId);
//        context.startActivity(intent);
        apiService = retrofit.create(APIService.class);
        apiService.findMessagesByIdUser(0, 1,"",userId).enqueue(new Callback<PagedResultDto<Message>>() {
            @Override
            public void onResponse(Call<PagedResultDto<Message>> call, Response<PagedResultDto<Message>> response) {
                if (response.isSuccessful()) {
                    try {

                        code = response.body().getData().get(0).getCode();
                        getConservation();
//                            sharedPrefManager.saveCurrentConservation(response.body().getData().get(0).getCode());

//                        finish();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PagedResultDto<Message>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getConservation() {
        apiService = retrofit.create(APIService.class);
        apiService.getMyConversations(0, 1000).enqueue(new Callback<PagedResultDto<Conservation>>() {
            @Override
            public void onResponse(Call<PagedResultDto<Conservation>> call, Response<PagedResultDto<Conservation>> response) {
                if (response.isSuccessful()) {
                    try {
                        for(Conservation c : response.body().getData()){
                            if(c.getCode().equals(code)){
                                sharedPrefManager.saveCurrentConservation(c);
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("code", c.getCode());
                                context.startActivity(intent);
                            }
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PagedResultDto<Conservation>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
