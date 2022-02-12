package com.inferno.mobile.billprogressbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.inferno.mobile.billprogressbarlib.BillProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BillProgressBar bill = findViewById(R.id.bill);
        bill.startAnimation();
        new Handler().postDelayed(() -> runOnUiThread(bill::stopAnimation), 3000);


    }
}