package com.khnkoyan.userimagesapplication.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.khnkoyan.userimagesapplication.R;

public class ImageListWithCheckboxHolder extends RecyclerView.ViewHolder {
    private ImageView imgItemWithCheckbox;
    private CheckBox chBoxItem;

    public ImageListWithCheckboxHolder(View itemView) {
        super(itemView);

        this.imgItemWithCheckbox = (ImageView) itemView.findViewById(R.id.imgItemWithCheckbox);
        this.chBoxItem = (CheckBox) itemView.findViewById(R.id.chBoxItem);
    }

    public ImageView getImgItemWithCheckbox() {
        return imgItemWithCheckbox;
    }

    public CheckBox getChBoxItem() {
        return chBoxItem;
    }

}
