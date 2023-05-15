package com.example.chatapp.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.MemberAdapter;
import com.example.chatapp.Model.Group.Member;
import com.example.chatapp.R;

import java.util.List;

public class BlockMemberDialog extends DialogFragment {
    private List<Member> members;

    public BlockMemberDialog(List<Member> members) {
        this.members = members;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Block Members");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_block_member, null);
        RecyclerView recyclerView = view.findViewById(R.id.block_member_recyclerview);
        MemberAdapter memberAdapter = new MemberAdapter(members);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(memberAdapter);

        Button closeButton = view.findViewById(R.id.close_dialog);
        closeButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }
}
