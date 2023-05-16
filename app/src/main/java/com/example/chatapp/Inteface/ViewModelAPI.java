package com.example.chatapp.Inteface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.Activities.MainActivity;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.List;

public class ViewModelAPI extends ViewModel {


    private MutableLiveData<List<Conservation>> conversationsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> groupsLiveData = new MutableLiveData<>();
    private APIService apiService = MainActivity.getApiService();
    private RetrofitClient retrofitClient = MainActivity.getRetrofitClient();
    SharedPrefManager sharedPrefManager = MainActivity.getSharedPrefManager();

    public LiveData<List<Conservation>> getConversationsLiveData() {
        return conversationsLiveData;
    }

    public LiveData<List<String>> getGroupsLiveData() {
        return groupsLiveData;
    }

    public void fetchConversations() {
        if (sharedPrefManager.getListConservation() != null) {
            conversationsLiveData.setValue(null);
            return;
        }
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.getMyConversations(0, 100).enqueue(new retrofit2.Callback<PagedResultDto<Conservation>>() {
            @Override
            public void onResponse(retrofit2.Call<PagedResultDto<Conservation>> call, retrofit2.Response<PagedResultDto<Conservation>> response) {
                if (response.isSuccessful()) {
                    try {
                        conversationsLiveData.setValue(response.body().getData());
                        sharedPrefManager.saveListConservation(response.body().getData());
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PagedResultDto<Conservation>> call, Throwable t) {
            }
        });
    }

    public void fetchGroups() {
        if (sharedPrefManager.getListGroupId() != null ) {
            groupsLiveData.setValue(null);
            return;
        }
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllIdConservationGroup().enqueue(new retrofit2.Callback<List<String>>() {
            @Override
            public void onResponse(retrofit2.Call<List<String>> call, retrofit2.Response<List<String>> response) {
                if (response.isSuccessful()) {
                    try {
                        sharedPrefManager.saveListGroupId(response.body());
                        groupsLiveData.setValue(response.body());
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<String>> call, Throwable t) {
            }
        });

    }

}
