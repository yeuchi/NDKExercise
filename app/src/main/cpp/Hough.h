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

class Hough
{
public:
    Hough();
    ~Hough();

public:
    bool Lines(float angle, float threshold);
    bool Circles(float radius, float threshold);

protected:
    bool Threshold();
    bool EdgeDetection();

private:
};
