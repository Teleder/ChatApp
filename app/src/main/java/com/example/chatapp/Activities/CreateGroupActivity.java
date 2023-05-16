package com.example.chatapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.Dialog.AddMemberDialog;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity implements AddMemberDialog.OnMembersSelectedListener {
public CreateGroupActivity() {
        super();
    }
    private EditText groupNameEditText;
    private Switch groupPublicSwitch;
    private RoundedImageView avatarPreview;
    private Button addMembersButton;
    private Button createGroupButton;
    private List<UserBasicDto> selectedMembers = new ArrayList<>();

    private File avatarFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupNameEditText = findViewById(R.id.group_name);
        groupPublicSwitch = findViewById(R.id.group_public_switch);
        avatarPreview = findViewById(R.id.avatar_preview);
        addMembersButton = findViewById(R.id.add_members_button);
        createGroupButton = findViewById(R.id.create_group_button);

        avatarPreview.setOnClickListener(v -> selectAvatar());
        addMembersButton.setOnClickListener(v -> addMembers());
        createGroupButton.setOnClickListener(v -> createGroup());
    }

    private void selectAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            avatarPreview.setImageURI(selectedImage);

        }
    }

    private void addMembers() {
        List<UserBasicDto> contacts = new ArrayList<>();
        // TODO: Fetch your contacts
        AddMemberDialog dialog = new AddMemberDialog(contacts, this);
        dialog.show(getSupportFragmentManager(), "AddMemberDialog");
    }

    @Override
    public void onMembersSelected(List<UserBasicDto> selectedMembers) {
        this.selectedMembers = selectedMembers;
    }

    private void createGroup() {
        String groupName = groupNameEditText.getText().toString();
        boolean isPublic = groupPublicSwitch.isChecked();
        if (groupName.trim().equals("")) {
            Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
