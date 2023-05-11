package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.chatapp.Adapter.SearchAdapter;
import com.example.chatapp.Adapter.WaitingAcceptContactAdapter;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.UserSearchDto;
import com.example.chatapp.Model.User.Contact;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rcListSearch;
    SearchView searchView;
    SearchAdapter searchAdapter;
    List<UserSearchDto> arrayList;
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    SharedPrefManager sharedPrefManager;
    APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rcListSearch  = findViewById(R.id.rcListSearch);
        searchView = findViewById(R.id.searchView);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterListener(s);
                return false;
            }
        });
    }
    private void filterListener(String text)
    {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.SearchFriend(text).enqueue(new Callback<List<UserSearchDto>>() {
            @Override
            public void onResponse(Call<List<UserSearchDto>> call, Response<List<UserSearchDto>> response) {
                if (response.isSuccessful()) {
                    try {
                        arrayList = response.body();
                        searchAdapter = new SearchAdapter(getApplicationContext(), arrayList);
                        rcListSearch.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        rcListSearch.setLayoutManager(layoutManager);
                        rcListSearch.setAdapter(searchAdapter);
                        searchAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Call api success", Toast.LENGTH_SHORT).show();
//                        finish();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserSearchDto>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}