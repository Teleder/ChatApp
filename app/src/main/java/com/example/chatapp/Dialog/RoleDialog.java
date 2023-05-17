package com.example.chatapp.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.FriendMessAdapter;
import com.example.chatapp.Adapter.RoleAdapter;
import com.example.chatapp.Adapter.SearchAdapter;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Model.Group.Role;
import com.example.chatapp.Model.Permission.Action;
import com.example.chatapp.Model.Permission.Permission;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RoleDialog extends DialogFragment {
    SharedPrefManager sharedPrefManager;
    APIService apiService;
    RecyclerView rcIcon;
    List<UserBasicDto> arrayList;
    RoleAdapter friendMessAdapter;
    SearchView searchView;
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    List<UserBasicDto> members;
    String groupId;

    public RoleDialog(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Friend");

        // create the view for the dialog
        View view = getActivity().getLayoutInflater().inflate(R.layout.role_dialog, null);
        retrofitClient = RetrofitClient.getInstance(view.getContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(view.getContext());
        rcIcon = view.findViewById(R.id.rcListSearch);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        getListMembers(groupId,view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getContacts(s, view);
                return false;
            }
        });
        builder.setView(view);

        return builder.create();
    }
    private void getContacts(String s, View view) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.getContacts(s, 0, 1000).enqueue(new Callback<PagedResultDto<UserBasicDto>>() {
            @Override
            public void onResponse(Call<PagedResultDto<UserBasicDto>> call, Response<PagedResultDto<UserBasicDto>> response) {
                if (response.isSuccessful()) {
                    try {
                        arrayList = response.body().getData();
                        friendMessAdapter = new RoleAdapter(view.getContext(), arrayList, members, groupId);
                        rcIcon.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                        rcIcon.setLayoutManager(layoutManager);
                        rcIcon.setAdapter(friendMessAdapter);
                        friendMessAdapter.notifyDataSetChanged();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(view.getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PagedResultDto<UserBasicDto>> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getListMembers(String groupId, View view) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.getNonBlockedNonMemberFriends(groupId).enqueue(new Callback<List<UserBasicDto>>() {
            @Override
            public void onResponse(Call<List<UserBasicDto>> call, Response<List<UserBasicDto>> response) {
                if (response.isSuccessful()) {
                    try {
                        members = response.body();
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
