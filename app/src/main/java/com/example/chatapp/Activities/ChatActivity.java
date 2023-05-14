package com.example.chatapp.Activities;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapter.ChatAdapter;
import com.example.chatapp.Dtos.PagedResultDto;
import com.example.chatapp.Dtos.PayloadMessage;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.R;
import com.example.chatapp.Retrofit.APIService;
import com.example.chatapp.Retrofit.RetrofitClient;
import com.example.chatapp.Retrofit.SharedPrefManager;
import com.example.chatapp.Retrofit.TokenManager;
import com.example.chatapp.Utils.CONSTS;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import droidninja.filepicker.FilePickerBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Retrofit;
import retrofit2.http.Body;


public class ChatActivity extends AppCompatActivity {
    private ProgressBar processBar;
    private RetrofitClient retrofitClient;
    RecyclerView rcMessages;
    private TokenManager tokenManager;
    private Retrofit retrofit;
    private VideoView videoPreview;
    private ImageView imagePreview;
    SharedPrefManager sharedPrefManager;
    APIService apiService;
    ChatAdapter chatAdapter;
    ArrayList<Message> messages = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSIONS = 123;
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
        EmojiManager.install(new GoogleEmojiProvider());
        loadMoreMessage();

//        rcMessages.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(rcMessages, this, messages, new ChatAdapter.MessageClickListener() {
            @Override
            public void onImageClicked(String imageUrl) {
                showImage(imageUrl);
            }

            @Override
            public void onAudioClicked(String audioUrl, TextView audioDuration) {
                playAudio(audioUrl, audioDuration);
            }

            @Override
            public void onVideoClicked(String videoUrl) {
                playVideo(videoUrl);
            }

            @Override
            public void onFileClicked(String videoUrl, String fileName) {
                downloadFile(videoUrl, fileName);
            }

        });
        chatAdapter.setLoadMoreListener(new ChatAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        chatAdapter.notifyDataSetChanged();
                    }
                });


            }
        });
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
//        rcMessages.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rcMessages.setLayoutManager(layoutManager);
        rcMessages.setAdapter(chatAdapter);
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

            }

            @Override
            public void afterTextChanged(Editable s) {
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
                        uploadFile(filePath, sharedPrefManager.getCurrentConservation() != null ? sharedPrefManager.getCurrentConservation().getCode() : "12021");


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
            // Send your message here. The implementation depends on your chat server.
            // Assuming you have a method like:
            // private void sendMessageToServer(String message);
//            sendMessageToServer(message);
            if (sharedPrefManager.getCurrentConservation().getGroupId() == null || sharedPrefManager.getCurrentConservation().getGroupId().trim().equals("")) {
                sendPrivateMessage(sharedPrefManager.getCurrentConservation().getUserId_1() == sharedPrefManager.getUser().getId() ? sharedPrefManager.getCurrentConservation().getUserId_2() : sharedPrefManager.getCurrentConservation().getUserId_1(),
                        new PayloadMessage(inputMessage.getText().toString(), sharedPrefManager.getCurrentConservation().getCode(), CONSTS.MESSAGE_PRIVATE, null, null, null)

                );
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

    private void loadMoreMessage() {
        if ((nextPage - 1) * limitMessage + limitMessage >= totalMessage)
            return;
        processBar.setVisibility(View.VISIBLE);
        apiService = retrofitClient.getRetrofit().create(APIService.class);
        apiService.findMessagesWithPaginationAndSearch(
                sharedPrefManager.getCurrentConservation().getCode(), nextPage, limitMessage, "").enqueue(new retrofit2.Callback<PagedResultDto<Message>>() {
            @Override
            public void onResponse(retrofit2.Call<PagedResultDto<Message>> call, retrofit2.Response<PagedResultDto<Message>> response) {
                if (response.isSuccessful()) {
                    try {
                        totalMessage = response.body().getPagination().getTotal();
                        nextPage += 1;
                        messages.addAll(response.body().getData());
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

    public void uploadFile(String filePath, String code) {
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
                                    new PayloadMessage(currentFile.getUrl(), sharedPrefManager.getCurrentConservation().getCode(), CONSTS.AUDIO, null, null, currentFile)
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

    public void playVideo(String videoUrl) {
        videoPreview.setVideoURI(Uri.parse(videoUrl));
        videoPreview.start();
    }

    public void showImage(String videoUrl) {
        Glide.with(ChatActivity.this).load(videoUrl).into(imagePreview);
    }

    public void playAudio(String audioUrl, TextView audioDuration) {
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
}