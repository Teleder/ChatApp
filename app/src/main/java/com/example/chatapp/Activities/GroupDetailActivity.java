package com.example.chatapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.Dialog.BlockMemberDialog;
import com.example.chatapp.Dialog.RequestMemberDialog;
import com.example.chatapp.Dialog.RoleDialog;
import com.example.chatapp.Model.Group.Member;
import com.example.chatapp.Model.Group.Role;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    private Button btnBlockMembers;
    private Button btnMembers;
    private Button btnRequestMembers;
    private Button btnRoles;
    private Button btnSetPublicPrivate;
    private Button btnDeleteGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");
        btnBlockMembers = findViewById(R.id.btn_block_members);
        btnMembers = findViewById(R.id.btn_members);
        btnRequestMembers = findViewById(R.id.btn_request_members);
        btnRoles = findViewById(R.id.btn_roles);
        btnSetPublicPrivate = findViewById(R.id.btn_set_public_private);
        btnDeleteGroup = findViewById(R.id.btn_delete_group);

        btnBlockMembers.setOnClickListener(v -> showBlockMembersDialog());
        btnMembers.setOnClickListener(v -> showMembersDialog(groupId));
        btnRequestMembers.setOnClickListener(v -> showRequestMembersDialog(groupId));
        btnRoles.setOnClickListener(v -> showAddMemberDialog(groupId));
        btnSetPublicPrivate.setOnClickListener(v -> setGroupPublicPrivate());
        btnDeleteGroup.setOnClickListener(v -> deleteGroup());
    }

    private void showBlockMembersDialog() {
        // TODO: get the list of blocked members
        List<Member> blockedMembers = new ArrayList<>();
        // show the dialog
        BlockMemberDialog dialog = new BlockMemberDialog(blockedMembers);
        dialog.show(getSupportFragmentManager(), "BlockMemberDialog");
    }

    private void showMembersDialog(String groupId) {
        // show the dialog
        RequestMemberDialog dialog = new RequestMemberDialog(groupId);
        dialog.show(getSupportFragmentManager(), "MemberDialog");
    }

    private void showRequestMembersDialog(String groupId) {
        // show the dialog
        RequestMemberDialog dialog = new RequestMemberDialog(groupId);
        dialog.show(getSupportFragmentManager(), "RequestMemberDialog");
    }

    private void showAddMemberDialog(String groupId) {
        // TODO: get the list of roles
        List<Role> roles = new ArrayList<>();
        // show the dialog
        RoleDialog dialog = new RoleDialog(groupId);
        dialog.show(getSupportFragmentManager(), "RolesDialog");
    }

    private void setGroupPublicPrivate() {
        // TODO: implement set group public/private
    }

    private void deleteGroup() {
        // TODO: implement delete group
    }
}
