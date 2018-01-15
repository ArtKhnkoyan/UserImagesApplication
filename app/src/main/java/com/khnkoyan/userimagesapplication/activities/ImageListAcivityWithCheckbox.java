package com.khnkoyan.userimagesapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.adapters.ImageListAdapterWithCheckbox;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.Image;
import com.khnkoyan.userimagesapplication.models.User;

import java.util.ArrayList;
import java.util.List;

public class ImageListAcivityWithCheckbox extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recImageListWithCheckbox;
    private CheckBox chBoxAllImage;
    private UserImageDbManager imageDbManager;
    private ImageListAdapterWithCheckbox adapterWithCheckbox;
    private String userEmail;

    private List<Integer> whereArgsList;
    private List<Image> imageList;
    private boolean isSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list_with_checkbox);

        this.chBoxAllImage = (CheckBox) findViewById(R.id.chBoxAllImage);
        recImageListWithCheckbox = (RecyclerView) findViewById(R.id.recImageListWithCheckbox);
        recImageListWithCheckbox.setLayoutManager(new LinearLayoutManager(this));
        chBoxAllImage.setOnClickListener(this);

        this.imageDbManager = new UserImageDbManager(this);
        whereArgsList = new ArrayList<>();
        if (getIntent().hasExtra("login")) {
            userEmail = getIntent().getStringExtra("login");
        }
        User user = imageDbManager.getUserDataWithData(userEmail);
        this.imageList = user.getImageList();

        adapterWithCheckbox = new ImageListAdapterWithCheckbox(this, imageList);
        recImageListWithCheckbox.setAdapter(adapterWithCheckbox);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.itemDelete:
                removeImageFromDb();
                return true;
            case R.id.itemDeleteAll:
                removeAllImageFromDb();
                return true;
            default:
                return false;
        }
    }

    private void removeImageFromDb() {
        whereArgsList.clear();
        if (imageList != null && imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                if (imageList.get(i).isChecked()) {
                    whereArgsList.add(imageList.get(i).getId());
                }
            }
        }
        if (whereArgsList.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(true)
                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            for (int i = 0; i < whereArgsList.size(); i++) {
                                imageDbManager.deleteItemImage(whereArgsList.get(i));
                            }
                            startApp();
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
            dialog.show();

        } else {
            Toast.makeText(getApplicationContext(), "didn't choose", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeAllImageFromDb() {
        if (isSelected) {
            if (imageList != null && imageList.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                imageDbManager.deleteUserAllImage(userEmail);
                                Intent intent = new Intent(ImageListAcivityWithCheckbox.this, ProfileActivity.class);
                                intent.putExtra("email", userEmail);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
                dialog.show();
            } else {
                Toast.makeText(getApplicationContext(), "didn't choose", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "didn't choose", Toast.LENGTH_SHORT).show();
        }
    }

    private void startApp() {
        Intent intent = new Intent(ImageListAcivityWithCheckbox.this, ImageListAcivityWithCheckbox.class);
        intent.putExtra("login", userEmail);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        isSelected = true;
        if (v.getId() == R.id.chBoxAllImage) {
            if (chBoxAllImage.isChecked()) {
                adapterWithCheckbox.selectAll();
            } else {
                adapterWithCheckbox.deSelectAll();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageDbManager.closeDb();
    }
}
