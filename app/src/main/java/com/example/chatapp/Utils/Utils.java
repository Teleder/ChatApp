package com.example.chatapp.Utils;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.example.chatapp.Model.File.FileCategory;
import com.example.chatapp.Model.User.Contact;


public class Utils {
    public static void downloadFile(Context context, String fileUrl, String fileName) {
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(fileUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(fileName);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
        downloadmanager.enqueue(request);
    }
}
