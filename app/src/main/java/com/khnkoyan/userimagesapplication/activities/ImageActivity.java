package com.khnkoyan.userimagesapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.khnkoyan.userimagesapplication.GetUserImageListTask;
import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.adapters.ImagePagerAdapter;
import com.khnkoyan.userimagesapplication.models.Image;

import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private ViewPager imgPager;
    private String userEmail;
    private boolean isThereImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imgPager = (ViewPager) findViewById(R.id.imgPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(imgPager, true);

        if (getIntent().hasExtra("login")) {
            userEmail = getIntent().getStringExtra("login");
        }
        GetUserImageListTask asyncTask = new GetUserImageListTask(this);
        asyncTask.execute(userEmail);
        Log.i("myLog", "ImageActivity.onCreate()");
    }

    public void setUserData(List<Image> imageList) {
        if (imageList != null && imageList.size() > 0) {
            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), imageList, userEmail);
            imgPager.setAdapter(imagePagerAdapter);
            isThereImage = true;
        } else {
            isThereImage = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_image_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.imageItemDelete:
                if (isThereImage) {
                    Intent intent = new Intent(ImageActivity.this, ImageListRemoveActivity.class);
                    intent.putExtra("login", userEmail);
                    startActivity(intent);
                }
                return true;
            default:
                return false;
        }
    }
}
