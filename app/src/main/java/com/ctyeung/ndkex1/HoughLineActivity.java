package com.ctyeung.ndkex1;

import com.ctyeung.ndkex1.models.KernelFactory;
import com.ctyeung.ndkex1.databinding.ActivityHoughLineBinding;
import com.ctyeung.ndkex1.models.Kernel;


import android.content.Context;
import android.databinding.DataBindingUtil;
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


import com.ctyeung.ndkex1.models.Kernel;
import com.ctyeung.ndkex1.models.Circle;
import com.ctyeung.ndkex1.models.Line;

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
public class HoughLineActivity extends AppCompatActivity implements IUIEvents {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    ActivityHoughLineBinding activityHoughLineBinding;
    Line mLine;

    private int DEFAULT_ANGLE = 40;
    private int DEFAULT_THRESHOLD = 69;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLine = new Line(DEFAULT_ANGLE, DEFAULT_THRESHOLD);
        activityHoughLineBinding = DataBindingUtil.setContentView(this, R.layout.activity_hough_line);
        activityHoughLineBinding.setLine(mLine);
        activityHoughLineBinding.setUiEvent(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this.getApplicationContext();

        runJNICode();
    }

    public void onActionButtonClick()
    {
        runJNICode();
    }

    private void runJNICode()
    {
        try {

            // perform edge detection
            Bitmap bmpIn = BitmapFactory.decodeResource(getResources(), R.drawable.hexagons);
            Bitmap bmpOut = BitmapFactory.decodeResource(getResources(), R.drawable.hexagons);
            //Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), Config.ALPHA_8);

            Kernel kernel = KernelFactory.isotropicDerivative();
            imageConvolveFromJNI(bmpIn, bmpOut, kernel.mValues, kernel.mWidth);

            // insert processed image
            ImageView ivDerivative = this.findViewById(R.id.image_hough_derivative);

            if (null != ivDerivative)
                ivDerivative.setImageBitmap(bmpOut);

            // perform Hough transform
            lineDetectFromJNI(bmpOut, bmpIn, mLine.mAngle, mLine.mThreshold);

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
