package com.khnkoyan.userimagesapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;

public class StartActivity extends AppCompatActivity {
    private UserImageDbManager imageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imageManager = UserImageDbManager.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imageManager.isLoggedIn()) {
                    String selectLoginedUser = imageManager.selectLoginedUser();
                    Intent intent = new Intent(StartActivity.this, ProfileActivity.class);
                    intent.putExtra("email", selectLoginedUser);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                }
            }
        }, 3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageManager.closeDb();
    }
}