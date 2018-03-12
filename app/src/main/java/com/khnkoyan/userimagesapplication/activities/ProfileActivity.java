package com.khnkoyan.userimagesapplication.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.khnkoyan.userimagesapplication.GetUserDataAsyncTask;
import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int CAPTURE_PHOTO = 2;

    private static final int STORAGE_PERMISSION_CODE = 0;


    private ImageView imgHeaderCover;
    private CircleImageView imgProfile;
    private ImageView userUpdateEdit;
    private ImageButton imgSelectGalPhoto;
    private ImageButton imgListActivity;
    private ImageButton imgDoingPhoto;

    private TextView usNameAndSurname;
    private TextView usEmail;
    private TextView usPassword;
    private TextView usAge;
    private TextView usGender;

    private UserImageDbManager imageDbManager;
    private Bitmap selectBitmap;
    private String setEmail;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindIds();

        imgHeaderCover.setOnClickListener(this);
        userUpdateEdit.setOnClickListener(this);
        imgSelectGalPhoto.setOnClickListener(this);
        imgListActivity.setOnClickListener(this);
        imgDoingPhoto.setOnClickListener(this);
        imageDbManager = UserImageDbManager.getInstance(this);
        setEmail = checkAndGetEmail();

        isReadStorageAllowed();
        if (getIntent().hasExtra("blob")) {
            byte[] blob = getIntent().getByteArrayExtra("blob");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
            imgProfile.setImageBitmap(bitmap);
        }
        GetUserDataAsyncTask asyncTask = new GetUserDataAsyncTask(this, imageDbManager);
        asyncTask.execute(setEmail);

    }

    private void bindIds() {
        imgHeaderCover = (ImageView) findViewById(R.id.imgHeaderCover);
        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        userUpdateEdit = (ImageView) findViewById(R.id.userUpdateEdit);
        usNameAndSurname = (TextView) findViewById(R.id.userNameAndSurname);
        usGender = (TextView) findViewById(R.id.usGender);
        usAge = (TextView) findViewById(R.id.usAge);
        imgSelectGalPhoto = (ImageButton) findViewById(R.id.imgSelectGalPhoto);
        imgListActivity = (ImageButton) findViewById(R.id.imgListActivity);
        imgDoingPhoto = (ImageButton) findViewById(R.id.imgDoingPhoto);
        usEmail = (TextView) findViewById(R.id.usEmail);
        usPassword = (TextView) findViewById(R.id.usPassword);

    }

    private boolean isReadStorageAllowed() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            imgProfile.setEnabled(false);
            requestMultiplePermissions();
            return false;
        } else {
            imgProfile.setEnabled(true);
            return true;
        }
    }

    private void requestMultiplePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, android.Manifest.permission.CAMERA)
                && ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "you need camera", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                imgProfile.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menuUserDelete:
                imageDbManager.deleteUserData(setEmail);
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.menuSignOut:
                showDialog();
                return true;
            default:
                return false;
        }
    }

    private void logOutUser() {
        imageDbManager.setLogin(false, setEmail);
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logOutUser();
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
    }


    public void setUserData(User user) {
        this.user = user;
        usNameAndSurname.setText(String.valueOf(user.getName()) + " " + String.valueOf(user.getSurName()));
        usEmail.setText(String.valueOf(user.getEmail()));
        usPassword.setText(String.valueOf(user.getPassword()));
        usAge.setText(String.valueOf(user.getAge()));
        usGender.setText(String.valueOf(user.getGender()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri image = data.getData();
                if (image != null) {
                    try {
                        InputStream imageInputStream = getContentResolver().openInputStream(image);
                        selectBitmap = BitmapFactory.decodeStream(imageInputStream);
                        byte[] blob = getImageBytes(selectBitmap);
                        //    Saving to Database...
                        imageDbManager.saveImageDb(blob, setEmail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (requestCode == CAPTURE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                byte[] blob = getImageBytes(bitmap);
                //    Saving to Database...
                imageDbManager.saveImageDb(blob, setEmail);
            }
        }
        Log.i("myLog", "ProfileActivity.onActivityResult()");
    }

    private byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String checkAndGetEmail() {
        if (getIntent().hasExtra("email")) {
            return getIntent().getStringExtra("email");
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.userUpdateEdit:
                intent = new Intent(this, UpdateUserDataActivity.class);
                intent.putExtra("login", setEmail);
                startActivity(intent);
                break;
            case R.id.imgListActivity:
                intent = new Intent(this, ImageActivity.class);
                intent.putExtra("login", setEmail);
                startActivity(intent);
                break;
            case R.id.imgSelectGalPhoto:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
                break;

            case R.id.imgDoingPhoto:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_PHOTO);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageDbManager.closeDb();
    }
}
