package com.khnkoyan.userimagesapplication;

import android.os.AsyncTask;

import com.khnkoyan.userimagesapplication.activities.ImageActivity;
import com.khnkoyan.userimagesapplication.activities.ImageListRemoveActivity;
import com.khnkoyan.userimagesapplication.activities.ProfileActivity;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.User;

public class GetUserDataAsyncTask extends AsyncTask<String, Void, User> {
    private ProfileActivity profileActivity;
    private ImageListRemoveActivity imgActivity;
    private UserImageDbManager imageDbManager;
    private ImageActivity imageActivity;

    public GetUserDataAsyncTask(ProfileActivity activity, UserImageDbManager imageDbManager) {
        this.profileActivity = activity;
        this.imageDbManager = imageDbManager;
    }

    public GetUserDataAsyncTask(ImageListRemoveActivity imgActivity, UserImageDbManager imageDbManager) {
        this.imgActivity = imgActivity;
        this.imageDbManager = imageDbManager;
    }

    public GetUserDataAsyncTask(ImageActivity imageActivity) {
        this.imageActivity = imageActivity;
        imageDbManager = UserImageDbManager.getInstance(imageActivity);
    }

    @Override
    protected User doInBackground(String... params) {
        String userEmail = params[0];
        User user = imageDbManager.getUserData(userEmail);
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (profileActivity != null) {
            profileActivity.setUserData(user);
        }
        if (imgActivity != null) {
            imgActivity.setUserData(user);
        }
        if (imageActivity != null) {
            imageActivity.setUserData(user);
        }
    }
}
