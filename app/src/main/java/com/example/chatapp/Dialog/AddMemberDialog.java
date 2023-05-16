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

import com.example.chatapp.Adapter.ContactRequestAdapter;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;

import java.util.List;

import retrofit2.Retrofit;

public class AddMemberDialog extends DialogFragment {
    private final List<UserBasicDto> contacts;
    private ContactRequestAdapter adapter;
    private OnMembersSelectedListener listener;
    private Retrofit retrofit;
    APIService apiService;
    private RetrofitClient retrofitClient;
    SharedPrefManager sharedPrefManager;

    public AddMemberDialog(List<UserBasicDto> contacts, OnMembersSelectedListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    public interface OnMembersSelectedListener {
        void onMembersSelected(List<UserBasicDto> selectedMembers);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sharedPrefManager = new SharedPrefManager(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add members");

        // create the view for the dialog
        View view;
        view = getActivity().getLayoutInflater().inflate(R.layout.add_member_dialog, null);
        RecyclerView contactRecyclerView = view.findViewById(R.id.contact_recycler_view);

        retrofitClient = RetrofitClient.getInstance(getContext());
        retrofit = retrofitClient.getRetrofit();
        fetchMemberPotential();


        adapter = new ContactRequestAdapter(contacts);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactRecyclerView.setAdapter(adapter);
        // when the add member button is clicked
        Button addMemberButton = view.findViewById(R.id.add_member);
        addMemberButton.setOnClickListener(v -> {
            List<UserBasicDto> selectedContacts = adapter.getSelectedContacts();
            listener.onMembersSelected(selectedContacts);
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }

    private void fetchMemberPotential() {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.getContacts("", 0, 100).enqueue(new retrofit2.Callback<PagedResultDto<UserBasicDto>>() {
            @Override
            public void onResponse(retrofit2.Call<PagedResultDto<UserBasicDto>> call, retrofit2.Response<PagedResultDto<UserBasicDto>> response) {
                if (response.isSuccessful()) {
                    try {
                        contacts.clear();
                        contacts.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "fail send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PagedResultDto<UserBasicDto>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}