package com.example.chatapp.Activities;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapter.ChatAdapter;
import com.example.chatapp.Dialog.ImagePreviewDialog;
import com.example.chatapp.Dialog.VideoPreviewDialog;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.PayloadAction;
import com.example.chatapp.Dtos.PayloadMessage;
import com.example.chatapp.Dtos.SocketPayload;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.User;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Retrofit.WebSocketManager;
import com.example.chatapp.Utils.CONSTS;
import com.example.chatapp.Utils.MessageObserver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Retrofit;
import retrofit2.http.Body;


public class ChatActivity extends AppCompatActivity implements MessageObserver {
    private ProgressBar processBar;
    private RetrofitClient retrofitClient;
    RecyclerView rcMessages;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    private VideoView videoPreview;
    private ImageView imagePreview, ic_active;
    private TextView txtStatus, txtName;

    SharedPrefManager sharedPrefManager;
    APIService apiService;
    ChatAdapter chatAdapter;
    ArrayList<Message> messages = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private MediaRecorder recorder;
    private FrameLayout layoutAttach;
    private FrameLayout layoutAudio;
    private FrameLayout layoutEmoji;
    private FrameLayout layoutSend;
    private ImageView imageBack;
    private long totalMessage = 21;
    private int nextPage = 0;
    private int limitMessage = 20;
    com.example.chatapp.Model.File.File currentFile;
    EmojiPopup emojiPopup;
    ArrayList<Uri> listFile = new ArrayList<>();
    EditText inputMessage;
    Message newMessage;
    User contact;
    private Handler typingHandler = new Handler();
    private Runnable typingRunnable = new Runnable() {
        @Override
        public void run() {
            PayloadAction payloadAction = new PayloadAction();
            Conservation currentConservation = sharedPrefManager.getCurrentConservation();
            payloadAction.setCode(currentConservation.getCode());
            payloadAction.setAction(CONSTS.STOP_CHATTING);
            payloadAction.setReceiverId(contact.getId());
            payloadAction.setReceiverType(CONSTS.MESSAGE_PRIVATE);
            sendAction(payloadAction);
        }
    };

    @Override
    public void onMessageReceived(String message) {
        // Handle received message here
        Gson gson = new Gson();
        Type socketPayloadType = new TypeToken<SocketPayload<Object>>() {
        }.getType();
        SocketPayload socketPayload = gson.fromJson(message, socketPayloadType);
        if (socketPayload == null || socketPayload.getType() == null)
            return;
        if ( socketPayload.getType().equals(CONSTS.MESSAGE_PRIVATE) || socketPayload.getType().equals(CONSTS.MESSAGE_GROUP)) {
            gson = new Gson();
            String json = gson.toJson(socketPayload.getData());
            Message mess = gson.fromJson(json, Message.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messages.add(mess);
                    chatAdapter.notifyDataSetChanged();
                    rcMessages.scrollToPosition(chatAdapter.getItemCount() - 1);
                }
            });
        } else if (socketPayload.getType() != null && socketPayload.getType().equals(CONSTS.EDIT_MESSAGE)) {
            gson = new Gson();
            String json = gson.toJson(socketPayload.getData());
            Message mess = gson.fromJson(json, Message.class);
            for (Message mes : messages) {
                if (mess.getId().equals(mes.getId())) {
                    mes = mess;
                    break;
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                }
            });
        } else if (socketPayload.getType() != null && socketPayload.getType().equals(CONSTS.CHATTING)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contact.setActive(true);
                    txtStatus.setText("Active now");
                    ic_active.setVisibility(View.VISIBLE);
                    txtStatus.setText("Typing...");
                }
            });

        } else if (socketPayload.getType() != null && socketPayload.getType().equals(CONSTS.STOP_CHATTING)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (contact.isActive()) {
                        txtStatus.setText("Active now");
                        ic_active.setVisibility(View.VISIBLE);
                    } else {
                        txtStatus.setText("Active " + getElapsedTime(contact.getLastActiveAt()) + " ago");
                        ic_active.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        WebSocketManager.getInstance(ChatActivity.this).removeObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        WebSocketManager.getInstance(ChatActivity.this).addObserver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        tokenManager = retrofitClient.getTokenManager();
        retrofit = retrofitClient.getRetrofit();
        sharedPrefManager = new SharedPrefManager(ChatActivity.this);
        // Initialize views
        layoutAttach = findViewById(R.id.layoutAttach);
        layoutAudio = findViewById(R.id.layoutAudio);
        layoutEmoji = findViewById(R.id.layoutEmoji);
        layoutSend = findViewById(R.id.layoutSend);
        imageBack = findViewById(R.id.imageBack);
        inputMessage = findViewById(R.id.inputMessage);
        videoPreview = findViewById(R.id.videoPreview);
        imagePreview = findViewById(R.id.imagePreview);
        rcMessages = (RecyclerView) findViewById(R.id.rcMessages);
        processBar = findViewById(R.id.processBar);
        txtName = findViewById(R.id.txtName);
        txtStatus = findViewById(R.id.txt_status);
        ic_active = findViewById(R.id.ic_active);
        contact = sharedPrefManager.getCurrentConservation().getUserId_1().
                equals(sharedPrefManager.getUser().getId()) ? sharedPrefManager.getCurrentConservation().getUser_2() : sharedPrefManager.getCurrentConservation().getUser_1();
        txtName.setText(contact.getDisplayName());
        if (contact.isActive()) {
            //Active X minutes ago
            txtStatus.setText("Active now");
            ic_active.setVisibility(View.VISIBLE);
        } else {
            txtStatus.setText("Active " + getElapsedTime(contact.getLastActiveAt()) + " ago");
            ic_active.setVisibility(View.GONE);
        }
        EmojiManager.install(new GoogleEmojiProvider());

//        rcMessages.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(rcMessages, this, messages, new ChatAdapter.MessageClickListener() {
            @Override
            public void onImageClicked(String imageUrl) {
                Dialog dialog = new Dialog(ChatActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.dialog_image_preview);
                ImageView imagePreview = dialog.findViewById(R.id.imagePreview);
                ImageButton btnClose = dialog.findViewById(R.id.btnClose);
                Glide.with(ChatActivity.this).load(imageUrl.replace("localhost:8080", "http://" + CONSTS.BASEURL)).into(imagePreview);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            @Override
            public void onAudioClicked(String audioUrl, TextView audioDuration) {
                playAudio(audioUrl.replace("localhost:8080", "http://" + CONSTS.BASEURL), audioDuration);
            }

            @Override
            public void onVideoClicked(String videoUrl) {
//                playVideo(videoUrl.replace("localhost:8080", "http://" + CONSTS.BASEURL));
                VideoPreviewDialog dialog = new VideoPreviewDialog(videoUrl.replace("localhost:8080", "http://" + CONSTS.BASEURL));
                dialog.show(getSupportFragmentManager(), "VideoPreviewDialog");
            }

            @Override
            public void onFileClicked(String videoUrl, String fileName) {
                downloadFile(videoUrl.replace("localhost:8080", "http://" + CONSTS.BASEURL), fileName);
            }

        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rcMessages.setLayoutManager(layoutManager);
        rcMessages.setAdapter(chatAdapter);
        loadMoreMessage();
        rcMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null && !processBar.isShown()) {
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    // Load more condition
                    if (firstVisibleItemPosition == 0) {
                        loadMoreMessage();
                    }
                }
            }
        });

        inputMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiPopup != null && emojiPopup.isShowing()) {
                    emojiPopup.dismiss(); // Toggles visibility of the Popup.
                }
            }
        });

        inputMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                typingHandler.removeCallbacks(typingRunnable);
                PayloadAction payloadAction = new PayloadAction();
                Conservation currentConservation = sharedPrefManager.getCurrentConservation();
                payloadAction.setCode(currentConservation.getCode());
                payloadAction.setAction(CONSTS.CHATTING);
                payloadAction.setReceiverId(contact.getId());
                payloadAction.setReceiverType(CONSTS.MESSAGE_PRIVATE);
                sendAction(payloadAction);
            }

            @Override
            public void afterTextChanged(Editable s) {
                typingHandler.removeCallbacks(typingRunnable);
                typingHandler.postDelayed(typingRunnable, 2000);
                if (s.toString().isEmpty()) {
                    // No text is present, show attach and audio layouts, hide send layout
                    layoutAttach.setVisibility(View.VISIBLE);
                    layoutAudio.setVisibility(View.VISIBLE);
                    layoutSend.setVisibility(View.GONE);
                } else {
                    // Text is present, hide attach and audio layouts, show send layout
                    layoutAttach.setVisibility(View.GONE);
                    layoutAudio.setVisibility(View.GONE);
                    layoutSend.setVisibility(View.VISIBLE);
                }

            }
        });
        // Attach listeners
        layoutAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        layoutAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudioRecording();
            }
        });

        layoutEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmojiPanel();
            }
        });

        layoutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivities(new Intent[]{new Intent(ChatActivity.this, MainActivity.class)});

            }
        });
        findViewById(R.id.imageInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(ChatActivity.this, SettingChatFriendActivity.class)});
            }
        });
    }

    private void startAudioRecording() {
        RelativeLayout layoutAudioRecording = findViewById(R.id.layoutAudioRecording);
        TextView tvAudioTimeline = findViewById(R.id.tvAudioTimeline);
        ImageButton btnCancelAudio = findViewById(R.id.btnCancelAudio);
        ImageButton btnSendAudio = findViewById(R.id.btnSendAudio);
        ImageButton btnStopAudio = findViewById(R.id.btnStopAudio);
        final Handler handler = new Handler();
        final long[] startTime = {0};

        // Create a runnable to update the recording time
        final Runnable runnable = new Runnable() {
            public void run() {
                if (recorder != null) {
                    // Calculate elapsed time
                    long elapsedTime = System.currentTimeMillis() - startTime[0];

                    // Format the time and update the timeline TextView
                    String time = String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
                            TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)));
                    tvAudioTimeline.setText(time);

                    // Post the runnable to run again after 1 second
                    handler.postDelayed(this, 1000);
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    200);
        }
        if (recorder == null) {
            layoutAudioRecording.setVisibility(View.VISIBLE);
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/audio.3gp");

            btnStopAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Stop recording but keep the audio file for later use
                    if (recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;

                        // Remove any pending posts of Runnable r that are in the message queue
                        handler.removeCallbacks(runnable);
                    }
                }
            });
            btnCancelAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                        new File(getExternalCacheDir().getAbsolutePath() + "/audio.3gp").delete();

                        // Remove any pending posts of Runnable r that are in the message queue
                        handler.removeCallbacks(runnable);
                    }

                    // Hide audio recording layout
                    layoutAudioRecording.setVisibility(View.GONE);
                }
            });
            btnSendAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // If recording, stop recording and send the audio file
                    if (recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                        // TODO: Send the audio file
                        String filePath = getExternalCacheDir().getAbsolutePath() + "/audio.3gp"; // Đường dẫn tệp âm thanh đã ghi
                        uploadFile(filePath, sharedPrefManager.getCurrentConservation() != null ? sharedPrefManager.getCurrentConservation().getCode() : "12021", CONSTS.AUDIO);

                        // Remove any pending posts of Runnable r that are in the message queue
                        handler.removeCallbacks(runnable);
                    }
                    // Hide audio recording layout
                    layoutAudioRecording.setVisibility(View.GONE);
                }
            });
            try {
                recorder.prepare();
                recorder.start();
                startTime[0] = System.currentTimeMillis();
                handler.post(runnable);
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        } else {
            recorder.stop();
            recorder.release();
            recorder = null;
        }

    }

    private void openEmojiPanel() {
        try {
            if (emojiPopup != null && emojiPopup.isShowing())
                emojiPopup.dismiss();
            else {
                emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.layoutAudioRecording)).build(inputMessage);
                emojiPopup.toggle(); // Toggles visibility of the Popup.
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputMessage.getText().toString();
        if (!message.isEmpty()) {
            if (sharedPrefManager.getCurrentConservation().getGroupId() == null || sharedPrefManager.getCurrentConservation().getGroupId().trim().equals("")) {
                sendPrivateMessage(sharedPrefManager.getCurrentConservation().getUserId_1().equals(sharedPrefManager.getUser().getId()) ? sharedPrefManager.getCurrentConservation().getUserId_2() : sharedPrefManager.getCurrentConservation().getUserId_1(),
                        new PayloadMessage(inputMessage.getText().toString(), sharedPrefManager.getCurrentConservation().getCode(), CONSTS.MESSAGE_PRIVATE, null, null, null)
                );
                Log.d("user1", sharedPrefManager.getCurrentConservation().getUserId_1() + "");
                Log.d("user2", sharedPrefManager.getCurrentConservation().getUserId_2() + "");
                Log.d("current", sharedPrefManager.getUser().getId() + "");
                inputMessage.setText("");  // Clear the input field
            }

        }
    }

    private void openFileChooser() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // If permission is already granted
            openFilePicker();
        } else {
            // If permission is not granted, ask for it
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your files",
                    REQUEST_CODE_PERMISSIONS,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void openFilePicker() {
        FilePickerBuilder.getInstance()
                .setMaxCount(10) // Set max file count
                .setActivityTitle("Please select media") // Set title
                .enableVideoPicker(true) // Enable video picker
                .enableCameraSupport(true) // Enable camera support
                .showGifs(true) // Show GIFs
                .showFolderView(true) // Show folder view
                .enableSelectAll(true) // Enable select all
                .enableImagePicker(true) // Enable image picker
                .setActivityTheme(R.style.CustomTheme)
                .setSelectedFiles(listFile) // Set selected files
                .pickPhoto(this); // Start file picker
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSIONS)
    private void onPermissionsGranted() {
        openFilePicker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            List<Uri> arrayList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            for (Uri uri : arrayList) {
                uploadFile(getRealPathFromURI(ChatActivity.this, uri),
                        sharedPrefManager.getCurrentConservation() != null ? sharedPrefManager.getCurrentConservation().getCode() : "12021", determineFileCategory(uri));
            }
        }
    }

    private void loadMoreMessage() {
        if ((nextPage - 1) * limitMessage >= totalMessage)
            return;
        processBar.setVisibility(View.VISIBLE);
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.findMessagesWithPaginationAndSearch(
                sharedPrefManager.getCurrentConservation().getCode(), nextPage,
                limitMessage, "").enqueue(new retrofit2.Callback<PagedResultDto<Message>>() {
            @Override
            public void onResponse(retrofit2.Call<PagedResultDto<Message>> call, retrofit2.Response<PagedResultDto<Message>> response) {
                if (response.isSuccessful()) {
                    try {
                        int currentScrollPosition = ((LinearLayoutManager) rcMessages.getLayoutManager()).findFirstVisibleItemPosition();
                        totalMessage = response.body().getPagination().getTotal();
                        nextPage += 1;
                        messages.addAll(0, response.body().getData());
                        chatAdapter.notifyDataSetChanged();
                        ((LinearLayoutManager) rcMessages.getLayoutManager()).scrollToPositionWithOffset(currentScrollPosition + response.body().getData().size(), 0);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    processBar.setVisibility(View.GONE);
                } else {
                    processBar.setVisibility(View.GONE);
                    Toast.makeText(ChatActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PagedResultDto<Message>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadFile(String filePath, String code, String Type) {
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.uploadFile(code, filePart).enqueue(new retrofit2.Callback<com.example.chatapp.Model.File.File>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.chatapp.Model.File.File> call, retrofit2.Response<com.example.chatapp.Model.File.File> response) {
                if (response.isSuccessful()) {
                    try {
                        currentFile = response.body();
                        if (sharedPrefManager.getCurrentConservation().getGroupId() == null || sharedPrefManager.getCurrentConservation().getGroupId().trim().equals("")) {
                            currentFile.setCreateAt(null);
                            currentFile.setUpdateAt(null);
                            sendPrivateMessage(sharedPrefManager.getCurrentConservation().getUserId_1() == sharedPrefManager.getUser().getId() ? sharedPrefManager.getCurrentConservation().getUserId_2() : sharedPrefManager.getCurrentConservation().getUserId_1(),
                                    new PayloadMessage(currentFile.getUrl(), sharedPrefManager.getCurrentConservation().getCode(), Type, null, null, currentFile)
                            );
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.chatapp.Model.File.File> call, Throwable t) {
                Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendPrivateMessage(String recipientId, PayloadMessage message) {
        apiService.sendPrivateMessage(recipientId, message).enqueue(new retrofit2.Callback<Message>() {
            @Override
            public void onResponse(retrofit2.Call<Message> call, retrofit2.Response<Message> response) {
                if (response.isSuccessful()) {
                    try {
                        newMessage = response.body();
                        messages.add(newMessage);
                        chatAdapter.notifyDataSetChanged();
                        rcMessages.scrollToPosition(chatAdapter.getItemCount() - 1);

                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "fail send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Message> call, Throwable t) {
                Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendAction(PayloadAction action) {
        apiService.sendAction(action).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                } else {
                    Toast.makeText(ChatActivity.this, "fail send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void playAudio(String audioUrl, TextView audioDuration) {
        Log.d("audioUrl", audioUrl);
        final MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();

            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer.isPlaying()) {
                        int minutes = (mediaPlayer.getCurrentPosition() / 1000) / 60;
                        int seconds = (mediaPlayer.getCurrentPosition() / 1000) % 60;
                        audioDuration.setText(String.format("%02d:%02d", minutes, seconds));
                    }
                    handler.postDelayed(this, 1000);
                }
            };
            handler.postDelayed(runnable, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String fileUrl, String fileName) {
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(fileUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(fileName);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(this, DIRECTORY_DOWNLOADS, fileName);
        downloadmanager.enqueue(request);
    }

    private static String getElapsedTime(Date lastActive) {
        long seconds = new Date().getTime() - lastActive.getTime();
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " minutes";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + " hours";
        }

        long days = hours / 24;
        if (days < 30) {
            return days + " days";
        }

        long months = days / 30;
        if (months < 12) {
            return months + " months";
        }

        long years = months / 12;
        if (years <= 1) {
            return "1 years";
        }
        return "a long time";
    }

    public String determineFileCategory(Uri uri) {
        String mimeType = getContentResolver().getType(uri);

        if (mimeType != null) {
            if (mimeType.startsWith("video/")) {
                return CONSTS.VIDEO;
            } else if (mimeType.startsWith("image/")) {
                return CONSTS.IMAGE;
            } else if (mimeType.startsWith("audio/")) {
                return CONSTS.AUDIO;
            }
        }
        return CONSTS.FILE;
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
}