package com.ctyeung.ndkex1.viewModels;

import com.ctyeung.ndkex1.viewModels.KernelFactory;

public class Line extends Common
{

    public int mAngle;
    public int mMinAngle = 0;
    public int mMaxAngle = 360;

    public Line()
    {
        super();
        mAngle = 0;
    }

    public Line(int angle,
                int threshold)
    {
        super(threshold);
        mAngle = angle;
    }

    public void onAngleChange(int oldValue,
                               int newValue)
    {
        mAngle = newValue;
    }
}
