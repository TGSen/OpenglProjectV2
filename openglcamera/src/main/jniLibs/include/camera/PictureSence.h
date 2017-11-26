/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 13:59
 * Des    : 每个场景都可以画 很多东西，目前只画picture
 */
//
#pragma once
#include "sggl.h"
#include "BaseSences.h"
class PictureSence:public BaseSences{
public:
    PictureSence();
    virtual ~PictureSence();
    //增加一个方法，这个方法在onSurfaceCreated 之前调用
    virtual void onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj);

    virtual void onSurfaceCreated();

    virtual void onSurfaceChanged(float width, float height);

    virtual void onDrawFrame(int width,int height,vector<Rect2f> faces);

    virtual void releaseNative(JNIEnv *env);

    virtual void changeFilter(jint r, jint g, jint b, jint a);

    virtual void changeVSFS(const char *vs, const char *fs);

    virtual void changeShape(int cameraShape, int count);

    virtual void changeBgColor(glm::vec4 bgcolor);

    virtual void changeShapeSize(float size);

    virtual void changeShapeDrawCount(int count);

    virtual void changeFileterZoom(float temp);
};

