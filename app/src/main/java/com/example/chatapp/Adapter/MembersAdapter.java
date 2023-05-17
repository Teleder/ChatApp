package com.example.chatapp.Adapter;

import android.content.Context;
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
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.RoleViewHolder> {
    private List<UserBasicDto> arrList;
    private Context context;

    public MembersAdapter(Context context, List<UserBasicDto> arrList) {
        this.arrList = arrList;
        this.context = context;
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
        holder.btnInvate.setVisibility(View.GONE);
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
}
