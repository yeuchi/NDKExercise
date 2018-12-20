package com.ctyeung.ndkex1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ctyeung.ndkex1.data.JSONhelper;
import com.ctyeung.ndkex1.data.ListGridAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private Context mContext;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hough);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this.getApplicationContext();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        initGrid();
        runJNICode();
    }

    private void initGrid()
    {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_circles);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /*
     * display circle information in list
     */
    private void populateGridList(JSONArray circles)
    {
        ListGridAdapter adapter = new ListGridAdapter(circles);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
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
            String jsonString = circleDetectFromJNI(bmpOut, mRadius, mThreshold);

            // parse json
            JSONArray circles = parseCircleJson(jsonString);

            if(null!=circles) {
                // log circles information to list
                populateGridList(circles);

                // draw highlight circles found
                highlightCircles(circles);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,
                    (String)ex.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Handle NDK Hough results
     * 1. parse string -> json -> json array
     * 2. update UI circle count
     * 3. return null or json array (if any circles)
     */
    private JSONArray parseCircleJson(String jsonString)
    {
        try {
            JSONObject json = JSONhelper.parseJson(jsonString);
            JSONArray circles = JSONhelper.getJsonArray(json, "circles");

            String numCircles = JSONhelper.parseValueByKey(json, "total");
            TextView textView = (TextView) findViewById(R.id.text_circles);
            textView.setText("Number Circles: " + numCircles);
            int count = Integer.valueOf(numCircles);
            return (count > 0) ? circles : null;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /*
     * draw circle highlight on image
     */
    private void highlightCircles(JSONArray circles)
    {

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
