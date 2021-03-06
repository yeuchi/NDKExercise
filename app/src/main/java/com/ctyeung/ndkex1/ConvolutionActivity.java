package com.ctyeung.ndkex1;


import android.databinding.DataBindingUtil;
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

import com.ctyeung.ndkex1.viewModels.KernelFactory;
import com.ctyeung.ndkex1.databinding.ActivityConvolutionBinding;
import com.ctyeung.ndkex1.viewModels.Kernel;

/*
 * References:
 *
 * Navigation drawer by Rohit Kumar Kanojia
 * https://medium.com/quick-code/android-navigation-drawer-e80f7fc2594f
 *
 *
 * Author: Frank Ableson
 * https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
 */
public class ConvolutionActivity extends BaseNavDrawerActivity
{
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ActivityConvolutionBinding mActivityConvolutionBinding;
    private Kernel mKernel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convolution);

        mKernel = KernelFactory.horizontalDerivative();
        mActivityConvolutionBinding = DataBindingUtil.setContentView(this, R.layout.activity_convolution);
        mActivityConvolutionBinding.setKernel(mKernel);

        runJNICode();
        super.initDrawer(R.id.activity_convolution);
    }

    /*
     * https://stackoverflow.com/questions/4939266/android-bitmap-native-code-linking-problem
     * need to set option in CMakeLists.txt
     */
    private void runJNICode()
    {
        try {

            Bitmap bmpIn = BitmapFactory.decodeResource(getResources(), R.drawable.white_lion_small);
            Bitmap bmpOut = BitmapFactory.decodeResource(getResources(), R.drawable.white_lion_small_gray);

            imageConvolveFromJNI(bmpIn, bmpOut, mKernel.mValues, mKernel.mWidth);

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
        if(t.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();
        int kernelWidth;
        int[] kernel;

        String filterType;
        //noinspection SimplifiableIfStatement
        switch (id)
        {
            default:
                return false;

            case R.id.filter_identity:
                mKernel = KernelFactory.identity();
                break;

            case R.id.filter_deriviative_h:
                mKernel = KernelFactory.horizontalDerivative();
                break;

            case R.id.filter_deriviative_v:
                mKernel = KernelFactory.verticalDerivative();
                break;

            case R.id.filter_deriviative_isotropic:
                mKernel = KernelFactory.isotropicDerivative();
                break;

            case R.id.filter_blur:
                mKernel = KernelFactory.blur();
                break;

            case R.id.filter_sharpen:
                mKernel = KernelFactory.sharpen();
                break;
        }
        mActivityConvolutionBinding.setKernel(mKernel);
        runJNICode();
        return true;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public native void imageConvolveFromJNI(Bitmap bmpIn, Bitmap BmpOut, int[] kernel, int kernelWidth);

}
