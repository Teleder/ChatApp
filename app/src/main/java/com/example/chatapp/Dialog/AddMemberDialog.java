package com.example.chatapp.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.ContactRequestAdapter;
import com.example.chatapp.Adapter.ContactRequestAdapter;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.R;

import java.util.List;

public class AddMemberDialog extends DialogFragment {
    private final List<UserBasicDto> contacts;
    private ContactRequestAdapter adapter;

    public AddMemberDialog(List<UserBasicDto> contacts) {
        this.contacts = contacts;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add members");

        // create the view for the dialog
        View view;
        view = getActivity().getLayoutInflater().inflate(R.layout.add_member_dialog, null);
        RecyclerView contactRecyclerView = view.findViewById(R.id.contact_recycler_view);
        adapter = new ContactRequestAdapter(contacts);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactRecyclerView.setAdapter(adapter);

        // when the add member button is clicked
        Button addMemberButton = view.findViewById(R.id.add_member);
        addMemberButton.setOnClickListener(v -> {
            List<UserBasicDto> selectedContacts = adapter.getSelectedContacts();
            // Here you can call your API to add the selected contacts to the group
            // After successful addition, you can dismiss the dialog
            // dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}