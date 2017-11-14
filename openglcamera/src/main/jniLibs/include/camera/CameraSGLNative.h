/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class sen_com_openglcamera_natives_CameraSGLNative */

#ifndef _Included_sen_com_openglcamera_natives_CameraSGLNative
#define _Included_sen_com_openglcamera_natives_CameraSGLNative

#include "BaseGLNative.h"

class CameraSGLNative :public BaseGLNative{
public:
    CameraSGLNative();
    virtual ~CameraSGLNative();
    //增加一个方法，这个方法在onSurfaceCreated 之前调用
    virtual void onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj);

    virtual void onSurfaceCreated();

    virtual void onSurfaceChanged(int width, int height);

    virtual void onDrawFrame();

    virtual void releaseNative(JNIEnv *env);
};

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     sen_com_openglcamera_natives_CameraSGLNative
 * Method:    getSurfaceTexture
 * Signature: ()V
 */
JNIEXPORT jobject JNICALL Java_sen_com_openglcamera_natives_CameraSGLNative_getSurfaceTexture
        (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
