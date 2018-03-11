package com.khnkoyan.userimagesapplication.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.khnkoyan.userimagesapplication.fragments.ImageFragment;
import com.khnkoyan.userimagesapplication.models.Image;

import java.util.List;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {
    private List<Image> imageList;

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ImagePagerAdapter(FragmentManager fm, List<Image> imageList) {
        this(fm);
        this.imageList = imageList;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("myLog", "img.size: " + imageList.size());
        byte[] blob = imageList.get(position).getBlob();
        return ImageFragment.newInstance(blob);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}
