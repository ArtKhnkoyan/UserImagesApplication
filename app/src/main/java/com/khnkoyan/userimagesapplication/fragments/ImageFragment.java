package com.khnkoyan.userimagesapplication.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.khnkoyan.userimagesapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {
    private static final String IMAGE_LIST = "imageList";
    private byte[] blob;
    private ImageView imgFragment;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(byte[] blob) {
        Fragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putByteArray(IMAGE_LIST, blob);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(IMAGE_LIST)) {
            blob = getArguments().getByteArray(IMAGE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgFragment = (ImageView) view.findViewById(R.id.imgFragment);
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 3;
//        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
        imgFragment.setImageBitmap(bitmap);
    }
}
