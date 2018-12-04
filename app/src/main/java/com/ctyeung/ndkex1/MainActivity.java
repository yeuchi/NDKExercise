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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI() + string2FromJNI());

        runJNICode_Color2Gray();
    }

    private int[] getBlurKernel()
    {
        int kernelWidth = 5;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = 1;
        kernel[1] = 1;
        kernel[2] = 1;
        kernel[3] = 1;
        kernel[4] = 1;

        kernel[5] = 1;
        kernel[6] = 1;
        kernel[7] = 1;
        kernel[8] = 1;
        kernel[9] = 1;

        kernel[10] = 1;
        kernel[11] = 1;
        kernel[12] = 1;
        kernel[13] = 1;
        kernel[14] = 1;

        kernel[15] = 1;
        kernel[16] = 1;
        kernel[17] = 1;
        kernel[18] = 1;
        kernel[19] = 1;

        kernel[20] = 1;
        kernel[21] = 1;
        kernel[22] = 1;
        kernel[23] = 1;
        kernel[24] = 1;

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
    private void runJNICode_Color2Gray()
    {
        try {

            Bitmap bmpIn = BitmapFactory.decodeResource(getResources(), R.drawable.white_lion_small);
            Bitmap bmpOut = BitmapFactory.decodeResource(getResources(), R.drawable.white_lion_small_gray);
            //Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), Config.ALPHA_8);
            //imageConvert2GrayFromJNI(bmpIn, bmpOut);

            int kernelWidth = 3;
            int[] kernel = getBlurKernel();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
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
