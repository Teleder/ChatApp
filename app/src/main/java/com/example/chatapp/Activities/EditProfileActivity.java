package com.example.chatapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chatapp.Dtos.UpdateInfoUserDto;
import com.example.chatapp.Dtos.UserDto;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity {
    public static final int MY_REQUEST_CODE = 100;
    public static String[] storge_permissions = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
    };
    ImageView editImage;
    EditText editEmail, editPhone, editFirstName, editLastName, editBio;
    SharedPrefManager sharedPrefManager;
    APIService apiService;
    UserProfileDto userProfileDto;
    private Uri mUri;
    private RetrofitClient retrofitClient;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            editImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
    );

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        userProfileDto = sharedPrefManager.getUser();
        AnhXa(userProfileDto);

        findViewById(R.id.imageCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(EditProfileActivity.this, ProfileActivity.class)});
            }
        });
        findViewById(R.id.imageCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
//                startActivities(new Intent[]{new Intent(EditProfileActivity.this, ProfileActivity.class)});
            }
        });
        findViewById(R.id.tv_editimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermission();
            }
        });
    }

    private void updateProfile() {
        final String fName = editFirstName.getText().toString();
        final String lName = editLastName.getText().toString();
        final String emailUser = editEmail.getText().toString();
        final String phoneUser = editPhone.getText().toString();
        final String bioUser = editBio.getText().toString();
        if (TextUtils.isEmpty(fName)) {
            editFirstName.setError("Please enter your firstname");
            editFirstName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lName)) {
            editLastName.setError("Please enter your lastname");
            editLastName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(bioUser)) {
            editBio.setError("Please enter your bio");
            editBio.requestFocus();
            return;
        }
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        File file = new File();

//        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
//        File file = new File(IMAGE_PATH);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part partbodyavatar = MultipartBody.Part.createFormData(CONSTS.MY_IMAGES, file.getName(), requestFile);
        UpdateInfoUserDto updateInfoUserDto = new UpdateInfoUserDto(fName, lName, phoneUser, emailUser, bioUser);
        apiService.updateProfile(updateInfoUserDto).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.isSuccessful()) {
                    try {
                        userProfileDto.setFirstName(response.body().getFirstName());
                        userProfileDto.setLastName(response.body().getLastName());
                        userProfileDto.setBio(response.body().getBio());
                        userProfileDto.setDisplayName(response.body().getFirstName() + " " + response.body().getLastName());
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        sharedPrefManager.saveUser(userProfileDto);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa(UserProfileDto userProfileDto) {
        editImage = findViewById(R.id.imgUser);
        editPhone = findViewById(R.id.editphone);
        editFirstName = findViewById(R.id.editfirstname);
        editLastName = findViewById(R.id.editlastname);
        editBio = findViewById(R.id.editbio);
        editEmail = findViewById(R.id.editemail);
        editPhone.setText(userProfileDto.getPhone());
        editBio.setText(userProfileDto.getBio());
        editFirstName.setText(userProfileDto.getFirstName());
        editLastName.setText(userProfileDto.getLastName());
        editEmail.setText(userProfileDto.getEmail());
        if (userProfileDto.getAvatar() == null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Glide.with(getApplicationContext()).load(getApplicationContext().getDrawable(R.drawable.user)).into(editImage);
            } else {
                Glide.with(getApplicationContext()).load(userProfileDto.getAvatar()).into(editImage);
            }
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissions(permissions(), MY_REQUEST_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}