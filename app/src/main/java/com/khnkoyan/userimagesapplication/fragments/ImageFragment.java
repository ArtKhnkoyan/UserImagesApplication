package com.khnkoyan.userimagesapplication.fragments;


import android.content.Intent;
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
import com.khnkoyan.userimagesapplication.activities.ProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements View.OnClickListener {
    private static final String IMAGE_LIST = "blob";
    private static final String USER_EMAIL = "email";
    private byte[] blob;
    private ImageView imgFragment;
    private Bitmap bitmap;
    private String userEmail;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(byte[] blob, String userEmail) {
        Fragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putByteArray(IMAGE_LIST, blob);
        bundle.putString(USER_EMAIL, userEmail);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(IMAGE_LIST) && getArguments().containsKey(USER_EMAIL)) {
            blob = getArguments().getByteArray(IMAGE_LIST);
            userEmail = getArguments().getString(USER_EMAIL);
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
        imgFragment.setOnClickListener(this);

//        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
        imgFragment.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgFragment) {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra(IMAGE_LIST, blob);
            intent.putExtra(USER_EMAIL, userEmail);
            startActivity(intent);
        }
    }
}
