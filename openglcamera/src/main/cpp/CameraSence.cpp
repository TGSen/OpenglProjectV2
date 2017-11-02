//
// Created by Administrator on 2017/10/11.
//

#include "camera/scence.h"
#include "camera/particlesystem.h"
#include "camera/camera.h"
#include "camera/circle.h"

ParticleSystem *particleSystem;
glm::vec3 carmeaPos(0.0f,0.0f,2.6f);
Camera *mCamera;
//这里需要在init 前产生一个SurfaceTexture给java 绑定接口，请对应查看
//CamerRenderer.java
Camera* beforInit(JNIEnv *env) {
    mCamera = new Camera;
    mCamera->createSurfaceTextureObject(env);
    return mCamera;
}

void init() {

//    particleSystem = new ParticleSystem;
//    particleSystem->init(0.0f,0.0f,0.0f,180);
//    particleSystem->mModelMatrix =glm::translate(-1.0f,0.0f,1.0f);
    mCamera->initVertex(0.0f,0.0f,0.0f,4);


}

void setViewPortSize(float width, float height) {
    glViewport(0,0,width,height);
    mCamera->initMVP(width,height,carmeaPos);
}


//绘制   使用ElementBuffer 来指定顶点顺序来绘制
void draw() {

    float time = getTime();
    glClearColor(mCamera->mBgColor.r,mCamera->mBgColor.g,mCamera->mBgColor.b,mCamera->mBgColor.a);
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
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
