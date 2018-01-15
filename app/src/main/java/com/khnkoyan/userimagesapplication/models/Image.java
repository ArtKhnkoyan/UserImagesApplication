package com.khnkoyan.userimagesapplication.models;

/**
 * Created by User on 17.12.2017.
 */

public class Image {
    private int id;
    private byte[] blob;
    private boolean checked;

    public Image() {
    }

    public Image(boolean checked) {
        this.checked = checked;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
