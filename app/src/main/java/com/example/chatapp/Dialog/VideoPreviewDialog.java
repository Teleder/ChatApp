package com.example.chatapp.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.example.chatapp.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.MediaItem;

public class VideoPreviewDialog extends DialogFragment {
    private String videoUrl;
    private SimpleExoPlayer player;

    public VideoPreviewDialog(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_video_preview, null);
        PlayerView playerView = view.findViewById(R.id.video_preview);

        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        player.release();
    }
}

