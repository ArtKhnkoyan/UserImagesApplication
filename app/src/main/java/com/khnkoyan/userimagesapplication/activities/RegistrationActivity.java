package com.khnkoyan.userimagesapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.khnkoyan.userimagesapplication.SHAUtil;
import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.SaveUserDataAsyncTask;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;
import com.khnkoyan.userimagesapplication.models.Gender;
import com.khnkoyan.userimagesapplication.models.User;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName;
    private EditText edtSurName;
    private RadioGroup rdGroup;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private EditText edtAge;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtCheckPassword;
    private Button btnSave;

    private String userName;
    private String userSurName;
    private String selectGenderType;
    private int userAge;
    private String userEmail;
    private String userPassword;
    private String passwordSHA_1;
    private String userCheckPassword;
    private UserImageDbManager imageDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindIds();

        imageDbManager = new UserImageDbManager(this);
    }

    private void bindIds() {
        edtName = (EditText) findViewById(R.id.edtfirstName);
        edtSurName = (EditText) findViewById(R.id.edtlastName);
        rdGroup = (RadioGroup) findViewById(R.id.rdGroup);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        edtAge = (EditText) findViewById(R.id.edtAge);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtCheckPassword = (EditText) findViewById(R.id.edtCheckPassword);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    private boolean attemptLogin() {
        boolean cancel = false;
        userName = edtName.getText().toString();
        userSurName = edtSurName.getText().toString();
        userEmail = edtEmail.getText().toString();
        userPassword = edtPassword.getText().toString();
        userCheckPassword = edtCheckPassword.getText().toString();

        if (!edtAge.getText().toString().isEmpty()) {
            userAge = Integer.parseInt(edtAge.getText().toString());
        } else {
            userAge = 0;
        }
        if (rbMale.isChecked()) {
            selectGenderType = rbMale.getText().toString();
        } else if (rbFemale.isChecked()) {
            selectGenderType = rbFemale.getText().toString();
        }
        if (TextUtils.isEmpty(userName)) {
            edtName.setError(getString(R.string.error_field_required));
            cancel = true;
            return false;
        }
        if (TextUtils.isEmpty(userSurName)) {
            edtSurName.setError(getString(R.string.error_field_required));
            cancel = true;
            return false;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(userEmail)) {
            edtEmail.setError(getString(R.string.error_field_required));
            cancel = true;
            return false;
        }
        if (!isEmailValid(userEmail)) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            cancel = true;
            return false;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(userPassword) || (!isPasswordValid(userPassword))) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
            return false;
        }
        if (TextUtils.isEmpty(userCheckPassword) || (!isPasswordValid(userCheckPassword))) {
            edtCheckPassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
            return false;
        }
        if (!userPassword.contentEquals(userCheckPassword)) {
            edtCheckPassword.setError("this password not match");
            cancel = true;
            return false;
        }
        if (TextUtils.isEmpty(String.valueOf(userAge)) || (!isUserAgeValid(userAge))) {
            edtAge.setError(getString(R.string.error_field_required));
            cancel = true;
            return false;
        }
        if (TextUtils.isEmpty(selectGenderType)) {
            if (!rdGroup.isClickable()) {
                Toast.makeText(getApplicationContext(), "Please choose your gender", Toast.LENGTH_SHORT).show();
            }
            cancel = true;
            return false;
        }

        if (!cancel) {
            boolean emailExists = imageDbManager.checkUser(userEmail);
            if (!emailExists) {
                saveUserData();
                Intent intent = new Intent(RegistrationActivity.this, ProfileActivity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "this email exists", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isUserAgeValid(int age) {
        //TODO: Replace this with your own logic
        return age >= 1 && age < 100;
    }

    private void saveUserData() {
        User user = new User();
        passwordSHA_1 = SHAUtil.makeSHA_1(userPassword);
        user.setName(userName);
        user.setSurName(userSurName);
        user.setAge(userAge);
        user.setGender(Gender.valueOf(selectGenderType));
        user.setEmail(userEmail);
        user.setPassword(passwordSHA_1);
        user.setUserLoggedIn(true);
        SaveUserDataAsyncTask imageAsyncTask = new SaveUserDataAsyncTask(imageDbManager);
        imageAsyncTask.execute(user);

        //imageDbManager.saveUserData(user);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageDbManager.closeDb();
    }

    @Override
    public void onClick(View v) {
        attemptLogin();
    }
}
