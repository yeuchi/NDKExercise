package com.ctyeung.ndkex1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.ctyeung.ndkex1.utils.JSONhelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class HighlightView extends View
{
    // setup initial color
    private final int paintColor = Color.CYAN;

    // defines paint and canvas
    private Paint drawPaint;
    private JSONArray mCircles;
    private float mRadius;

    public HighlightView(Context context,
                         AttributeSet attrs)
    {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint(); // same as before
    }

    // Draw each circle onto the view
    @Override
    protected void onDraw(Canvas canvas) {

        if(null==mCircles)
            return;

        for(int i=0; i<mCircles.length(); i++)
        {
            try {
                JSONObject circle = mCircles.getJSONObject(i);
                String x = JSONhelper.parseValueByKey(circle, "x");
                String y = JSONhelper.parseValueByKey(circle, "y");
                String c = JSONhelper.parseValueByKey(circle, "count");

                int iX = Integer.valueOf(x);
                int iY = Integer.valueOf(y);
                int iCount = Integer.valueOf(c);
                canvas.drawCircle(iX, iY, mRadius, drawPaint);

                // draw count ??
            }
            catch (Exception ex)
            {

            }
        }

    }

    public void draw(JSONArray circlePoints,
                     float radius)
    {
        mCircles = circlePoints;
        mRadius = radius;

        invalidate();
    }

    // Setup paint with color and stroke styles
    private void setupPaint()
    {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(1);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStyle(Paint.Style.STROKE); // change to fill
    }
}
