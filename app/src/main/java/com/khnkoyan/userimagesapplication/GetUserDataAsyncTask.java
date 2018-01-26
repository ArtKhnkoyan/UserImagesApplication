package com.khnkoyan.userimagesapplication;

import android.os.AsyncTask;

import com.khnkoyan.userimagesapplication.activities.ImageListActivity;

import com.khnkoyan.userimagesapplication.activities.ProfileActivity;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.User;

public class GetUserDataAsyncTask extends AsyncTask<String, Void, User> {
    private ProfileActivity profileActivity;

    private ImageListActivity activityWithCheckbox;
    private UserImageDbManager imageDbManager;

    public GetUserDataAsyncTask(ProfileActivity activity, UserImageDbManager imageDbManager) {
        this.profileActivity = activity;
        this.imageDbManager = imageDbManager;
    }


    public GetUserDataAsyncTask(ImageListActivity activityWithCheckbox, UserImageDbManager imageDbManager) {
        this.activityWithCheckbox = activityWithCheckbox;
        this.imageDbManager = imageDbManager;
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
            profileActivity.getUserData(user);
        }
        if (activityWithCheckbox != null) {
            activityWithCheckbox.getUserData(user);
        }
    }
}
