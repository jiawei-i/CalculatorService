package com.example.apple.calculatorservice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.open_calculator_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemUtil.getAppOps(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, CalculatorService.class);
                    startService(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 10);
                }


            }
        });
    }
}
