//
// Created by Administrator on 2017/10/16.
//

#include "particlesystem.h"
#include "utils.h"
void ParticleSystem::init(float x,float y,float z,int count){
    mModelMatrix = glm::translate(x,y,z);
    vertexBuffer = new VertexBuffer;
    vertexBuffer->setSize(count);
    for (int i = 0; i < count; ++i) {
      //  vertexBuffer->setPosition(i,2.0f*cosf(float(i)*8.0f*3.14f/180.0f)+0.5f*i,0.0f,2.0f*sinf(float(i)*8.0f*3.14f/180.0f));
        vertexBuffer->setPosition(i,0.01f*i,0.0f,0.02f*i);
        vertexBuffer->setColor(i,0.1f,0.4f,0.6f);
    }

    mShader =new SShader;
    mShader->init("Res/particlesystem.vs","Res/particlesystem.fs");
    mShader->setTexture("U_Texture",createProcedureTexture(128));
}

void ParticleSystem::draw(glm::mat4 viewMatrix, glm::mat4 projectionMatrix) {
    //不要产生深度测试，因为这里的粒子需要透明的，当然有时需要不透明的，那就启用深度测试吧
    glDisable(GL_DEPTH_TEST);
    //http://www.cppblog.com/wc250en007/archive/2012/07/18/184088.html
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA,GL_ONE);
    vertexBuffer->bind();
    mShader->bind(glm::value_ptr(mModelMatrix),glm::value_ptr(viewMatrix),glm::value_ptr(projectionMatrix));
    glDrawArrays(GL_POINTS,0,vertexBuffer->mVertexCount);
    vertexBuffer->unBind();
    glDisable(GL_BLEND);
}

void ParticleSystem::updataFrame(float deltaTime) {
    static float angle = 0.0f;
    angle +=deltaTime *10.0f;
    mModelMatrix =glm::rotate(angle,0.0f,1.0f,0.0f);
    //然后让前90个向上的偏移
    for (int i = 0; i < vertexBuffer->mVertexCount/2; ++i) {
        Vertex &vertex = vertexBuffer->get(i);
//        if (i>90){
//            vertex.normal[1] = 0.05f*i+i*deltaTime;
//        } else{
            vertex.normal[0] = 0.2f*i;

//        }


    }
}
