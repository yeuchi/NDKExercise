/*
 * Reference:
 * https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
 * https://stackoverflow.com/questions/4841345/sending-ints-between-java-and-c
 * ibmphotophun.c
 *
 * Original Author: Frank Ableson
 * Contact Info: fableson@msiservices.com
 */
#include <jni.h>
#include <stdio.h>
#include <string>
#include <sstream>

#include <android/log.h>
#include <android/bitmap.h>
#include "Convolution.h"
#include "CirclePoint.h"
#include "HoughCircle.h"

#define  LOG_TAG    "libibmphotophun"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

AndroidBitmapInfo  infoSource;
void*              pixelsSource;
AndroidBitmapInfo  infoDestination;
void*              pixelsDestination;
int                ret;

bool initializeBitmap(JNIEnv *env,
                       jobject bitmapSource)
{
    if ((ret = AndroidBitmap_getInfo(env, bitmapSource, &infoSource)) < 0)
    {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return false;
    }

    LOGI("source image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infoSource.width,infoSource.height,infoSource.stride,infoSource.format,infoSource.flags);
    if (infoSource.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
        LOGE("Bitmap format is not RGBA_8888 !");
        return false;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmapSource, &pixelsSource)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return false;
    }

    return true;
}

bool releaseBitmap(JNIEnv *env,
                   jobject bitmapSource)
{
    if(NULL==bitmapSource)
        return false;

    AndroidBitmap_unlockPixels(env, bitmapSource);
    return true;
}

bool initializeBitmaps(JNIEnv *env,
                       jobject bitmapSource,
                       jobject bitmapDestination)
{
    if ((ret = AndroidBitmap_getInfo(env, bitmapDestination, &infoDestination)) < 0)
    {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return false;
    }

    LOGI("convolved image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infoDestination.width,infoDestination.height,infoDestination.stride,infoDestination.format,infoDestination.flags);
    if (infoDestination.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
        //    if (infoconvolved.format != ANDROID_BITMAP_FORMAT_A_8) {
        LOGE("Bitmap format is not A_8 !");
        return false;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmapDestination, &pixelsDestination)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return false;
    }

    return initializeBitmap(env, bitmapSource);
}

bool releaseBitmaps(JNIEnv *env,
                    jobject bitmapSource,
                    jobject bitmapDestination)
{
    if(NULL==bitmapDestination)
        return false;

    AndroidBitmap_unlockPixels(env, bitmapDestination);
    return releaseBitmap(env, bitmapSource);
}

/*
 * Hough transform base on Frank Ableson's gray conversion
 */

extern "C" JNIEXPORT jstring JNICALL
Java_com_ctyeung_ndkex1_HoughActivity_circleDetectFromJNI(
        JNIEnv *env,
        jobject obj,
        jobject bitmapSource,
        jfloat radius,
        jint threshold)
{
    initializeBitmap(env, bitmapSource);

    HoughCircle houghCircle;
    houghCircle.CreateRhoTheta(radius, infoSource, pixelsSource);
    CirclePoint* linkList = houghCircle.EvaluatePlot(threshold);

    std::string string="";
    std::ostringstream os;

    if(NULL!=linkList)
    {
        int numCircles = linkList->Size();
        os << "{\"total\":" << numCircles << ",\"circles\":[";

        for(int i=0; i<numCircles; i++)
        {
            CirclePoint* circle = linkList->Pop();
            if(NULL!=circle)
            {
                if(i>0)
                    os << ",";

                os << "{ \"index\":" << i << ",\"x\":" << circle->mX << ",\"y\":" << circle->mY << ",\"count\":" <<circle->mCount <<"}";
                delete circle;
            }
        }
        os << "]}";
        //delete linkList;
        string = os.str();
    }

    releaseBitmap(env, bitmapSource);
    LOGI("unlocking pixels");

    return env->NewStringUTF(string.c_str());
}

void Convolve(JNIEnv *env,
                 jobject obj,
                 jobject bitmapsource,
                 jobject bitmapconvolved,
                 jintArray arr,
                 jint kernelWidth)
{
    // initializations, declarations, etc
    jint *c_array;

    // get a pointer to the array
    c_array = env->GetIntArrayElements(arr, NULL);

    // do some exception checking
    if (c_array == NULL) {
        return; // exception occurred
    }

    initializeBitmaps(env, bitmapsource, bitmapconvolved);

    Convolution convolution;
    convolution.LoadKernel(&c_array[0], kernelWidth);
    convolution.Convolve(infoSource, pixelsSource, infoDestination, pixelsDestination);

    releaseBitmaps(env, bitmapsource, bitmapconvolved);
    LOGI("unlocking pixels");

    // release the memory so java can have it again
    env->ReleaseIntArrayElements(arr, c_array, 0);
}

extern "C" JNIEXPORT void JNICALL
Java_com_ctyeung_ndkex1_HoughActivity_imageConvolveFromJNI(
        JNIEnv *env,
        jobject obj,
        jobject bitmapsource,
        jobject bitmapconvolved,
        jintArray arr,
        jint kernelWidth)
{
    Convolve(env, obj, bitmapsource, bitmapconvolved, arr, kernelWidth);
}

/*
 * Convolution filter base on Frank Ableson's gray conversion
 */
extern "C" JNIEXPORT void JNICALL
Java_com_ctyeung_ndkex1_ConvolutionActivity_imageConvolveFromJNI(
        JNIEnv *env,
        jobject obj,
        jobject bitmapsource,
        jobject bitmapconvolved,
        jintArray arr,
        jint kernelWidth)
{
    Convolve(env, obj, bitmapsource, bitmapconvolved, arr, kernelWidth);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ctyeung_ndkex1_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */)
{
    std::string hello = "Hello world";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ctyeung_ndkex1_MainActivity_string2FromJNI(
        JNIEnv *env,
        jobject /* this */)
{
    std::string hello = " from C++";
    return env->NewStringUTF(hello.c_str());
}






