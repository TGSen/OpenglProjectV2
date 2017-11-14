/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 14:10
 * Des    : 
 */
//

#include "camera/BaseGLNative.h"

BaseGLNative *baseGLNative;
void JNICALL Java_sen_com_openglcamera_natives_CameraSGLNative_initAssetManager
        (JNIEnv *env, jclass clzss, jobject assetManager) {
    baseGLNative = new BaseGLNative;
    baseGLNative->aAssetManager = AAssetManager_fromJava(env, assetManager);

};

JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_CameraSGLNative_onSurfaceCreated
        (JNIEnv *env, jclass clzss) {
    init();

};


JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_CameraSGLNative_onSurfaceChanged
        (JNIEnv *env, jclass clzss, jint width, jint height) {
    setViewPortSize((float)width,(float) height);


};

JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_CameraSGLNative_onDrawFrame
        (JNIEnv *env, jclass clzss) {
    if(camera!= nullptr){
        draw();
    }


};