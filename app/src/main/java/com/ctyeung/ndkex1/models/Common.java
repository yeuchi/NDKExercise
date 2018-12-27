package com.ctyeung.ndkex1.models;

import com.ctyeung.ndkex1.models.KernelFactory;

public class Common {
    public Kernel mKernel;

    public int mThreshold;
    public int mMinThreshold = 0;
    public int mMaxThreshold = 255;

    public Common()
    {
        mThreshold = 100;
        mKernel = KernelFactory.isotropicDerivative();

    }

    public Common(int threshold)
    {
        mThreshold = threshold;
        mKernel = KernelFactory.isotropicDerivative();
    }

    public void onThresholdChange(int oldValue,
                                  int newValue)
    {
        mThreshold = newValue;
    }
}
