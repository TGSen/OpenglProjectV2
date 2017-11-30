/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/28 16:45
 * Des    : 
 */
//
#include <camera/sggl.h>
#include "camera/NativeOpencvOpstion.h"
#include "camera/sggl.h"
void nv21ToRGB(void *pixels, int width, int height){
    //获得的是nv21格式 nv21格式 y数据的高就是图像高，uv数据的高加起来只有图像的一半
    LOGE("sen0");
    Mat nv21(height + height / 2, width, CV_8UC1, pixels);
//    LOGE("sen10");
//    //把nv21格式的数据转为rgb
//    cvtColor(nv21, nv21, CV_YUV2RGB_NV21);
//    LOGE("sen20");
//    //前置摄像头 需要旋转90度 或者 -270度
//    //获得原图像宽高
//    Size src_sz = nv21.size();
//    LOGE("sen30");
//    //旋转后宽高
//    Size dst_sz(src_sz.height, src_sz.width);
//    LOGE("sen40");
//    //宽高的最大值
//    int len = max(nv21.cols, nv21.rows);
//    LOGE("sen50");
//    //获得仿射变换矩阵 该怎么旋转的一个信息记录者
//    Mat matrix = getRotationMatrix2D(Point2f(len / 2.0, len / 2.0),-90.0, 1.0);
//    LOGE("sen60");
//    //旋转
//    warpAffine(nv21, nv21, matrix, dst_sz);
    LOGE("sen70");
 //   imwrite("sdcard/saveBitmap.png", nv21);
    LOGE("sen80");
   // mat.release();
    LOGE("sen90");
//    matrix.release();
    LOGE("sen10");
};


JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_NativeOpencvOpstion_saveBitmap
        (JNIEnv * env, jclass jclazz, jobject jbitmap){
    //原图
    Mat src_image;
    Java_org_opencv_android_Utils_nBitmapToMat2(env, jclazz, jbitmap, (jlong) &src_image, 0);
   imwrite("sdcard/saveBitmap.png", src_image);

}



JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_NativeOpencvOpstion_saveBitmapByData(JNIEnv *env, jclass jclass,
                                                                                              jbyteArray data, jint width,
                                                                                              jint height){
    jbyte *cameraData = env->GetByteArrayElements(data, NULL);
    Mat nv21(height + height / 2, width, CV_8UC1, cameraData);
    Mat src;
    //把nv21格式的数据转为rgb
    cvtColor(nv21, src, CV_YUV2RGB_NV21);
    //前置摄像头 需要旋转90度 或者 -270度
    //获得原图像宽高
    Size src_sz = src.size();
    //旋转后宽高
    Size dst_sz(src_sz.height, src_sz.width);
    //宽高的最大值
    int len = max(src.cols, src.rows);
    //获得仿射变换矩阵 该怎么旋转的一个信息记录者
    Mat matrix = getRotationMatrix2D(Point2f(len / 2., len / 2.), -270, 1);
    //旋转
    warpAffine(src, src, matrix, dst_sz);

    imwrite("sdcard/saveBitmap.png", nv21);
    env->ReleaseByteArrayElements(data,cameraData,0);
} ;