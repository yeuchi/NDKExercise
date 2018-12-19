//
// Created by yeuchi on 12/19/18.
//
// Module:      CirclePoint.h
//
// Description: Information about a circle found
//

#ifndef NDKEXERCISE_CIRCLEPOINT_H
#define NDKEXERCISE_CIRCLEPOINT_H


class CirclePoint {
public:
    CirclePoint(int x, int y, int count);
    ~CirclePoint();

public:
    bool Push(int x, int y, int count);
    CirclePoint* Pop();
    int Size();
    bool IsLast();

public:
    int mCount;
    int mX;
    int mY;
    CirclePoint* next;
};


#endif //NDKEXERCISE_CIRCLEPOINT_H
