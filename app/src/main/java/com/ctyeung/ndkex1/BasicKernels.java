package com.ctyeung.ndkex1;

public class BasicKernels {

    public static int[] horizontalDerivative()
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

    public static int[] verticalDerivative()
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

    public static int[] isotropicDerivative()
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

    public static int[] sharpen()
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

    public static int[] blur()
    {
        int kernelWidth = 7;
        int[] kernel = new int[kernelWidth*kernelWidth];
        for(int i=0; i<kernel.length; i++)
            kernel[i] = 1;

        return kernel;
    }

    public static int[] identity()
    {
        int kernelWidth = 1;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = 1;

        return kernel;
    }
}
