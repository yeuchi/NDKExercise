package com.ctyeung.ndkex1;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ctyeung.ndkex1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this.getApplicationContext();
       // ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        initButtons();
    }

    private void initButtons()
    {
        Button btnConvolution = findViewById(R.id.btnConvolution);
        btnConvolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ConvolutionActivity.class);
                startActivity(intent);
            }
        });

        Button btnHoughCircle = findViewById(R.id.btnHoughCircle);
        btnHoughCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, HoughCircleActivity.class);
                startActivity(intent);
            }
        });

        Button btnHoughLine = findViewById(R.id.btnHoughLine);
        btnHoughLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, HoughLineActivity.class);
                startActivity(intent);
            }
        });
    }

    public native String stringFromJNI();
    public native String string2FromJNI();
}
