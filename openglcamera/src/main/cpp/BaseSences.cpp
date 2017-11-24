/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 18:05
 * Des    : 
 */
//

#include "camera/BaseSences.h"
BaseSences::BaseSences(){}
BaseSences:: ~BaseSences(){}
void BaseSences::onSurfaceCreated() {}

void BaseSences::onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj) {};

void BaseSences::onSurfaceChanged(float width, float height) {}

void BaseSences::onDrawFrame(const void * data) {}

void BaseSences::releaseNative(JNIEnv *env) {

}

void  BaseSences::changeFilter(jint r, jint g, jint b, jint a) {}

void  BaseSences::changeVSFS(const char *vs, const char *fs) {}

void  BaseSences::changeShape(int cameraShape, int count) {}

void  BaseSences::changeBgColor(glm::vec4 bgcolor) {}

void  BaseSences::changeShapeSize(float size) {}

void  BaseSences::changeShapeDrawCount(int count) {}

void  BaseSences::changeFileterZoom(float temp) {}

jobject BaseSences::getSurfaceTexture() {}