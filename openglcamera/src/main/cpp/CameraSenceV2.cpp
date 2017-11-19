/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 13:59
 * Des    : 
 */
//
#include <camera/camera.h>
#include "camera/CameraSence.h"
#include "camera/Picture.h"
#include "camera/Model3D.h"

glm::vec3 carmeaPoss(0.0f, 0.0f, 6.0f);
Camera *mCamera;

CameraSence::CameraSence() {}

CameraSence::~CameraSence() {}

void CameraSence::onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj) {
    mCamera = new Camera;
    mCamera->createSurfaceTextureObject(env);

}

void CameraSence::onSurfaceCreated() {
    mCamera->initVertex(0.0f,0.0f,0.0f,4);
}

void CameraSence::onSurfaceChanged(float width, float height) {
    glViewport(0,0,width,height);
    mCamera->initMVP(width,height,carmeaPoss);
}

//绘制场景
void CameraSence::onDrawFrame() {
    if(mCamera== nullptr){
        LOGE("camera is nullptr");
        return;
    }
    float time = getTime();
    glClearColor(mCamera->mBgColor.r,mCamera->mBgColor.g,mCamera->mBgColor.b,mCamera->mBgColor.a);
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    //好像这个发方法用来坑锯齿，但是里面的参数有待验证
    glHint(GL_GENERATE_MIPMAP_HINT, GL_LINEAR_MIPMAP_NEAREST);
    mCamera->draw();
    //绘制粒子
    // particleSystem->draw(mViewMatrix, mProjectionMatrix);
    //粒子运动
    //  particleSystem->updataFrame(time);
//    oxHead->drawModel(mViewMatrix,mProjectionMatrix,carmeaPos.x,carmeaPos.y,carmeaPos.z);
    //绘制圆形

    //良好习惯，当绘制完毕后，将程序置为0 号程序
    glUseProgram(0);
//    LOGE("draw usetime %f",time);
}

//增加一个方法，这个方法在onSurfaceCreated 之前调用

void CameraSence::releaseNative(JNIEnv *env) {
}

void CameraSence::changeFilter(jint r, jint g, jint b, jint a) {
    if (mCamera)
        mCamera->changeFilter(r, g, b, a);
}

void CameraSence::changeVSFS(const char *vs, const char *fs) {
    if (mCamera)
        mCamera->changeVSFS(vs, fs);

}

void CameraSence::changeShape(int cameraShape, int count) {
    if (mCamera) {
        mCamera->changeShape(cameraShape, count);
    }

}

void CameraSence::changeBgColor(glm::vec4 bgcolor) {
    if (mCamera) {
        mCamera->changeBgColor(bgcolor);
    }
}

void CameraSence::changeShapeSize(float size) {
    if (mCamera) {
        mCamera->changeShapeSize(size);
    }
    //
}

void CameraSence::changeShapeDrawCount(int count) {
    if (mCamera) {
        mCamera->changeShapeDrawCount(count);
    }
}

void CameraSence::changeFileterZoom(float temp) {
    if (mCamera) {
        mCamera->changeFileterZoom(temp);
    }
}

 jobject CameraSence::getSurfaceTexture(){
     if (mCamera) {
         return mCamera->javaSurfaceTextureObj;
     }
     return nullptr;
}

