//
// Created by yeuchi on 12/18/18.
//
// Module:      Hough.cpp
//
// Description: Demonstrate integration of a C++ class in Android NDK
//

#include <jni.h>
#include <math.h>
#include <android/log.h>
#include <android/bitmap.h>
#include "Common.h"
#include "CirclePoint.h"
#include "HoughCircle.h"

//////////////////////////////////////////////////////////////////
// Construct / Destruct

HoughCircle::HoughCircle()
{
    mHistogram = NULL;
    maxXIndex = 0;
    maxYIndex = 0;
}

HoughCircle::~HoughCircle()
{
    ReleasePlot();
}

//////////////////////////////////////////////////////////////////
// Public methods

void HoughCircle::ReleasePlot()
{
    if(NULL != mHistogram)
    {
        delete mHistogram;
        mHistogram = NULL;
    }
}
/*
 * Create Rho & Theta plot from Image
 * 0. input image assumed threshold already
 * 1. perform 1st derivative -> edge detection
 * 2. calulate rho + theta -> plot
 */
bool HoughCircle::CreateRhoTheta(  float radius,
                                   AndroidBitmapInfo infoSource,
                                   void* pixelsSource)
{
    // need to release memory first
    if(NULL!=mHistogram)
        return false;

    // assume 3x3 kernel so outter pixels are useless
    maxYIndex = infoSource.height-1;
    maxXIndex = infoSource.width-1;
    void* currentPixelsSource;

    try{
        // create a histogram; initialize zero
        int length = maxXIndex*maxYIndex;
        mHistogram = new uint8_t[length]();
        for(int j=0; j<length; j++)
            mHistogram[j] = 0;

        // step through threshold image
        for(int y=1; y<maxYIndex; y++)
        {
            currentPixelsSource = (char *)pixelsSource + (infoSource.stride * y);
            rgba * line = (rgba *) currentPixelsSource;

            for(int x=1; x<maxXIndex; x++)
            {
                // peak on any color channel is considered
                if(line[x].red==0xFF ||
                   line[x].green==0xFF ||
                   line[x].blue==0xFF)
                {
                    // draw a circle around highlighted pixel
                    for(int degree=0; degree<360; degree++)
                    {
                        // degree to radian
                        float theta = (float)degree * 3.1416 / 180.0;
                        float a = x - radius * cos(theta);
                        float b = y - radius * sin(theta);
                        if((a<maxXIndex && a>=0)&&
                           (b<maxYIndex && b>=0))
                        {
                            int i = (int)b*maxXIndex+(int)a;
                            mHistogram[i]++;
                        }
                    }
                }

            }
        }

        // result is a 2D histogram of intersections; highest point is circle center
        return true;
    }
    catch (...)
    {
        return false;
    }
}

/*
 * Find all circles with count greater than threshold
 * - return circlePoint linklist object
 */
CirclePoint* HoughCircle::EvaluatePlot(int threshold)
{
    if(NULL==mHistogram)
        return NULL;

    CirclePoint* circles = NULL;
    int length = maxXIndex * maxYIndex;
    for(int y=0; y<maxYIndex; y++)
    {
        for(int x=0; x<maxXIndex; x++)
        {
            int i = y * maxXIndex + x;
            if(mHistogram[i]>threshold)
            {
                if(NULL==circles)
                    circles = new CirclePoint(x, y, mHistogram[i]);
                else
                    circles->Push(x, y, mHistogram[i]);
            }
        }
    }
    return circles;
}

