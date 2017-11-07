/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/7 14:04
 * Des    : 
 */
#include "camera/NativeSwapYUV.h"
#include <string>
JNIEXPORT jbyteArray JNICALL Java_sen_com_openglcamera_mediacodec_NativeSwapYUV_nV21ToI420
        (JNIEnv *env, jclass jclazz, jbyteArray jbaty,jint width,jint height,jint byteLength){
    jbyte *nv21_buffer = env->GetByteArrayElements(jbaty, NULL);
    int length = width*height;
    jbyte temp;
    for (int i = length; i < byteLength; i += 2) {
        temp =nv21_buffer[i];
        nv21_buffer[i] = nv21_buffer[i + 1];
        nv21_buffer[i + 1] = temp;
    }
    //使用原来的数组返回
    env->SetByteArrayRegion(jbaty,0,byteLength,nv21_buffer);
    //稀放
    env->ReleaseByteArrayElements(jbaty, nv21_buffer, 0);
    return jbaty;
}

JNIEXPORT jbyteArray JNICALL Java_sen_com_openglcamera_mediacodec_NativeSwapYUV_yV12ToI420
        (JNIEnv *env, jclass jclazz, jbyteArray jbaty,jint width,jint height,jint byteLength){

}