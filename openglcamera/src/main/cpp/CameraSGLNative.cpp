
/**
 * Author : 唐家森
 * Version: 1.0
 * On     :
 * Des    :方法名和java 层opengl 对应，还有Camera的一些属性，不应该public 像java 那封装，到时在修改了
 *
 */
#include "camera/CameraSGLNative.h"
#include "camera/sggl.h"
#include "camera/scence.h"
Camera *camera;

CameraSGLNative::CameraSGLNative(){

}
CameraSGLNative::~CameraSGLNative(){

 }
void CameraSGLNative::onBeforeSurfaceCreated(JNIEnv *env,jobject jobj) {
    camera = beforInit(env);
}
void CameraSGLNative::onSurfaceCreated(){
    init();
}

void CameraSGLNative:: onSurfaceChanged(int width, int height){
    setViewPortSize((float)width,(float) height);
}

void CameraSGLNative:: onDrawFrame(){
    if(camera!= nullptr){
        draw();
    }

}

void CameraSGLNative:: releaseNative(JNIEnv *env){
    if(camera != nullptr){
        //javaSurfaceTextureObj 在析构函数好像不能delete ，因为是env Ref
        env->DeleteGlobalRef(camera->javaSurfaceTextureObj);
        delete(camera);
        camera= nullptr;
    }
}


JNIEXPORT jobject JNICALL Java_sen_com_openglcamera_natives_CameraSGLNative_getSurfaceTexture
        (JNIEnv *, jclass){
    return camera->getSurfaceTextureObject();
}

