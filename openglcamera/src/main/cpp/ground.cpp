//
// Created by Administrator on 2017/10/12.
//

#include <model.h>
#include "utils.h"
#include "ground.h"
/*void Ground::init() {
    Vertex vertex[1600]; //表示地形有1600个顶点
    for (int z= 0; z< 20; ++z) {
        float zStart = 100.f- z*10.0f;
        for (int x = 0; x < 20; ++x) {
            int offset =(x+z*20)*4;
            float xStart = x*10.0f-100f;
            vertex[offset].position[0] =xStart;
            vertex[offset].position[1] =-1.0f;
            vertex[offset].position[2] =zStart;
            vertex[offset].position[3] =1.0f;

            vertex[offset+1].position[0] =xStart+10.0f;
            vertex[offset+1].position[1] =-1.0f;
            vertex[offset+1].position[2] =zStart;
            vertex[offset+1].position[3] =1.0f;

            vertex[offset+2].position[0] =xStart;
            vertex[offset+2].position[1] =-1.0f;
            vertex[offset+2].position[2] =zStart-10.0f;
            vertex[offset+2].position[3] =1.0f;


            vertex[offset+2].position[0] =xStart+10.0f;
            vertex[offset+2].position[1] =-1.0f;
            vertex[offset+2].position[2] =zStart-10.0f;
            vertex[offset+2].position[3] =1.0f;

        }
    }
    vbo = createBufferObj(GL_ARRAY_BUFFER, sizeof(Vertex)* sizeof(vertex),GL_STATIC_DRAW,vertex);
}*/

/**

void Ground::init() {
    float data[] = {
            //每一行前4位为position ,后四位为color
            -0.2f, -0.2f, 0.0f, 1.0f, 0.8f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.2f, -0.2f, 0.0f, 1.0f, 0.0f, 0.8f, 0.0f, 1.0f, 1.0f, 0.0f,
            0.0f, 0.2f, 0.0f, 1.0f, 0.0f, 0.0f, 0.8f, 1.0f, 0.5f, 1.0f
    };
    vertexBuffer = new VertexBuffer;
    vertexBuffer->setSize(1600);
    vertexBuffer->setPosition(0, -0.2f, -0.2f, -0.6f);
    vertexBuffer->setPosition(1, 0.2f, -0.2f, -0.6f);
    vertexBuffer->setPosition(2, 0.0f, 0.2f, -0.6f);
    vertexBuffer->setNarmal(0, 0.0f, 1.0f, 0.0f);
    vertexBuffer->setNarmal(1, 0.0f, 1.0f, 0.0f);
    vertexBuffer->setNarmal(2 , 0.0f, 1.0f, 0.0f);

    vertexBuffer->setColor(0, 0.7f, 0.7f, 0.7f, 1.0f);
    vertexBuffer->setColor(1 , 0.7f, 0.7f, 0.7f, 1.0f);
    vertexBuffer->setColor(2 , 0.7f, 0.7f, 0.7f, 1.0f);

    vbo = createBufferObj(GL_ARRAY_BUFFER, sizeof(Vertex)*vertexBuffer->mVertexCount ,GL_STATIC_DRAW,vertexBuffer->mVertexes);
    mShader = new SShader;
    mShader->init("Res/ground.vs", "Res/ground.fs");
}
*/


void Ground::initData() {
    float startTime = getTime();
    vertexBuffer = new VertexBuffer;
    vertexBuffer->setSize(1600);
    for (int z= 0; z< 10; ++z) {
        float zStart = 100.0f- z*10.0f;
        for (int x = 0; x < 10; ++x) {
            int offset =(x+z*20)*4;
            float xStart = x * 10.0f - 100.0f;
            vertexBuffer->setPosition(offset,xStart,-2.0f,zStart);
            vertexBuffer->setPosition(offset+1,xStart+10.0f,-2.0f,zStart);
            vertexBuffer->setPosition(offset+2,xStart,-2.0f,zStart-10.0f);
            vertexBuffer->setPosition(offset+3,xStart+10.0f,-2.0f,zStart-10.0f);
            vertexBuffer->setNarmal(offset,0.0f,1.0f,0.0f);
            vertexBuffer->setNarmal(offset+1,0.0f,1.0f,0.0f);
            vertexBuffer->setNarmal(offset+2,0.0f,1.0f,0.0f);
            vertexBuffer->setNarmal(offset+3,0.0f,1.0f,0.0f);

            vertexBuffer->setTexcoord(offset,0.0f,1.0f);
            vertexBuffer->setTexcoord(offset+1,0.0f,1.0f);
            vertexBuffer->setTexcoord(offset+2,0.0f,1.0f);
            vertexBuffer->setTexcoord(offset+3,0.0f,1.0f);

            if((z%2)^(x%2)){
                vertexBuffer->setColor(offset,0.3f,0.3f,0.3f,1.0f);
                vertexBuffer->setColor(offset+1,0.3f,0.3f,0.3f,1.0f);
                vertexBuffer->setColor(offset+2,0.3f,0.3f,0.3f,1.0f);
                vertexBuffer->setColor(offset+3,0.3f,0.3f,0.3f,1.0f);
            }else{
                vertexBuffer->setColor(offset,0.7f,0.7f,0.7f,1.0f);
                vertexBuffer->setColor(offset+1,0.7f,0.7f,0.7f,1.0f);
                vertexBuffer->setColor(offset+2,0.7f,0.7f,0.7f,1.0f);
                vertexBuffer->setColor(offset+3,0.7f,0.7f,0.7f,1.0f);
            }
        }
    }
    //这个放在vertexbuffer 初始化
  //  vbo = createBufferObj(GL_ARRAY_BUFFER, sizeof(Vertex)*vertexBuffer->mVertexCount ,GL_STATIC_DRAW,vertexBuffer->mVertexes);
    mShader = new SShader;
    mShader->init("Res/ground.vs", "Res/ground.fs");
//    mShader->setTexture("U_Texture","Res/test.bmp");
    mShader->setUiformVec4("U_LightPos",0.0f,0.0f,1.0f,1.0f);
    mShader->setUiformVec4("U_LightAmbient",1.0f,1.0f,1.0f,1.0f);
    mShader->setUiformVec4("U_LightDiffuse",.0f,1.0f,1.0f,1.0f);
    mShader->setUiformVec4("U_LightOpt",32.0f,0.0f,0.0f,1.0f);
    mShader->setUiformVec4("U_AmbientMaterial",0.1f,0.1f,0.1f,1.0f);
    mShader->setUiformVec4("U_DiffuseMaterial",0.6f,0.6f,0.6f,1.0f);
    float timeEnd = getTime();
    LOGE("Ground::init %f",timeEnd-startTime);
}

void Ground::draw(glm::mat4 &mViewMatrix,
                  glm::mat4 &mProjectionMatrix) {
    glEnable(GL_DEPTH_TEST);//启动深度测试
    //vbo 放在vertex
//    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    ////这里要注意,必须先设置vbo ,后shader 才能获取attribute，否则出错
    vertexBuffer->bind();
    mShader->bind(glm::value_ptr(mModelMatrix), glm::value_ptr(mViewMatrix),
                  glm::value_ptr(mProjectionMatrix));

    //绘制400个正方形格子
    for (int i = 0; i < 20; i++) {
        glDrawArrays(GL_TRIANGLE_STRIP, i * 4, 4);
    }
    vertexBuffer->unBind();
    LOGE("Ground::draw");
}

