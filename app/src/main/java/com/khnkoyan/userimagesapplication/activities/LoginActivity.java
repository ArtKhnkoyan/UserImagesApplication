package com.khnkoyan.userimagesapplication.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khnkoyan.userimagesapplication.SHAUtil;
import com.khnkoyan.userimagesapplication.R;
import com.khnkoyan.userimagesapplication.dbManagers.UserImageDbManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button btnSignIn;
    private Button btnRegistration;
    private UserImageDbManager imageDbManager;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        bindIds();
        imageDbManager = UserImageDbManager.getInstance(this);
    }

    private void bindIds() {
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.btnLogin);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        btnSignIn.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
    }


    private boolean attemptLogin() {
//        mEmailView.setError(null);
//        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            return false;
        }
        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            return false;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || (!isPasswordValid(password))) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            return false;
        }

        String passwordSHA_1 = SHAUtil.makeSHA_1(password);

        boolean emailAndPassExists = imageDbManager.checkUser(email, passwordSHA_1);
        if (emailAndPassExists) {
            showProgress(true);
            imageDbManager.setLogin(true, email);
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "this email and password not exists", Toast.LENGTH_SHORT).show();
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                attemptLogin();
                break;
            case R.id.btnRegistration:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageDbManager.closeDb();
    }
}
