package com.ctyeung.ndkex1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Reference:
 * https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
 *
 * ibmphotophun.c
 *
 * Author: Frank Ableson
 * Contact Info: fableson@msiservices.com
 */
public class HoughActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private float mRadius = 40;
    private int mThreshold = 184;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hough);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        runJNICode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hough, menu);
        return true;
    }

    private void runJNICode()
    {
        try {

            // perform edge detection
            Bitmap bmpIn = BitmapFactory.decodeResource(getResources(), R.drawable.circles);
            Bitmap bmpOut = BitmapFactory.decodeResource(getResources(), R.drawable.circles);
            //Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), Config.ALPHA_8);

            imageConvolveFromJNI(bmpIn, bmpOut, BasicKernels.isotropicDerivative(), 3);

            // insert processed image
            ImageView imageView = this.findViewById(R.id.image_hough_derivative);

            if (null != imageView)
                imageView.setImageBitmap(bmpOut);


            // perform Hough transform
            String str = circleDetectFromJNI(bmpOut, mRadius, mThreshold);

            TextView textView = (TextView)findViewById(R.id.text_circles);
            textView.setText(str);
        }
        catch (Exception ex)
        {
            Toast.makeText(this,
                    (String)ex.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            default:
            case R.id.hough_circle:
                runJNICode();
                break;

            case R.id.hough_line:
                break;
        }
        return true;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public native void imageConvolveFromJNI(Bitmap bmpIn,
                                            Bitmap BmpOut,
                                            int[] kernel,
                                            int kernelWidth);

    public native String circleDetectFromJNI(Bitmap bmpIn,
                                           float radius,
                                           int threshold);

}
