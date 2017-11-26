/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 18:05
 * Des    : 
 */
//

#include "camera/BaseSences.h"
#include "camera/EyesTracker.h"
BaseSences::BaseSences(){}
BaseSences:: ~BaseSences(){}
void BaseSences::onSurfaceCreated() {}

void BaseSences::onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj) {};

void BaseSences::onSurfaceChanged(float width, float height) {}

void BaseSences::onDrawFrame( void * data,int width,int height) {}

void BaseSences::releaseNative(JNIEnv *env) {
    if(eyesTracker){
        eyesTracker->stopTracking();
        delete eyesTracker;
        eyesTracker = nullptr;
    }

}

void  BaseSences::changeFilter(jint r, jint g, jint b, jint a) {}

void  BaseSences::changeVSFS(const char *vs, const char *fs) {}

void  BaseSences::changeShape(int cameraShape, int count) {}

void  BaseSences::changeBgColor(glm::vec4 bgcolor) {}

void  BaseSences::changeShapeSize(float size) {}

void  BaseSences::changeShapeDrawCount(int count) {}

void  BaseSences::changeFileterZoom(float temp) {}

jobject BaseSences::getSurfaceTexture() {}

void  BaseSences::initEyeTracker(){
    //加载人脸检测,先检查文件是否存在，并且是否可读权限
    float time = getTime();
    if(access("/sdcard/seeta_fa_v1.1.bin",F_OK)==-1){
        writeFileToSdcard("facemodel/seeta_fa_v1.1.bin","/sdcard/seeta_fa_v1.1.bin");
    }
    if(access("/sdcard/lbpcascade_frontalface.xml",F_OK)==-1){
        writeFileToSdcard("facemodel/lbpcascade_frontalface.xml","/sdcard/lbpcascade_frontalface.xml");
    }
    float endtime = getTime()-time;
    LOGE("writeFileToSdcard usetime %f",endtime);
    //开始追踪
    if(access("/sdcard/seeta_fa_v1.1.bin",R_OK)!=-1 &&access("/sdcard/lbpcascade_frontalface.xml",R_OK)!=-1){
        //文件有可读权限
        eyesTracker = new EyesTracker("/sdcard/lbpcascade_frontalface.xml","/sdcard/seeta_fa_v1.1.bin");
        eyesTracker->startTracking();
    }
}