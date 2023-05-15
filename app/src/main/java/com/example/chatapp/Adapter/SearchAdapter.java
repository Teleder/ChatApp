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
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Dtos.UserSearchDto;
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchContactHolder> {
    private Context context;
    private List<UserSearchDto> arrList;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    SharedPrefManager sharedPrefManager;
    UserProfileDto userProfileDto;
    APIService apiService;

    public SearchAdapter(Context context, List<UserSearchDto> arrList) {
        this.context = context;
        this.arrList = arrList;
        retrofitClient = RetrofitClient.getInstance(context);
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(context);
        userProfileDto = sharedPrefManager.getUser();
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
        //Kiem tra neu list contact khong co ai thi them button add friend
        if (userProfileDto.getList_contact().size() <= 0) {
            if (!userSearchDto.getId().equals(userProfileDto.getId())) {
                holder.btnAddFriend.setVisibility(View.VISIBLE);
            }
            holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFriend(holder.userId.getText().toString(), holder);
                    Toast.makeText(context, "addFriend", Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            for (int i = 0; i < userProfileDto.getList_contact().size(); i++) {
                if (userProfileDto.getList_contact().get(i).getUserId().equals(userSearchDto.getId())) {
                    if (userProfileDto.getList_contact().get(i).getStatus().toString().equals("WAITING")) {
                        holder.btnUnfriend.setVisibility(View.VISIBLE);
                        holder.btnUnfriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                RemoveAddFriend(userSearchDto.getId(), holder);
                            }
                        });
                    } else if (userProfileDto.getList_contact().get(i).getStatus().toString().equals("ACCEPT")) {
                        holder.btnUnfriend.setVisibility(View.GONE);
                        holder.btnAddFriend.setVisibility(View.VISIBLE);
                        holder.btnAddFriend.setText("Bạn bè");
                    } else {
                        holder.btnAddFriend.setVisibility(View.VISIBLE);
                        holder.btnAddFriend.setText("Accept");
                        holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AcceptFriend(userSearchDto.getId(),holder);
                            }
                        });
                    }
                } else {
                    if (!userSearchDto.getId().equals(userProfileDto.getId())) {
                        holder.btnAddFriend.setVisibility(View.VISIBLE);
                    }
                    holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AddFriend(holder.userId.getText().toString(), holder);
                            Toast.makeText(context, "addFriend", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
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
        private Button btnAddFriend, btnUnfriend;

        public SearchContactHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgUser);
            tvDisplayName = itemView.findViewById(R.id.tvNameUser);
            tvBio = itemView.findViewById(R.id.tvBioUser);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);
            btnUnfriend = itemView.findViewById(R.id.btnUnFriend);
            userId = itemView.findViewById(R.id.id_User);
        }
    }

    public void setListenerList(List<UserSearchDto> contactModelList) {
        this.arrList = contactModelList;
        notifyDataSetChanged();
    }

    private void AddFriend(String id, SearchContactHolder holder) {
        apiService = retrofit.create(APIService.class);
        apiService.AddFriend(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        holder.btnAddFriend.setVisibility(View.GONE);
                        holder.btnUnfriend.setVisibility(View.VISIBLE);
                        holder.btnUnfriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                RemoveAddFriend(id, holder);
                            }
                        });
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
    private void RemoveAddFriend(String id, SearchContactHolder holder) {
        apiService = retrofit.create(APIService.class);
        apiService.RemoveRequestAddFriend(id).enqueue(new Callback<UserProfileDto>() {
            @Override
            public void onResponse(Call<UserProfileDto> call, Response<UserProfileDto> response) {
                if (response.isSuccessful()) {
                    try {
                        holder.btnAddFriend.setVisibility(View.VISIBLE);
                        holder.btnUnfriend.setVisibility(View.GONE);
                        holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AddFriend(id, holder);
                            }
                        });
                        userProfileDto = response.body();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileDto> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void AcceptFriend(String id,SearchContactHolder holder)
    {
        apiService = retrofit.create(APIService.class);
        apiService.AcceptContact(id, true).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        holder.btnAddFriend.setText("Bạn bè");

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
