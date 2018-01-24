package com.khnkoyan.userimagesapplication;

import android.os.AsyncTask;

import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.User;

public class SaveUserDataAsyncTask extends AsyncTask<User, Void, Void>{
    private UserImageDbManager imageDbManager;

    public SaveUserDataAsyncTask(UserImageDbManager imageDbManager) {
        this.imageDbManager = imageDbManager;
    }

    @Override
    protected Void doInBackground(User... params) {
        User user= params[0];
        imageDbManager.saveUserData(user);
        return null;
    }
}
