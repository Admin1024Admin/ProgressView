package com.l024.studydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.l024.studydemo.views.ProgressView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressView progressView =  this.findViewById(R.id.pv_view);

        progressView.setProgress(0.8f);
    }
}
