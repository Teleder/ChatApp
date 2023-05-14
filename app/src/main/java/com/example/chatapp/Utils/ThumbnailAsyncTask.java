package com.example.chatapp.Utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;

class ThumbnailAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    public ThumbnailAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String videoPath = strings[0];
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        return retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }
}
