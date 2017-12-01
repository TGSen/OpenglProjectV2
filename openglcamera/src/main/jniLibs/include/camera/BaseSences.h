/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 18:05
 * Des    : 
 */
//

#ifndef OPENGLPROJECTV2_BASESENCES_H
#define OPENGLPROJECTV2_BASESENCES_H

#include "sggl.h"
#include "camera/EyesTracker.h"
class BaseSences {
public:
    EyesTracker *eyesTracker;
    bool isNeedEyeTracker;
    BaseSences();
    virtual ~BaseSences();


    //增加一个方法，这个方法在onSurfaceCreated 之前调用
    virtual void onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj);

    virtual void onSurfaceCreated();

    virtual void onSurfaceChanged(float width, float height);

    virtual void onDrawFrame(  void * data,int width,int height);

    virtual void releaseNative(JNIEnv *env);

    virtual void changeFilter(jint r, jint g, jint b, jint a);

    virtual void changeVSFS(const char *vs, const char *fs);

    virtual void changeShape(int cameraShape, int count);

    virtual void changeBgColor(glm::vec4 bgcolor);

    virtual void changeShapeSize(float size);

    virtual void changeShapeDrawCount(int count);

    virtual void changeFileterZoom(float temp);
    virtual void addTextEffect(void *piexl, float width, float height);
    virtual jobject getSurfaceTexture();
    void  initEyeTracker();
};
#endif //OPENGLPROJECTV2_BASESENCES_H
