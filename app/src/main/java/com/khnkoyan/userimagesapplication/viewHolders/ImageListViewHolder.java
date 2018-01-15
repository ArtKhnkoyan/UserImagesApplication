package com.khnkoyan.userimagesapplication.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.khnkoyan.userimagesapplication.R;

public class ImageListViewHolder extends RecyclerView.ViewHolder {
    private ImageView imgView;
    private ImageView imgItemPoint;

    public ImageListViewHolder(View itemView) {
        super(itemView);
        this.imgView = (ImageView) itemView.findViewById(R.id.imgItemView);
        this.imgItemPoint = (ImageView) itemView.findViewById(R.id.imgItemPoint);
    }

    public ImageView getImgView() {
        return imgView;
    }

    public ImageView getImgItemPoint() {
        return imgItemPoint;
    }
}
