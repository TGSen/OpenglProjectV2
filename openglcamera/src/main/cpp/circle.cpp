//
// Created by Administrator on 2017/10/12.
//

#include "utils.h"
#include "circle.h"


void Circle::initData() {
    float startTime = getTime();
    vertexBuffer = new VertexBuffer;
    vertexBuffer->setSize(400);
    float rudis = 1.0f;

    for (int i = 0; i <vertexBuffer->mVertexCount; i++) {
        vertexBuffer->setPosition(i, rudis * sin(2 * M_PI * i / vertexBuffer->mVertexCount),
                                  rudis * cos(2 * M_PI * i / vertexBuffer->mVertexCount), 0);
        vertexBuffer->setColor(i, 0.7f, 0.7f, 0.7f, 1.0f);
    }

//    glm::rotate();


    mShader = new SShader;
    mShader->init("resource/circle/circle_normal.vs", "resource/circle/circle_normal.fs");
    float timeEnd = getTime();
}

void Circle::draw(glm::mat4 &mViewMatrix,
                  glm::mat4 &mProjectionMatrix) {
    glEnable(GL_DEPTH_TEST);//启动深度测试
    ////这里要注意,必须先设置vbo ,后shader 才能获取attribute，否则出错
    vertexBuffer->bind();
    mShader->bind(glm::value_ptr(mModelMatrix), glm::value_ptr(mViewMatrix),
                  glm::value_ptr(mProjectionMatrix));

    glDrawArrays(GL_TRIANGLE_FAN, 0, vertexBuffer->mVertexCount);
    vertexBuffer->unBind();
}

