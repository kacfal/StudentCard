package com.example.onemoretime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.onemoretime.LoginRegister.LoginActivity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}