package com.khnkoyan.userimagesapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.models.Image;
import com.khnkoyan.userimagesapplication.viewHolders.ImageListHolder;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListHolder> {
    private Context context;
    private List<Image> imageList;
    private boolean isSelectedAll;
    private boolean isClick = true;

    public ImageListAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public ImageListHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item_with_checkbox, viewGroup, false);
        return new ImageListHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageListHolder holder, final int position) {
        byte[] blob = imageList.get(position).getBlob();
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        holder.getImgItemWithCheckbox().setImageBitmap(bitmap);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 3;
//        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
//        holder.getImgItemWithCheckbox().setImageBitmap(bitmap);
        if (!isClick) {
            if (!isSelectedAll) {
                holder.getChBoxItem().setChecked(false);
            } else {
                holder.getChBoxItem().setChecked(true);
            }

        }
        holder.getChBoxItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
                if (holder.getChBoxItem().isChecked()) {

                    imageList.get(position).setChecked(true);
                    notifyDataSetChanged();
                } else {
                    imageList.get(position).setChecked(false);
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        return imageList.size();
    }


    public void selectAll() {
        isSelectedAll = true;
        isClick = false;
        notifyDataSetChanged();
    }

    public void deSelectAll() {
        isSelectedAll = false;
        isClick = false;
        notifyDataSetChanged();
    }
}
