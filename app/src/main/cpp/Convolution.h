//
// Created by yeuchi on 12/10/18.
//
// Module:      Convolution.h
//
// Description: Demonstrate integration of a C++ class in Android NDK
//

#ifndef NDKEXERCISE_CONVOLUTION_H
#define NDKEXERCISE_CONVOLUTION_H

#endif //NDKEXERCISE_CONVOLUTION_H

class Convolution
{
public:
    Convolution();
    ~Convolution();

public:
    bool LoadKernel(jint *kernel, int kernelWidth);
    bool Convolve(AndroidBitmapInfo infoSource,
                  void* pixelsSource,
                  AndroidBitmapInfo infoConvolved,
                  void* pixelsConvolved);

protected:
    void FindKernelDenominator();
    int bound(double value);

private:

    jint * mKernel;
    int mKernelWidth;
    int mDenominator;
};