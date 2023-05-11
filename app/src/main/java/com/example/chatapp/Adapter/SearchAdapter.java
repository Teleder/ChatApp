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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchContactHolder>{
    private Context context;
    private List<UserSearchDto> arrList;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;

    public SearchAdapter(Context context, List<UserSearchDto> arrList) {
        this.context = context;
        this.arrList = arrList;
        retrofitClient = RetrofitClient.getInstance(context);
        retrofit = retrofitClient.getRetrofit();
    }

    @NonNull
    @Override
    public SearchAdapter.SearchContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.item_list_search, parent, false);
        return new SearchAdapter.SearchContactHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchContactHolder holder, int position) {
        UserSearchDto userSearchDto = arrList.get(position);
        if (userSearchDto.getAvatar() != null)
            Glide.with(context).load(userSearchDto.getAvatar()).into(holder.avatar);
        holder.tvDisplayName.setText(userSearchDto.getDisplayName());
        holder.tvBio.setText(userSearchDto.getBio());
        holder.userId.setText(userSearchDto.getId());
        holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnFriend(holder.userId.getText().toString());
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

    public class SearchContactHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView tvDisplayName, tvBio, userId;
        private Button btnAddFriend;

        public SearchContactHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgUser);
            tvDisplayName = itemView.findViewById(R.id.tvNameUser);
            tvBio = itemView.findViewById(R.id.tvBioUser);
            btnAddFriend = itemView.findViewById(R.id.btnUnFriend);
            userId = itemView.findViewById(R.id.id_User);
        }
    }

    public void setListenerList(List<UserSearchDto> contactModelList) {
        this.arrList = contactModelList;
        notifyDataSetChanged();
    }
    private void UnFriend(String id)
    {
        apiService = retrofit.create(APIService.class);
        apiService.UnFriend(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(context, "Unfriend success", Toast.LENGTH_SHORT).show();

//                        finish();
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
