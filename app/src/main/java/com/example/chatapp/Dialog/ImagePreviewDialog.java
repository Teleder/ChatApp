package com.example.chatapp.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;

public class ImagePreviewDialog extends DialogFragment {
    private String imageUrl;

    public ImagePreviewDialog(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_image_preview, null);
        ImageView imageView = view.findViewById(R.id.imagePreview);

        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        builder.setView(view);

        return builder.create();
    }
}
