//
// Created by Administrator on 2017/10/11.
//

#include <model.h>
#include "ground.h"
#include "scence.h"
#include "ground.h"
#include "particlesystem.h"
#include "camera.h"

glm::mat4 mViewMatrix;
glm::mat4 mProjectionMatrix;
glm::mat4 mModelMatrix;
Ground *ground ;
Model *model,*oxHead;
ParticleSystem *particleSystem;
glm::vec3 carmeaPos(0.0f,0.0f,2.0f);
Camera *mCamera;
Camera* beforInit(JNIEnv *env) {
    mCamera = new Camera;
    mCamera->createSurfaceTextureObject(env);
    return mCamera;
}

void init() {
//    mViewMatrix = glm::lookAt(carmeaPos,glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.0f,1.0f,1.0f));
    model = new Model;
    model->initModel("Res/Sphere.obj");
    model->setModelTexture("Res/earth.bmp");
//    model->setModelPosition(0.0f,0.0f,0.0f);
//    ground = new DrawAnyS;
//    ground->initData();
    particleSystem = new ParticleSystem;
    particleSystem->init(0.0f,0.0f,0.0f,180);
    particleSystem->mModelMatrix =glm::translate(-1.0f,0.0f,1.0f);

//    camera->initData();
    mCamera->init(0.0f,0.0f,0.0f);
//    mCamera->mModelMatrix =glm::translate(-1.0f,0.0f,1.0f);

}

void setViewPortSize(float width, float height) {
    glViewport(0,0,width,height);
    /**
     * 1.视角
     * 2.宽高比
     * 3.最近看到的距离
     * 4.最远看到的距离
     */
//   mProjectionMatrix= glm::perspective(60.0f,width/height,0.1f,1000.0f);
    //其他两个没设置就是单位矩阵
}


//绘制   使用ElementBuffer 来指定顶点顺序来绘制
void draw() {

    float time = getTime();
    glClearColor(0.6f,0.0f,0.6f,1.0f);
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    mCamera->drawModel(mViewMatrix,mProjectionMatrix);
//    ground->draw(mViewMatrix,mProjectionMatrix);
//    model->drawModel(mViewMatrix,mProjectionMatrix,carmeaPos.x,carmeaPos.y,carmeaPos.z);
    //绘制粒子
    particleSystem->draw(mViewMatrix, mProjectionMatrix);
    //粒子运动
    particleSystem->updataFrame(time);
//    oxHead->drawModel(mViewMatrix,mProjectionMatrix,carmeaPos.x,carmeaPos.y,carmeaPos.z);

    //良好习惯，当绘制完毕后，将程序置为0 号程序
    glUseProgram(0);
    LOGE("draw usetime %f",time);
}
