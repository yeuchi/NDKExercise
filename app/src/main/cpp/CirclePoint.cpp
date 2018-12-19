//
// Created by yeuchi on 12/19/18.
//
// Module:      CirclePoint.cpp
//
// Description: Circle data value object to store x,y and count
//

#include <jni.h>
#include "CirclePoint.h"

//////////////////////////////////////////////////////////////////
// Construct / Destruct

CirclePoint::CirclePoint(int x,
                         int y,
                         int count)
{
    mX = x;
    mY = y;
    mCount = count;
    next = NULL;
}

CirclePoint::~CirclePoint()
{

}

/*
 * Add an instance of oject
 */
bool CirclePoint::Push(int x,
                       int y,
                       int count)
{
   if(NULL!=next)
       return next->Push(x, y, count);

   next = new CirclePoint(x, y, count);

   return true;
}

/*
 * Return last instance of linklist
 * IMPORTANT: YOU must delete the resource manually after pop !!!
 */
CirclePoint* CirclePoint::Pop()
{
    if(IsLast())
        return this;

    if(next->IsLast())
    {
        CirclePoint* circlePoint = next->Pop();
        next = NULL;
        return circlePoint;
    }

    return next->Pop();
}

/*
 * Return depth of linklist
 */
int CirclePoint::Size()
{
    if(NULL!=next)
        return (next->Size() + 1);

    return 1;
}

bool CirclePoint::IsLast()
{
    return (NULL==next)?true:false;
}
