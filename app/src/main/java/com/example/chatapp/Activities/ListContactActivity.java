package com.example.chatapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.ContactAdapter;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListContactActivity extends Fragment {
    SharedPrefManager sharedPrefManager;
    APIService apiService;
    RecyclerView rcIcon;
    List<UserBasicDto> arrayList;
    ContactAdapter contactAdapter;
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;

    public ListContactActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_contact, container, false);
        Context context = getActivity();
        retrofitClient = RetrofitClient.getInstance(view.getContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(view.getContext());
        rcIcon = view.findViewById(R.id.rcContacts);
        getContacts(view);
        return view;
    }

    private void getContacts(View view) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.getContacts("", 0, 1000).enqueue(new Callback<PagedResultDto<UserBasicDto>>() {
            @Override
            public void onResponse(Call<PagedResultDto<UserBasicDto>> call, Response<PagedResultDto<UserBasicDto>> response) {
                if (response.isSuccessful()) {
                    try {
                        arrayList = response.body().getData();
                        contactAdapter = new ContactAdapter(view.getContext(), arrayList);
                        rcIcon.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                        rcIcon.setLayoutManager(layoutManager);
                        rcIcon.setAdapter(contactAdapter);
                        contactAdapter.notifyDataSetChanged();
                        Toast.makeText(view.getContext(), "Call api success", Toast.LENGTH_SHORT).show();
//                        finish();
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
}