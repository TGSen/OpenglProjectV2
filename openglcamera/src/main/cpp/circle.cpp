//
// Created by Administrator on 2017/10/12.
//

#include "utils.h"
#include "circle.h"


void Circle::initVertex(float x, float y, float z, int count) {
    float startTime = getTime();
    vertexBuffer = new VertexBuffer;
    vertexBuffer->setSize(count);
    float rudis = 1.0f;
    float sinValue;
    float cosValue;
    double angle ;
    int i;
    for ( i = 0; i <vertexBuffer->mVertexCount; i++) {
        angle= 2 * M_PI * i / vertexBuffer->mVertexCount;
        sinValue= sin(angle);
        cosValue = cos(angle);
        vertexBuffer->setPosition(i, rudis * cosValue, rudis *sinValue, 0);
        vertexBuffer->setColor(i, 0.7f, 0.3f, 0.1f, 1.0f);
        vertexBuffer->setTexcoord(i,(-cosValue + 1.0f)*0.5f,(sinValue + 1.0f)*0.5f);
    }
    mShader = new SShader;
    mShader->init("resource/camera/camera_normal.vs", "resource/camera/camera_normal.fs");
    float timeEnd = getTime();
}
void Circle::initMVP(float width, float height, glm::vec3 carmeaPos) {
    mViewMatrix = glm::lookAt(carmeaPos,glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.0f,1.0f,1.0f));
    mProjectionMatrix= glm::perspective(60.0f,width/height,0.1f,1000.0f);
}

void Circle::draw() {
    glEnable(GL_DEPTH_TEST);//启动深度测试
    ////这里要注意,必须先设置vbo ,后shader 才能获取attribute，否则出错
    vertexBuffer->bind();
    mShader->bind(glm::value_ptr(mModelMatrix), glm::value_ptr(mViewMatrix),
                  glm::value_ptr(mProjectionMatrix));

    glDrawArrays(GL_TRIANGLE_FAN, 0, vertexBuffer->mVertexCount);
    vertexBuffer->unBind();
}

