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

#include <android/log.h>
#include <android/bitmap.h>
#include "Convolution.h"

#define  LOG_TAG    "libibmphotophun"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

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

/*
 * Convolution filter base on Frank Ableson's gray conversion
 */
extern "C" JNIEXPORT void JNICALL
Java_com_ctyeung_ndkex1_MainActivity_imageConvolveFromJNI(
        JNIEnv *env,
        jobject obj,
        jobject bitmapsource,
        jobject bitmapconvolved,
        jintArray arr,
        jint kernelWidth)
{

    AndroidBitmapInfo  infosource;
    void*              pixelssource;
    AndroidBitmapInfo  infoconvolved;
    void*              pixelsconvolved;
    int                ret;

    // initializations, declarations, etc
    jint *c_array;

    // get a pointer to the array
    c_array = env->GetIntArrayElements(arr, NULL);

    // do some exception checking
    if (c_array == NULL) {
        return; // exception occurred
    }

    LOGI("convolution");
    if ((ret = AndroidBitmap_getInfo(env, bitmapsource, &infosource)) < 0)
    {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }


    if ((ret = AndroidBitmap_getInfo(env, bitmapconvolved, &infoconvolved)) < 0)
    {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }

    LOGI("source image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infosource.width,infosource.height,infosource.stride,infosource.format,infosource.flags);
    if (infosource.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
        LOGE("Bitmap format is not RGBA_8888 !");
        return;
    }


    LOGI("convolved image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infoconvolved.width,infoconvolved.height,infoconvolved.stride,infoconvolved.format,infoconvolved.flags);
    if (infoconvolved.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
    //    if (infoconvolved.format != ANDROID_BITMAP_FORMAT_A_8) {
        LOGE("Bitmap format is not A_8 !");
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmapsource, &pixelssource)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmapconvolved, &pixelsconvolved)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    rgba * line = (rgba *) pixelssource + (infosource.stride * 1);

    Convolution convolution;
    convolution.LoadKernel(&c_array[0], kernelWidth);
    convolution.Convolve(infosource, pixelssource, infoconvolved, pixelsconvolved);

    LOGI("unlocking pixels");
    AndroidBitmap_unlockPixels(env, bitmapsource);
    AndroidBitmap_unlockPixels(env, bitmapconvolved);

    // release the memory so java can have it again
    env->ReleaseIntArrayElements(arr, c_array, 0);
}






