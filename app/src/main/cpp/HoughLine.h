//
// Created by yeuchi on 12/21/18.
//

#ifndef NDKEXERCISE_HOUGHLINE_H
#define NDKEXERCISE_HOUGHLINE_H


class HoughLine {

public:
    HoughLine();
    ~HoughLine();

public:
    bool CreateRhoTheta(int angle,
                        AndroidBitmapInfo infoSource,
                        void* pixelsSource);

    bool DrawLines(int threshold,
                   AndroidBitmapInfo infoSource,
                   void* pixelsSource,
                   AndroidBitmapInfo infoDestination,
                   void* pixelsDestination);

    void ReleaseHistogram();

protected:
    bool InitHistogram(int width, int height);

public:
    uint16_t *mHistogram;
    int mHistLength;

private:
    int maxXIndex;
    int maxYIndex;
    int mAngle;
};


#endif //NDKEXERCISE_HOUGHLINE_H
