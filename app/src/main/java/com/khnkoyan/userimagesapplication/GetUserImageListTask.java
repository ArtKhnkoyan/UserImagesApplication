package com.khnkoyan.userimagesapplication;


import android.os.AsyncTask;

import com.khnkoyan.userimagesapplication.activities.ImageActivity;
import com.khnkoyan.userimagesapplication.activities.ImageListRemoveActivity;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.Image;

import java.util.List;

public class GetUserImageListTask extends AsyncTask<String, Void, List<Image>> {
    private ImageListRemoveActivity imgActivity;
    private ImageActivity imageActivity;
    private UserImageDbManager imageDbManager;

    public GetUserImageListTask(ImageListRemoveActivity imgActivity, UserImageDbManager imageDbManager) {
        this.imgActivity = imgActivity;
        this.imageDbManager = imageDbManager;
    }

    public GetUserImageListTask(ImageActivity imageActivity) {
        this.imageActivity = imageActivity;
        imageDbManager = UserImageDbManager.getInstance(imageActivity);
    }

    @Override
    protected List<Image> doInBackground(String... params) {
        String userEmail = params[0];
        return imageDbManager.selectBlobOrIdByUserId(userEmail);
    }

    @Override
    protected void onPostExecute(List<Image> imageList) {
        super.onPostExecute(imageList);
        if (imgActivity != null) {
            imgActivity.setUserData(imageList);
        }
        if (imageActivity != null) {
            imageActivity.setUserData(imageList);
        }
    }
}
