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

#define  LOG_TAG    "libibmphotophun"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
typedef struct
{
    uint8_t red;
    uint8_t green;
    uint8_t blue;
    uint8_t alpha;
} rgba;

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

extern "C" JNIEXPORT void JNICALL
Java_com_ctyeung_ndkex1_MainActivity_imageConvert2GrayFromJNI(
        JNIEnv *env,
        jobject obj,
        jobject bitmapcolor,
        jobject bitmapgray)
{
    AndroidBitmapInfo  infocolor;
    void*              pixelscolor;
    AndroidBitmapInfo  infogray;
    void*              pixelsgray;
    int                ret;
    int 			y;
    int             x;

    LOGI("convertToGray");
    if ((ret = AndroidBitmap_getInfo(env, bitmapcolor, &infocolor)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }


    if ((ret = AndroidBitmap_getInfo(env, bitmapgray, &infogray)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }


    LOGI("color image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infocolor.width,infocolor.height,infocolor.stride,infocolor.format,infocolor.flags);
    if (infocolor.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888 !");
        return;
    }


    LOGI("gray image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infogray.width,infogray.height,infogray.stride,infogray.format,infogray.flags);
    if (infogray.format != ANDROID_BITMAP_FORMAT_A_8) {
        LOGE("Bitmap format is not A_8 !");
        return;
    }


    if ((ret = AndroidBitmap_lockPixels(env, bitmapcolor, &pixelscolor)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmapgray, &pixelsgray)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    // modify pixels with image processing algorithm

    for (y=0;y<infocolor.height;y++) {
        rgba * line = (rgba *) pixelscolor;
        uint8_t * grayline = (uint8_t *) pixelsgray;
        for (x=0;x<infocolor.width;x++) {
            grayline[x] = 0.3 * line[x].red + 0.59 * line[x].green + 0.11*line[x].blue;
        }

        pixelscolor = (char *)pixelscolor + infocolor.stride;
        pixelsgray = (char *) pixelsgray + infogray.stride;
    }

    LOGI("unlocking pixels");
    AndroidBitmap_unlockPixels(env, bitmapcolor);
    AndroidBitmap_unlockPixels(env, bitmapgray);
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
    void*              currentpixelssource;
    AndroidBitmapInfo  infoconvolved;
    void*              pixelsconvolved;
    int                ret;
    int 			    y, cy;
    int                 x, cx;

    // initializations, declarations, etc
    jint *c_array;

    // get a pointer to the array
    c_array = env->GetIntArrayElements(arr, NULL);

    // do some exception checking
    if (c_array == NULL) {
        return; // exception occurred
    }

    LOGI("convolution");
    if ((ret = AndroidBitmap_getInfo(env, bitmapsource, &infosource)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }


    if ((ret = AndroidBitmap_getInfo(env, bitmapconvolved, &infoconvolved)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }

    LOGI("source image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infosource.width,infosource.height,infosource.stride,infosource.format,infosource.flags);
    if (infosource.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888 !");
        return;
    }


    LOGI("convolved image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infoconvolved.width,infoconvolved.height,infoconvolved.stride,infoconvolved.format,infoconvolved.flags);
    if (infoconvolved.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
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

    if(0==kernelWidth%2)
    {
        // kernel width must be odd number
        return;
    }

    int pad = (1==kernelWidth)?0:(kernelWidth-1)/2;
    int denominator = 0;

    for(int k=0; k<kernelWidth*kernelWidth; k++)
        denominator += c_array[k];

    // modify pixels with image processing algorithm
    for (y=pad;y<infosource.height-pad;y++)
    {
        rgba * destline = (rgba *) pixelsconvolved;
        for (x=pad;x<infosource.width-pad;x++)
        {
            double integralR = 0;
            double integralG = 0;
            double integralB = 0;

            if(0==pad)
            {
                // identity kernel = 1  for debugging only
                currentpixelssource = (char *)pixelssource + (infosource.stride * y);
                rgba * line = (rgba *) currentpixelssource;
                integralR =  line[x].red;
                integralG =  line[x].green;
                integralB =  line[x].blue;
            }
            else
            {
                // perform convolution with kernel
                int ki = 0;
                for(cy=0-pad; cy<=pad; cy++)
                {
                    currentpixelssource = (char *)pixelssource + (infosource.stride * (y+cy));
                    rgba * line = (rgba *) currentpixelssource;

                    for(cx=0-pad; cx<=pad; cx++)
                    {
                        int i = x+cx;
                        int kernelValue = c_array[ki++];
                        integralR += line[i].red * kernelValue;
                        integralG += line[i].green * kernelValue;
                        integralB += line[i].blue * kernelValue;
                    }
                }
            }

            destline[x].alpha = 255;   // alpha channel
            destline[x].red = (int)(integralR / denominator); // red
            destline[x].green = (int)(integralG / denominator);   // green
            destline[x].blue = (int)(integralB / denominator);  // blue
        }

        pixelsconvolved = (char *) pixelsconvolved + infoconvolved.stride;
    }

    LOGI("unlocking pixels");
    AndroidBitmap_unlockPixels(env, bitmapsource);
    AndroidBitmap_unlockPixels(env, bitmapconvolved);

    // release the memory so java can have it again
    env->ReleaseIntArrayElements(arr, c_array, 0);
}

