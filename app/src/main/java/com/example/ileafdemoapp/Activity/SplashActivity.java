package com.example.ileafdemoapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.ileafdemoapp.MainActivity;
import com.example.ileafdemoapp.R;
import com.example.ileafdemoapp.Utils.SharedPref;

public class SplashActivity extends AppCompatActivity {

    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTimeDelay();
    }

    private void setTimeDelay() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SharedPref.getLoggedIn_Status(SplashActivity.this)) {

                    autoLogin();
                    return;
                }
                goToLoginScreen();


            }
        }, 3000);


    }

    private void autoLogin() {

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);

    }

    private void goToLoginScreen() {

        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
