package com.inferno.mobile.billprogressbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.inferno.mobile.billprogressbarlib.BillProgressBar;
import com.inferno.mobile.billprogressbarlib.ReverseBillProgressBar;

public class MainActivity extends AppCompatActivity {
    private ReverseBillProgressBar bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bill = findViewById(R.id.bill);
        // bill.startAnimation();
        // new Handler().postDelayed(() -> runOnUiThread(bill::stopAnimation), 3000);


    }

    public void animate(View view) {
        bill.startAnimation(ReverseBillProgressBar.SLOW);
        new Handler().postDelayed(() -> runOnUiThread(bill::stopAnimation),
                ReverseBillProgressBar.SLOW);

    }
}