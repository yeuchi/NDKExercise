package com.ctyeung.ndkex1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.json.JSONArray;

/*
 * Reference:
 * https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
 *
 * ibmphotophun.c
 *
 * Author: Frank Ableson
 * Contact Info: fableson@msiservices.com
 */
public class HoughLineActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private int mAngle = 40;
    private int mThreshold = 79;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hough_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this.getApplicationContext();

        intUIControlls();
        runJNICode();
    }

    /*
     * initialize UI controlls + handlers
     * - move this into MVP presenter class
     */
    private void intUIControlls()
    {
        NumberPicker thresholdPicker = findViewById(R.id.num_threshold);
        thresholdPicker.setMaxValue(255);
        thresholdPicker.setMinValue(0);
        thresholdPicker.setValue(mThreshold);

        thresholdPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                mThreshold = newVal;
            }
        });

        NumberPicker radiusPicker = findViewById(R.id.num_angle);
        radiusPicker.setMaxValue(360);
        radiusPicker.setMinValue(0);
        radiusPicker.setValue(mAngle);

        radiusPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                mAngle = newVal;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_star);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runJNICode();
            }
        });
    }

    private void runJNICode()
    {
        try {

            // perform edge detection
            Bitmap bmpIn = BitmapFactory.decodeResource(getResources(), R.drawable.hexagons);
            Bitmap bmpOut = BitmapFactory.decodeResource(getResources(), R.drawable.hexagons);
            //Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), Config.ALPHA_8);

            imageConvolveFromJNI(bmpIn, bmpOut, BasicKernels.isotropicDerivative(), 3);

            // insert processed image
            ImageView ivDerivative = this.findViewById(R.id.image_hough_derivative);

            if (null != ivDerivative)
                ivDerivative.setImageBitmap(bmpOut);

            // perform Hough transform
            lineDetectFromJNI(bmpOut, bmpIn, mAngle, mThreshold);

            // highlight image
            ImageView ivHighlight = this.findViewById(R.id.image_hough_found);

            if (null != ivHighlight)
                ivHighlight.setImageBitmap(bmpIn);
        }
        catch (Exception ex)
        {
            Toast.makeText(this,
                    (String)ex.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
     * draw line highlight on image
     */
    private void highlightCircles(JSONArray circles)
    {
        HighlightView view = findViewById(R.id.image_hough_found);
        view.draw(circles, mAngle);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public native void imageConvolveFromJNI(Bitmap bmpIn,
                                            Bitmap BmpOut,
                                            int[] kernel,
                                            int kernelWidth);

    public native String lineDetectFromJNI(  Bitmap bmpIn,
                                             Bitmap bmpOut,
                                             int angle,
                                             int threshold);
}
