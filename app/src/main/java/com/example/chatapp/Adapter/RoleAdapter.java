package com.example.chatapp.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.Model.Group.Role;
import com.example.chatapp.Model.Permission.Permission;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {
    private List<UserBasicDto> arrList;
    private Context context;
    SharedPrefManager sharedPrefManager;
    UserProfileDto userProfileDto;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;
    List<UserBasicDto> members;
    String groupId;

    public RoleAdapter(Context context, List<UserBasicDto> arrList, List<UserBasicDto> members, String groupId) {
        this.arrList = arrList;
        this.context = context;
        this.members = members;
        this.groupId = groupId;
        sharedPrefManager = new SharedPrefManager(context);
        userProfileDto = sharedPrefManager.getUser();
        retrofitClient = RetrofitClient.getInstance(context);
        retrofit = retrofitClient.getRetrofit();

    }

    @Override
    public RoleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_user_group, parent, false);
        return new RoleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoleViewHolder holder, int position) {
        UserBasicDto userBasicDto = arrList.get(position);
        if (userBasicDto.getAvatar() != null)
            Glide.with(context).load(userBasicDto.getAvatar()).into(holder.imgAvt);
        holder.tv_name.setText(userBasicDto.getDisplayName());
        holder.tv_phone.setText(userBasicDto.getPhone());
        holder.tv_id.setText(userBasicDto.getId());
        if (members.size() > 0) {
            for (int i = 0; i < members.size(); i++) {
                if (!members.get(i).getId().toString().equals(userBasicDto.getId().toString())) {
                    holder.btnInvate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            InvateUserInGroup(groupId, holder.tv_id.getText().toString(), holder);
                        }
                    });
                } else {
                    holder.btnInvate.setVisibility(View.GONE);
                    break;
                }
            }
        } else {
            holder.btnInvate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InvateUserInGroup("646413ad9f1e9016dd1a3a98", holder.tv_id.getText().toString(), holder);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }

    public static class RoleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_phone, tv_id;
        Button btnInvate;
        ImageView imgAvt;

        public RoleViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tvNameUser);
            tv_phone = itemView.findViewById(R.id.tvPhoneUser);
            btnInvate = itemView.findViewById(R.id.btnInvateUser);
            imgAvt = itemView.findViewById(R.id.image_User);
            tv_id = itemView.findViewById(R.id.id_User);
        }
    }

    public void InvateUserInGroup(String groupId, String userId, RoleViewHolder holder) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.addMemberToGroup(groupId, userId).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if (response.isSuccessful()) {
                    try {
                        //
                        holder.btnInvate.setText("Đã mời");
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
