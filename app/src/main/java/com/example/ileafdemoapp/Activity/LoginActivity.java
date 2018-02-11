package com.example.ileafdemoapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ileafdemoapp.MainActivity;
import com.example.ileafdemoapp.R;
import com.example.ileafdemoapp.Utils.AppConst;
import com.example.ileafdemoapp.Utils.SharedPref;
import com.example.ileafdemoapp.app.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.emailWrapper)
    TextInputLayout emailWrapper;
    @BindView(R.id.edttxt_email)
    EditText edttxt_email;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.edttxt_password)
    EditText edttxt_password;
    @BindView(R.id.checkbox_remember_me)
    CheckBox checkbox_remember_me;
    @BindView(R.id.animation_view)
    LottieAnimationView animation_view;
    @BindView(R.id.rel_loader)
    RelativeLayout rel_loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setClickListeners();
        checkRememberStatus();

    }

    private void checkRememberStatus() {

        if (SharedPref.getRememberMeLoggedIn_Status(MyApplication.getInstance())) {
            edttxt_email.setText(SharedPref.getRememberMeUserName(MyApplication.getInstance()));
            edttxt_password.setText(SharedPref.getRememberMePassword(MyApplication.getInstance()));
            checkbox_remember_me.setChecked(true);
        }
    }

    private void setClickListeners() {
        buttonLogin.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {

        exitApp();

    }

    private void exitApp() {

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonLogin:


                String strUsername = edttxt_email.getText().toString().trim();
                String strPassword = edttxt_password.getText().toString().trim();

                if (strUsername.length() == 0) {

                    emailWrapper.setError("Please enter the username");
                } else if (strPassword.length() == 0) {

                    emailWrapper.setError(null);
                    passwordWrapper.setError("Please enter the password");
                } else {

                    emailWrapper.setError(null);
                    passwordWrapper.setError(null);

                    if (strUsername.equals(AppConst.DEFAULT_USER_NAME) && strPassword.equals(AppConst.DEFAULT_PASSWORD)) {

                        SharedPref.setLoggedIn_Status(MyApplication.getInstance(), true);
                        SharedPref.setRememberMeUserName(MyApplication.getInstance(), strUsername);


                        if (checkbox_remember_me.isChecked()) {

                            SharedPref.setRememberMeLoggedIn_Status(MyApplication.getInstance(), true);
                            SharedPref.setRememberMeUserName(MyApplication.getInstance(), strUsername);
                            SharedPref.setRememberMePassword(MyApplication.getInstance(), strPassword);


                        }

                        rel_loader.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                rel_loader.setVisibility(View.GONE);
                                gotoHomeActivity();


                            }
                        }, 2000);


                        return;
                    }

                    Toast.makeText(getApplicationContext(), "Wrong credentials entered", Toast.LENGTH_SHORT).show();
                }

                break;


        }
    }


    private void gotoHomeActivity() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


}
