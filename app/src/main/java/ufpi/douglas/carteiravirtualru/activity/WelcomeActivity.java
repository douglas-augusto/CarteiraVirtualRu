package ufpi.douglas.carteiravirtualru.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ufpi.douglas.carteiravirtualru.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);
    }
}
