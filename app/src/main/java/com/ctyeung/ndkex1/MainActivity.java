package com.ctyeung.ndkex1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/*
 * Reference:
 * https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
 *
 * ibmphotophun.c
 *
 * Author: Frank Ableson
 * Contact Info: fableson@msiservices.com
 */
public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        mTextView = findViewById(R.id.filter_selection);
        String filterType = getResources().getString(R.string.filter_derivative_h);
        mTextView.setText(filterType);
        int kernelWidth = 3;
        int[] kernel = getHorizontalDerivativeKernel();
        runJNICode_Convolution(kernel, kernelWidth);
    }

    private int[] getHorizontalDerivativeKernel()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = -1;
        kernel[1] = 1;
        kernel[2] = 0;

        kernel[3] = -1;
        kernel[4] = 1;
        kernel[5] = 0;

        kernel[6] = -1;
        kernel[7] = 1;
        kernel[8] = 0;

        return kernel;
    }

    private int[] getVerticalDerivativeKernel()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = -1;
        kernel[1] = -1;
        kernel[2] = -1;

        kernel[3] = 1;
        kernel[4] = 1;
        kernel[5] = 1;

        kernel[6] = 0;
        kernel[7] = 0;
        kernel[8] = 0;

        return kernel;
    }

    private int[] getIsotropicDerivativeKernel()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = -1;
        kernel[1] = -1;
        kernel[2] = -1;

        kernel[3] = -1;
        kernel[4] = 8;
        kernel[5] = -1;

        kernel[6] = -1;
        kernel[7] = -1;
        kernel[8] = -1;

        return kernel;
    }

    private int[] getSharpenKernel()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = -1;
        kernel[1] = -1;
        kernel[2] = -1;

        kernel[3] = -1;
        kernel[4] = 10;
        kernel[5] = -1;

        kernel[6] = -1;
        kernel[7] = -1;
        kernel[8] = -1;

        return kernel;
    }

    private int[] getBlurKernel()
    {
        int kernelWidth = 7;
        int[] kernel = new int[kernelWidth*kernelWidth];
        for(int i=0; i<kernel.length; i++)
            kernel[i] = 1;

        return kernel;
    }

    private int[] getIdentityKernel()
    {
        int kernelWidth = 1;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = 1;

        return kernel;
    }

    /*
     * https://stackoverflow.com/questions/4939266/android-bitmap-native-code-linking-problem
     * need to set option in CMakeLists.txt
     */
    private void runJNICode_Convolution(int[] kernel,
                                        int kernelWidth)
    {
        try {

            Bitmap bmpIn = BitmapFactory.decodeResource(getResources(), R.drawable.white_lion_small);
            Bitmap bmpOut = BitmapFactory.decodeResource(getResources(), R.drawable.white_lion_small_gray);
            //Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), Config.ALPHA_8);

            imageConvolveFromJNI(bmpIn, bmpOut, kernel, kernelWidth);

            // insert processed image
            ImageView imageView = this.findViewById(R.id.image_proccessed);

            if (null != imageView)
                imageView.setImageBitmap(bmpOut);
        }
        catch (Exception ex)
        {
            Toast.makeText(this,
                    (String)ex.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int kernelWidth;
        int[] kernel;

        String filterType;
        //noinspection SimplifiableIfStatement
        switch (id)
        {
            default:
            case R.id.filter_identity:
                filterType = getResources().getString(R.string.filter_identity);
                kernel = getIdentityKernel();
                kernelWidth = 1;
                break;

            case R.id.filter_deriviative_h:
                filterType = getResources().getString(R.string.filter_derivative_h);
                kernel = getHorizontalDerivativeKernel();
                kernelWidth = 3;
                break;

            case R.id.filter_deriviative_v:
                filterType = getResources().getString(R.string.filter_derivative_v);
                kernel = getVerticalDerivativeKernel();
                kernelWidth = 3;
                break;

            case R.id.filter_deriviative_isotropic:
                filterType = getResources().getString(R.string.filter_derivative);
                kernel = getIsotropicDerivativeKernel();
                kernelWidth = 3;
                break;

            case R.id.filter_blur:
                filterType = getResources().getString(R.string.filter_blur);
                kernel = getBlurKernel();
                kernelWidth = 7;
                break;

            case R.id.filter_sharpen:
                filterType = getResources().getString(R.string.filter_sharpen);
                kernel = getSharpenKernel();
                kernelWidth = 3;
                break;
        }

        mTextView.setText(filterType);
        runJNICode_Convolution(kernel, kernelWidth);
        return true;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String string2FromJNI();
    public native void imageConvert2GrayFromJNI(Bitmap bmpIn, Bitmap BmpOut);
    public native void imageConvolveFromJNI(Bitmap bmpIn, Bitmap BmpOut, int[] kernel, int kernelWidth);

}
