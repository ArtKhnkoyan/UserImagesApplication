package com.khnkoyan.userimagesapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.Gender;
import com.khnkoyan.userimagesapplication.models.User;

public class UpdateUserDataActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUpdFirstName;
    private EditText edtUpdLastName;
    private RadioGroup rdUpdGroup;
    private RadioButton rbUpdMale;
    private RadioButton rbUpdFemale;
    private EditText edtUpdAge;
    private Button btnUpdSave;
    private String userEmail;
    private UserImageDbManager imageDbManager;
    private User user;
    private String userName;
    private String userSurName;
    private int userAge;
    private String selectGenderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_data);

        bindIds();

        imageDbManager = UserImageDbManager.getInstance(this);
        if (getIntent().hasExtra("login")) {
            userEmail = getIntent().getStringExtra("login");
        }
    }

    private void bindIds() {
        edtUpdFirstName = (EditText) findViewById(R.id.edtUpdFirstName);
        edtUpdLastName = (EditText) findViewById(R.id.edtUpdLastName);
        rdUpdGroup = (RadioGroup) findViewById(R.id.rdUpdGroup);
        rbUpdMale = (RadioButton) findViewById(R.id.rbUpdMale);
        rbUpdFemale = (RadioButton) findViewById(R.id.rbUpdFemale);
        edtUpdAge = (EditText) findViewById(R.id.edtUpdAge);
        btnUpdSave = (Button) findViewById(R.id.btnUpdSave);
        btnUpdSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        attemptLogin();
    }

    private boolean attemptLogin() {
        userName = edtUpdFirstName.getText().toString();
        userSurName = edtUpdLastName.getText().toString();

        if (!edtUpdAge.getText().toString().isEmpty()) {
            userAge = Integer.parseInt(edtUpdAge.getText().toString());
        } else {
            userAge = 0;
        }
        if (rbUpdMale.isChecked()) {
            selectGenderType = rbUpdMale.getText().toString();
        } else if (rbUpdFemale.isChecked()) {
            selectGenderType = rbUpdFemale.getText().toString();
        }
        if (TextUtils.isEmpty(userName)) {
            edtUpdFirstName.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(userSurName)) {
            edtUpdLastName.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(String.valueOf(userAge)) || (!isUserAgeValid(userAge))) {
            edtUpdAge.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(selectGenderType)) {
            if (!rdUpdGroup.isClickable()) {
                Toast.makeText(getApplicationContext(), "Please choose your gender", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        updateUserData();
        Intent intent = new Intent(UpdateUserDataActivity.this, ProfileActivity.class);
        intent.putExtra("email", userEmail);
        startActivity(intent);
        return true;
    }

    private boolean isUserAgeValid(int age) {
        //TODO: Replace this with your own logic
        return age >= 1 && age < 100;
    }

    private void updateUserData() {
        user = new User();
        user.setName(userName);
        user.setSurName(userSurName);
        user.setAge(userAge);
        user.setGender(Gender.valueOf(selectGenderType));
        imageDbManager.updateUserData(user, userEmail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageDbManager.closeDb();
    }
}
