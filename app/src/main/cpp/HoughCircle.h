//
// Created by yeuchi on 12/18/18.
//
// Module:      Hough.h
//
// Description: Demonstrate integration of a C++ class in Android NDK
//

#ifndef NDKEXERCISE_HOUGH_H
#define NDKEXERCISE_HOUGH_H

#endif //NDKEXERCISE_HOUGH_H


class HoughCircle
{
public:
    HoughCircle();
    ~HoughCircle();

public:
    bool CreateRhoTheta(  float radius,
                          AndroidBitmapInfo infoSource,
                          void* pixelsSource);

    CirclePoint* EvaluatePlot(int threshold);

    void ReleasePlot();

private:
    uint8_t *mHistogram;
    int maxXIndex;
    int maxYIndex;
};
