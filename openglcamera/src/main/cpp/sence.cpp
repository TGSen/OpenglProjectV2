//
// Created by Administrator on 2017/10/11.
//

#include "scence.h"
#include "particlesystem.h"
#include "camera.h"
#include "circle.h"

glm::mat4 mViewMatrix;
glm::mat4 mProjectionMatrix;
glm::mat4 mModelMatrix;
ParticleSystem *particleSystem;
glm::vec3 carmeaPos(0.0f,0.0f,3.0f);
Camera *mCamera;
Circle *mCircle;
Camera* beforInit(JNIEnv *env) {
    mCamera = new Camera;
    mCamera->createSurfaceTextureObject(env);
    return mCamera;
}

void init() {
    mViewMatrix = glm::lookAt(carmeaPos,glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.0f,1.0f,1.0f));
//    particleSystem = new ParticleSystem;
//    particleSystem->init(0.0f,0.0f,0.0f,180);
//    particleSystem->mModelMatrix =glm::translate(-1.0f,0.0f,1.0f);
    mCircle =new Circle;
    mCamera->init(0.0f,0.0f,0.0f);
    mCircle->initData();


}

void setViewPortSize(float width, float height) {
   // glViewport(0,0,width,height);
    /**
     * 1.视角
     * 2.宽高比
     * 3.最近看到的距离
     * 4.最远看到的距离
     */
   mProjectionMatrix= glm::perspective(60.0f,width/height,0.1f,1000.0f);
    //其他两个没设置就是单位矩阵
}


//绘制   使用ElementBuffer 来指定顶点顺序来绘制
void draw() {

    float time = getTime();
    glClearColor(0.6f,0.0f,0.6f,1.0f);
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
//    mCircle->draw(mViewMatrix,mProjectionMatrix);
    mCamera->drawModel(mViewMatrix,mProjectionMatrix);
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
