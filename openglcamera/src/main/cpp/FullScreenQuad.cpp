/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/15 17:57
 * Des    : 
 */
//

#include "camera/FullScreenQuad.h"

void FullScreenQuad::init() {
    mVertexBuffer = new VertexBuffer;
    mVertexBuffer->setSize(4);
    mVertexBuffer->setTexcoord(0,0.0f,0.0f);
    mVertexBuffer->setTexcoord(1,1.0f,0.0f);
    mVertexBuffer->setTexcoord(2,0.0f,1.0f);
    mVertexBuffer->setTexcoord(3,1.0f,1.0f);
    mShader = new SShader;
}

void FullScreenQuad::draw() {
    //单位矩阵
    float identify[] = {
            1.0f,0.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,0.0f,
            0.0f,0.0f,1.0f,0.0f,
            0.0f,0.0f,0.0f,1.0f,
    };

    mVertexBuffer->setPosition(0,-1.0f,-1.0f,0.0f);
    mVertexBuffer->setPosition(1,1.0f,-1.0f,0.0f);
    mVertexBuffer->setPosition(2,-1.0f,1.0f,0.0f);
    mVertexBuffer->setPosition(3,1.0f,1.0f,0.0f);

    mVertexBuffer->bind();
    mShader->bind(identify,identify,identify);
    glDrawArrays(GL_TRIANGLE_STRIP,0,4);
    mVertexBuffer->unBind();
};

void FullScreenQuad::drawToLeftTop() {
    //单位矩阵
    float identify[] = {
            1.0f,0.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,0.0f,
            0.0f,0.0f,1.0f,0.0f,
            0.0f,0.0f,0.0f,1.0f,
    };

    mVertexBuffer->setPosition(0,-1.0f,-0.0f,-1.0f);
    mVertexBuffer->setPosition(1,0.0f,-1.0f,-1.0f);
    mVertexBuffer->setPosition(2,-1.0f,1.0f,-1.0f);
    mVertexBuffer->setPosition(3,0.0f,1.0f,-1.0f);

    mVertexBuffer->bind();
    mShader->bind(identify,identify,identify);
    glDrawArrays(GL_TRIANGLE_STRIP,0,4);
    mVertexBuffer->unBind();
};

void FullScreenQuad::drawToRightTop() {
    //单位矩阵
    float identify[] = {
            1.0f,0.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,0.0f,
            0.0f,0.0f,1.0f,0.0f,
            0.0f,0.0f,0.0f,1.0f,
    };

    mVertexBuffer->setPosition(0,0.0f,0.0f,-1.0f);
    mVertexBuffer->setPosition(1,1.0f,0.0f,-1.0f);
    mVertexBuffer->setPosition(2,0.0f,1.0f,-1.0f);
    mVertexBuffer->setPosition(3,1.0f,1.0f,-1.0f);

    mVertexBuffer->bind();
    mShader->bind(identify,identify,identify);
    glDrawArrays(GL_TRIANGLE_STRIP,0,4);
    mVertexBuffer->unBind();
};

void FullScreenQuad::drawToLeftBottom() {
    //单位矩阵
    float identify[] = {
            1.0f,0.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,0.0f,
            0.0f,0.0f,1.0f,0.0f,
            0.0f,0.0f,0.0f,1.0f,
    };

    mVertexBuffer->setPosition(0,-1.0f,-1.0f,-1.0f);
    mVertexBuffer->setPosition(1,0.0f,-1.0f,-1.0f);
    mVertexBuffer->setPosition(2,-1.0f,0.0f,-1.0f);
    mVertexBuffer->setPosition(3,0.0f,0.0f,-1.0f);

    mVertexBuffer->bind();
    mShader->bind(identify,identify,identify);
    glDrawArrays(GL_TRIANGLE_STRIP,0,4);
    mVertexBuffer->unBind();
};

void FullScreenQuad::drawToRightBottom() {

    //单位矩阵
    float identify[] = {
            1.0f,0.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,0.0f,
            0.0f,0.0f,1.0f,0.0f,
            0.0f,0.0f,0.0f,1.0f,
    };

    mVertexBuffer->setPosition(0,0.0f,-1.0f,-1.0f);
    mVertexBuffer->setPosition(1,1.0f,-1.0f,-1.0f);
    mVertexBuffer->setPosition(2,0.0f,0.0f,-1.0f);
    mVertexBuffer->setPosition(3,1.0f,0.0f,-1.0f);

    mVertexBuffer->bind();
    mShader->bind(identify,identify,identify);
    glDrawArrays(GL_TRIANGLE_STRIP,0,4);
    mVertexBuffer->unBind();
};