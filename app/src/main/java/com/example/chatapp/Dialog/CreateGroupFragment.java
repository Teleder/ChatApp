package com.example.chatapp.Dialog;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activities.MainActivity;
import com.example.chatapp.Adapter.SelectedMemberAdapter;
import com.example.chatapp.Dtos.CreateGroupDto;
import com.example.chatapp.Dtos.GroupDto;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.Model.Group.Member;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Utils.CONSTS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class CreateGroupFragment extends Fragment implements AddMemberDialog.OnMembersSelectedListener {
    private ActivityResultLauncher<Intent> launcher;
    private EditText groupNameEditText;
    private EditText bio;
    private Switch groupPublicSwitch;
    private RoundedImageView avatarPreview;
    private Button addMembersButton;
    private Button createGroupButton;
    RecyclerView rcMember;
    private List<UserBasicDto> selectedMembers = new ArrayList<>();

    private File avatarFile;
    SelectedMemberAdapter adapter;
    private Retrofit retrofit;
    APIService apiService;
    Uri currentFile = null;
    private RetrofitClient retrofitClient;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        currentFile = result.getData().getData();
                        avatarPreview.setImageURI(currentFile);
                    }
                }
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_create_group, container, false);
        retrofitClient = RetrofitClient.getInstance(getContext());
        retrofit = retrofitClient.getRetrofit();
        groupNameEditText = view.findViewById(R.id.group_name);
        groupPublicSwitch = view.findViewById(R.id.group_public_switch);
        avatarPreview = view.findViewById(R.id.avatar_preview);
        addMembersButton = view.findViewById(R.id.add_members_button);
        createGroupButton = view.findViewById(R.id.create_group_button);
        bio = view.findViewById(R.id.group_bio);
        rcMember = view.findViewById(R.id.selected_members_recycler_view);
        avatarPreview.setOnClickListener(v -> selectAvatar());
        addMembersButton.setOnClickListener(v -> addMembers());
        createGroupButton.setOnClickListener(v -> createGroup());
        adapter = new SelectedMemberAdapter(getContext(), selectedMembers);
        rcMember.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcMember.setLayoutManager(layoutManager);
        rcMember.setAdapter(adapter);
        return view;
    }

    private void selectAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    private void addMembers() {
        List<UserBasicDto> contacts = new ArrayList<>();
        // TODO: Fetch your contacts
        AddMemberDialog dialog = new AddMemberDialog(contacts, this);
        dialog.show(getParentFragmentManager(), "AddMemberDialog");
    }


    private void createGroup() {
        String groupName = groupNameEditText.getText().toString();
        boolean isPublic = groupPublicSwitch.isChecked();
        if (groupName.trim().equals("")) {
            Toast.makeText(getContext(), "Please enter a group name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentFile != null) {
            uploadFile(getRealPathFromURI(getContext(), currentFile), "12312312", CONSTS.IMAGE);
        } else {
            List<Member> mem = new ArrayList<>();
            for (UserBasicDto userBasicDto : selectedMembers) {
                mem.add(new Member(userBasicDto.getId()));
            }
            CreateGroupDto createGroupDto = new CreateGroupDto(mem,
                    groupPublicSwitch.isChecked(),
                    null,
                    groupNameEditText.getText().toString(),
                    bio.getText().toString()
            );
            createGroup(createGroupDto);
        }
    }

    @Override
    public void onMembersSelected(List<UserBasicDto> selectedMembers) {
        if (selectedMembers == null)
            return;
        this.selectedMembers.clear();
        this.selectedMembers.addAll(selectedMembers);
        adapter.notifyDataSetChanged();
        Log.d("TAG", "onMembersSelected: " + selectedMembers.size());
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    public void uploadFile(String filePath, String code, String Type) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        java.io.File file = new java.io.File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        apiService.uploadFile(code, filePart).enqueue(new retrofit2.Callback<com.example.chatapp.Model.File.File>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.chatapp.Model.File.File> call, retrofit2.Response<com.example.chatapp.Model.File.File> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.isSuccessful()) {
                            List<Member> mem = new ArrayList<>();
                            for (UserBasicDto userBasicDto : selectedMembers) {
                                mem.add(new Member(userBasicDto.getId()));
                            }
                            CreateGroupDto createGroupDto = new CreateGroupDto(mem,
                                    groupPublicSwitch.isChecked(),
                                    response.body().getUrl(),
                                    groupNameEditText.getText().toString(),
                                    bio.getText().toString()
                            );
                            createGroup(createGroupDto);
                        }

                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.chatapp.Model.File.File> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createGroup(CreateGroupDto createGroupDto) {
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.createGroup(createGroupDto).enqueue(new retrofit2.Callback<GroupDto>() {
            @Override
            public void onResponse(retrofit2.Call<GroupDto> call, retrofit2.Response<GroupDto> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<GroupDto> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

