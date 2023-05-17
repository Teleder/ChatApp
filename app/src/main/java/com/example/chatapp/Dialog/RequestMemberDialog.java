package com.example.chatapp.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.MembersAdapter;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestMemberDialog extends DialogFragment {
    private String groupId;
    private RetrofitClient retrofitClient;
    private Retrofit retrofit;
    APIService apiService;
    List<UserBasicDto> members;
    RecyclerView recyclerView;

    public RequestMemberDialog(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Request Members");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_request_member, null);
        retrofitClient = RetrofitClient.getInstance(view.getContext());
        retrofit = retrofitClient.getRetrofit();
        getListMembers(groupId, view);
        recyclerView = view.findViewById(R.id.request_member_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Button closeButton = view.findViewById(R.id.close_dialog);
        closeButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }

    public void getListMembers(String groupId, View view) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.getWaitingAccept(groupId).enqueue(new Callback<List<UserBasicDto>>() {
            @Override
            public void onResponse(Call<List<UserBasicDto>> call, Response<List<UserBasicDto>> response) {
                if (response.isSuccessful()) {
                    try {
                        members = response.body();
                        MembersAdapter memberAdapter = new MembersAdapter(view.getContext(),members, groupId);
                        recyclerView.setAdapter(memberAdapter);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(view.getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserBasicDto>> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
