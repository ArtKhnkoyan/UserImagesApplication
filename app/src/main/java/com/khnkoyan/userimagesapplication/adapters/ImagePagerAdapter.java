package com.khnkoyan.userimagesapplication.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.khnkoyan.userimagesapplication.fragments.ImageFragment;
import com.khnkoyan.userimagesapplication.models.Image;

import java.util.List;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {
    private List<Image> imageList;
    private String userEmail;

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ImagePagerAdapter(FragmentManager fm, List<Image> imageList, String userEmail) {
        this(fm);
        this.imageList = imageList;
        this.userEmail = userEmail;
    }

    @Override
    public Fragment getItem(int position) {
        byte[] blob = imageList.get(position).getBlob();
        return ImageFragment.newInstance(blob, userEmail);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}
