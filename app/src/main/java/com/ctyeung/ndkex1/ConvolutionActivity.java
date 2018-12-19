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
import android.view.Menu;
import android.view.MenuItem;

/*
 * Reference:
 * https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
 *
 * ibmphotophun.c
 *
 * Author: Frank Ableson
 * Contact Info: fableson@msiservices.com
 */
public class ConvolutionActivity extends AppCompatActivity
{

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convolution);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setDisplayHomeAsUpEnabled(true);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mTextView = findViewById(R.id.filter_selection);
        String filterType = getResources().getString(R.string.filter_derivative_h);
        mTextView.setText(filterType);
        int kernelWidth = 3;
        int[] kernel = BasicKernels.horizontalDerivative();
        runJNICode_Convolution(kernel, kernelWidth);
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
        getMenuInflater().inflate(R.menu.menu_convolution, menu);
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
                kernel = BasicKernels.identity();
                kernelWidth = 1;
                break;

            case R.id.filter_deriviative_h:
                filterType = getResources().getString(R.string.filter_derivative_h);
                kernel = BasicKernels.horizontalDerivative();
                kernelWidth = 3;
                break;

            case R.id.filter_deriviative_v:
                filterType = getResources().getString(R.string.filter_derivative_v);
                kernel = BasicKernels.verticalDerivative();
                kernelWidth = 3;
                break;

            case R.id.filter_deriviative_isotropic:
                filterType = getResources().getString(R.string.filter_derivative);
                kernel = BasicKernels.isotropicDerivative();
                kernelWidth = 3;
                break;

            case R.id.filter_blur:
                filterType = getResources().getString(R.string.filter_blur);
                kernel = BasicKernels.blur();
                kernelWidth = 7;
                break;

            case R.id.filter_sharpen:
                filterType = getResources().getString(R.string.filter_sharpen);
                kernel = BasicKernels.sharpen();
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

    public native void imageConvolveFromJNI(Bitmap bmpIn, Bitmap BmpOut, int[] kernel, int kernelWidth);

}
