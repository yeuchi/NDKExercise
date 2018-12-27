package com.ctyeung.ndkex1.viewModels;

import com.ctyeung.ndkex1.viewModels.KernelFactory;

public class Circle extends Common
{
    public int mRadius;
    public int mMinRadius = 0;
    public int mMaxRadius = 300;

    public Circle()
    {
        super();
        this.mRadius = 1;
    }

    public Circle(int radius,
                  int threshold)
    {
        super(threshold);
        this.mRadius = radius;
    }

    public void onRadiusChange(int oldValue,
                                int newValue)
    {
        mRadius = newValue;
    }
}
