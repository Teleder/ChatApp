package com.example.chatapp.Utils;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class AudioRecorder {

    private MediaRecorder recorder;
    private String path;

    public AudioRecorder(String path) {
        this.path = path;
    }

    public void startRecording() throws IOException {
        if (recorder != null) {
            recorder.release();
        }

        File outFile = new File(path);
        if (outFile.exists()) {
            outFile.delete();
        }

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }
}
