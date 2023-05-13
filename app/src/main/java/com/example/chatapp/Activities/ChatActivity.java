package com.example.chatapp.Activities;

import android.Manifest;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.emoji.EmojiCategory;

import java.io.IOException;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class ChatActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 123;
    private MediaRecorder recorder;
    private FrameLayout layoutAttach;
    private FrameLayout layoutAudio;
    private FrameLayout layoutEmoji;
    private FrameLayout layoutSend;
    ArrayList<Uri> listFile = new ArrayList<>();
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize views
        layoutAttach = findViewById(R.id.layoutAttach);
        layoutAudio = findViewById(R.id.layoutAudio);
        layoutEmoji = findViewById(R.id.layoutEmoji);
        layoutSend = findViewById(R.id.layoutSend);

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
    }

    private void startAudioRecording() {
        // This is a simple implementation. For a full-featured audio recording and playing,
        // you might need to use MediaPlayer and MediaRecorder classes with proper handling
        // of runtime permissions.

        // Assume you have a member variable like:
        // private MediaRecorder recorder;
        if (recorder == null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/audio.3gp");

            try {
                recorder.prepare();
                recorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    private void openEmojiPanel() {
        View rootView = getWindow().getDecorView().getRootView();
        try {
            final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(editTextMessage);
            emojiPopup.toggle(); // Toggles visibility of the Popup.
        } catch (IllegalStateException e) {
            // Handle the case where EmojiProvider is not installed.
            // You can show an error message or prompt the user to install the necessary dependencies.
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString();
        if (!message.isEmpty()) {
            // Send your message here. The implementation depends on your chat server.
            // Assuming you have a method like:
            // private void sendMessageToServer(String message);
//            sendMessageToServer(message);

            editTextMessage.setText("");  // Clear the input field
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSIONS)
    private void onPermissionsGranted() {
        openFilePicker();
    }

}