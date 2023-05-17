package com.example.chatapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.Model.Group.Member;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.RoleViewHolder> {
    private List<UserBasicDto> arrList = new ArrayList<>();
    private Context context;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;
    SharedPrefManager sharedPrefManager;
String groupId;
    public MembersAdapter(Context context, List<UserBasicDto> arrList, String groupid) {
        sharedPrefManager = new SharedPrefManager(context);

        this.arrList = arrList;
        this.context = context;
        this.groupId = groupid;
        retrofitClient = RetrofitClient.getInstance(context);
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(context);
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
        holder.btnInvate.setVisibility(View.VISIBLE);
        holder.btnInvate.setText("Chap nhan");
        holder.btnInvate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequestMemberJoin(userBasicDto.getId().toString(), holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrList == null ? 0 : arrList.size();
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

    private void getRequestMemberJoin(String id,RoleViewHolder holder ) {
        apiService = retrofit.create(APIService.class);
        Log.d("idsdasdasdasdas",sharedPrefManager.getCurrentGroup().getId());
        apiService.responseMemberJoin(groupId, id, true).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    try {
                        List<Member> mems = sharedPrefManager.getCurrentGroup().getMembers();
                        for (Member m : mems) {
                            if (m.getUserId().equals(id)) {
                                m.setStatus(Member.Status.ACCEPT);
                                holder.btnInvate.setText("Accept");
                                break;
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
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
