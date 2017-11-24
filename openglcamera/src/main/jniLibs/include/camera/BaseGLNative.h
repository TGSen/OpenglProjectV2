/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 14:10
 * Des    : 
 */
//


#include "sggl.h"


#include <jni.h>
/* Header for class sen_com_openglcamera_natives_BaseGLNative */

#ifndef _Included_sen_com_openglcamera_natives_BaseGLNative
#define _Included_sen_com_openglcamera_natives_BaseGLNative
#ifdef __cplusplus

class BaseGLNative {
public:

    BaseGLNative();

    virtual ~BaseGLNative();

    AAssetManager *aAssetManager;

    //增加一个方法，这个方法在onSurfaceCreated 之前调用
    virtual void onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj);

    virtual void onSurfaceCreated();

    virtual void onSurfaceChanged(int width, int height);

    virtual void onDrawFrame();

    virtual void releaseNative(JNIEnv *env);

    virtual void changeFilter(jint r, jint g, jint b, jint a);

    virtual void changeVSFS(const char *vs, const char *fs);

    virtual void changeShape(int cameraShape, int count);

    virtual void changeBgColor(glm::vec4 bgcolor);

    virtual void changeShapeSize(float size);

    virtual void changeShapeDrawCount(int count);

    virtual void changeFileterZoom(float temp);
};

extern "C" {
#endif
/*
 * Class:     sen_com_openglcamera_natives_BaseGLNative
 * Method:    initAssetManager
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_initAssetManager
        (JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onBeforeSurfaceCreated(JNIEnv *, jclass, jobject);
/*
 * Class:     sen_com_openglcamera_natives_BaseGLNative
 * Method:    onSurfaceCreated
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_onSurfaceCreated
        (JNIEnv *, jclass);

/*
 * Class:     sen_com_openglcamera_natives_BaseGLNative
 * Method:    onSurfaceChanged
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_onSurfaceChanged
        (JNIEnv *, jclass, jint, jint);

/*
 * Class:     sen_com_openglcamera_natives_BaseGLNative
 * Method:    onDrawFrame
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_onDrawFrame
        (JNIEnv *, jclass,jbyteArray ,jint ,jint );


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_releaseNative(JNIEnv *, jclass);


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeFileter(JNIEnv *, jclass, jint, jint, jint,
                                                               jint, jint);


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeVSFS(JNIEnv *, jclass,
                                                            jstring, jstring);


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeShape(JNIEnv *, jclass, jint, jint);


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeBgColor(JNIEnv *, jclass,
                                                               jfloat, jfloat, jfloat,
                                                               jfloat);

JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeShapeSize(JNIEnv *, jclass, jint, jint);


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeShapeCount(JNIEnv *, jclass, jint);


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeFileterZoom(JNIEnv *, jclass,
                                                                   jint i, jint);

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
