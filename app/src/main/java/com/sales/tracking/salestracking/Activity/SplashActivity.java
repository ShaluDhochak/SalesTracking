package com.sales.tracking.salestracking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.SessionManagement;

public class SplashActivity extends AppCompatActivity {

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onResume() {
        super.onResume();
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        if (session.isLoggedIn()){
            launchLoginScreen();
        }
    }

    private void launchLoginScreen(){
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, NavigationDrawerActivity.class));
                finish();
            }
        }, secondsDelayed * 3000);
    }
}
