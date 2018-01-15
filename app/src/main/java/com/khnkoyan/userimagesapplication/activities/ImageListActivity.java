package com.khnkoyan.userimagesapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.adapters.ImageListAdapter;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.User;

public class ImageListActivity extends AppCompatActivity implements View.OnCreateContextMenuListener {

    private RecyclerView recImageList;
    private ImageListAdapter imageListAdapter;

    private UserImageDbManager imageDbManager;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        recImageList = (RecyclerView) findViewById(R.id.recImageList);
        recImageList.setLayoutManager(new GridLayoutManager(this, 2));
        this.imageDbManager = new UserImageDbManager(this);

        if (getIntent().hasExtra("login")) {
            userEmail = getIntent().getStringExtra("login");
        }
        User user = imageDbManager.getUserDataWithData(userEmail);

        imageListAdapter = new ImageListAdapter(this, user.getImageList(), imageDbManager, userEmail);
        recImageList.setAdapter(imageListAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageDbManager.closeDb();
    }
}
