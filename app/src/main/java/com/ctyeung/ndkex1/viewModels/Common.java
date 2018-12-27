package com.ctyeung.ndkex1.viewModels;

import com.ctyeung.ndkex1.viewModels.KernelFactory;

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
