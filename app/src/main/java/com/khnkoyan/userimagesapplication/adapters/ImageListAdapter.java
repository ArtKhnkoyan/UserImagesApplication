package com.khnkoyan.userimagesapplication.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.activities.ProfileActivity;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.Image;
import com.khnkoyan.userimagesapplication.viewHolders.ImageListViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListViewHolder> {
    private Activity activity;
    private List<Image> imageList;
    private UserImageDbManager imageDbManager;
    private String userLogin;

    public ImageListAdapter(Activity activity, List<Image> imageList, UserImageDbManager imageDbManager, String userLogin) {
        this.activity = activity;
        this.imageList = imageList;
        this.imageDbManager = imageDbManager;
        this.userLogin = userLogin;
    }

    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.image_item, viewGroup, false);
        return new ImageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageListViewHolder holder, final int position) {
        final byte[] blob = imageList.get(position).getBlob();
//
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;
        final Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        holder.getImgView().setImageBitmap(bitmap);

        holder.getImgView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", blob);
                activity.setResult(Activity.RESULT_OK, returnIntent);
                activity.finish();
            }
        });

        holder.getImgItemPoint().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(activity, holder.getImgItemPoint());
                //inflating menu from xml resource
                popup.inflate(R.menu.context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemDelete:
                                //handle menu1 click
                                int img_id = imageList.get(position).getId();
                                imageDbManager.deleteItemImage(img_id);
                                notifyDataSetChanged();
                                Intent intent = new Intent(activity, ProfileActivity.class);
                                intent.putExtra("email", userLogin);
                                activity.startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        Log.i("log_tag", "imageList.size(): " + imageList.size());
        return imageList.size();
    }
}
