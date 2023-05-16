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
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WaitingAcceptContactAdapter extends RecyclerView.Adapter<WaitingAcceptContactAdapter.ContactHolder> {
    private Context context;
    private List<Contact> arrList;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;

    public WaitingAcceptContactAdapter(Context context, List<Contact> arrList) {
        this.context = context;
        this.arrList = arrList;
        retrofitClient = RetrofitClient.getInstance(context);
        retrofit = retrofitClient.getRetrofit();
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.item_contact_request_sent, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        Contact contactModel = arrList.get(position);
        if (contactModel.getUser().getAvatar() != null)
            Glide.with(context).load(contactModel.getUser().getAvatar()).into(holder.avatar);
        holder.tvDisplayName.setText(contactModel.getUser().getDisplayName());
        holder.tvBio.setText(contactModel.getUser().getBio());
        holder.userId.setText(contactModel.getUser().getId());
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelAcceptContact(holder.userId.getText().toString(), holder);
//                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptContact(holder.userId.getText().toString(), holder);
//                Toast.makeText(context, "accept", Toast.LENGTH_SHORT).show();
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
        private Button btnCancel, btnAccept;

        public ContactHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgUser);
            tvDisplayName = itemView.findViewById(R.id.tvNameUser);
            tvBio = itemView.findViewById(R.id.tvBioUser);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            userId = itemView.findViewById(R.id.id_User);
        }
    }

    public void setListenerList(List<Contact> contactModelList) {
        this.arrList = contactModelList;
        notifyDataSetChanged();
    }
    private void CancelAcceptContact(String contactId, ContactHolder holder)
    {
        apiService = retrofit.create(APIService.class);
        apiService.AcceptContact(contactId, false).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        holder.btnCancel.setVisibility(View.GONE);
                        holder.btnAccept.setVisibility(View.VISIBLE);
                        holder.btnAccept.setText("Đã hủy yêu cầu");
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
    private void AcceptContact(String contactId, ContactHolder holder)
    {
        apiService = retrofit.create(APIService.class);
        apiService.AcceptContact(contactId, true).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    try {
                        holder.btnCancel.setVisibility(View.GONE);
                        holder.btnAccept.setVisibility(View.VISIBLE);
                        holder.btnAccept.setText("Bạn bè");
//                        Toast.makeText(context, "accept success", Toast.LENGTH_SHORT).show();
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
