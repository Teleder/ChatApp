package com.example.chatapp.Adapter;

import android.content.Context;
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
import com.example.chatapp.Dtos.UserSearchDto;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private Context context;
    private List<UserSearchDto> arrList;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;

    public ContactAdapter(Context context, List<UserSearchDto> arrList) {
        this.context = context;
        this.arrList = arrList;
        retrofitClient = RetrofitClient.getInstance(context);
        retrofit = retrofitClient.getRetrofit();
    }

    @NonNull
    @Override
    public ContactAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.item_contacts, parent, false);
        return new ContactAdapter.ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactHolder holder, int position) {
        UserSearchDto userSearchDto = arrList.get(position);
        if (userSearchDto.getAvatar() != null)
            Glide.with(context).load(userSearchDto.getAvatar()).into(holder.avatar);
        holder.tvDisplayName.setText(userSearchDto.getDisplayName());
        holder.tvBio.setText(userSearchDto.getBio());
        holder.userId.setText(userSearchDto.getId());
        holder.btnUnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnFriend(holder.userId.getText().toString(), holder);
//                Toast.makeText(context, "addFriend", Toast.LENGTH_SHORT).show();
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

    public class ContactHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView tvDisplayName, tvBio, userId;
        private Button btnUnFriend;

        public ContactHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgUser);
            tvDisplayName = itemView.findViewById(R.id.tvNameUser);
            tvBio = itemView.findViewById(R.id.tvBioUser);
            btnUnFriend = itemView.findViewById(R.id.btnUnFriend);
            userId = itemView.findViewById(R.id.id_User);
        }
    }

    public void setListenerList(List<UserSearchDto> contactModelList) {
        this.arrList = contactModelList;
        notifyDataSetChanged();
    }
    private void UnFriend(String id, ContactHolder holder)
    {
        apiService = retrofit.create(APIService.class);
        apiService.UnFriend(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        holder.btnUnFriend.setText("Đã hủy kết bạn");
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
