//
// Created by yeuchi on 12/21/18.
//

#include <jni.h>
#include <math.h>
#include <android/log.h>
#include <android/bitmap.h>
#include "Common.h"
#include "HoughLine.h"


//////////////////////////////////////////////////////////////////
// Construct / Destruct

HoughLine::HoughLine()
{
    mHistogram = NULL;
    mHistLength = 0;
    maxXIndex = 0;
    maxYIndex = 0;

    mAngle = -1;
}

HoughLine::~HoughLine()
{
    ReleaseHistogram();
}

//////////////////////////////////////////////////////////////////
// Public methods

bool HoughLine::CreateRhoTheta(int angle,
                              AndroidBitmapInfo infoSource,
                              void* pixelsSource)
{
    // need to release memory first
    if(NULL!=mHistogram)
        return false;

    mAngle = angle;

    // assume 3x3 kernel so outter pixels are useless
    maxYIndex = infoSource.height-1;
    maxXIndex = infoSource.width-1;
    void* currentPixelsSource;

    try {
        // create histogram
        InitHistogram(maxXIndex, maxYIndex);
        float radian = (float)angle * 3.1416 / 180.0;

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
                    int rho = x * cos(radian) + y * sin(radian);

                    if(rho < mHistLength)
                        mHistogram[rho] ++;
                }
            }
        }

        return true;
    }
    catch (...)
    {
        return false;

    }
}

bool HoughLine::DrawLines(int threshold,
                          AndroidBitmapInfo infoSource,
                          void* pixelsSource,
                          AndroidBitmapInfo infoDestination,
                          void* pixelsDestination)
{
    if(NULL==mHistogram)
        return false;

    void* currentPixelsSource;

    try {
        float radian = (float)mAngle * 3.1416 / 180.0;

        // step through threshold image
        for(int y=1; y<maxYIndex; y++)
        {
            currentPixelsSource = (char *)pixelsSource + (infoSource.stride * y);
            rgba * line = (rgba *) currentPixelsSource;
            rgba * destline = (rgba *) pixelsDestination;

            for(int x=1; x<maxXIndex; x++)
            {
                // peak on any color channel is considered
                if(line[x].red==0xFF ||
                   line[x].green==0xFF ||
                   line[x].blue==0xFF)
                {
                    int rho = x * cos(radian) + y * sin(radian);

                    if(rho < mHistLength)
                    {
                        if(mHistogram[rho] > threshold)
                        {
                            // draw highlight
                          //  destline[x].alpha = 255;    // alpha channel
                            destline[x].red = 0;        // red
                            destline[x].green = 0;    // green
                            destline[x].blue = 0;     // blue
                        }
                    }
                }
            }
            pixelsDestination = (char *) pixelsDestination + infoDestination.stride;
        }

        return true;
    }
    catch (...)
    {
        return false;

    }
}

//////////////////////////////////////////////////////////////////
// Protected / Private methods

bool HoughLine::InitHistogram(int width, int height)
{
    if(NULL!=mHistogram)
        return false;

    mHistLength = sqrt(width*width+height*height);
    mHistogram = new uint16_t[mHistLength]();

    // is this necessary ?
    for(int j=0; j<mHistLength; j++)
        mHistogram[j] = 0;
    return true;
}

void HoughLine::ReleaseHistogram()
{
    if(NULL != mHistogram)
    {
        delete mHistogram;
        mHistogram = NULL;
        mHistLength = 0;
    }
}