/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/1 00:09
 * Des    : 一直没留意，没为c++ 新建文件时做个这样的注释，现在开始加吧，方便写注释，~怕忘
 */
//

#include "camera/NormalShape.h"
#include "camera/MultipleShape.h"
NormalShape::NormalShape(float rotateAngle){
    this->rotateAngle = rotateAngle;
    LOGE("NormalShape::NormalShape()");
}
NormalShape::~NormalShape(){
    LOGE("NormalShape::~NormalShape()");
}
//初始化矩阵
void NormalShape::initMVP( float width,float height,glm::vec3 carmeaPos){
    LOGE("NormalShape::initMVP");
    mProjectionMatrix =glm::mat4(1.0f);
    mViewMatrix =glm::mat4(1.0f);
    mModelMatrix = glm::rotate(glm::mat4(1.0f),rotateAngle,glm::vec3(0.0f,0.0f,1.0f));
   // glm::ortho(-1, radio, -1, 1, 4, 10);
//    glm::frustum(-radio, radio, -1.0f, 1.0f, 4.0f, 10.0f);
    // 根据屏幕方向设置投影矩阵
//    float ratio= width > height ? (float)width / height : (float)height / width;
//    if (width > height) {
//        // 横屏
//        mProjectionMatrix= glm::ortho(  -ratio, ratio, -1.0f, 1.0f, 0.0f, 5.0f);
//    } else {
//        mProjectionMatrix= glm::ortho(  -1.0f, 1.0f, -ratio, ratio, 0.0f, 5.0f);
//    }
}
//初始化顶点 ,normal 是4个顶点，
void NormalShape::initShapeData(float x,float y,float z,int count, float shapeSize){
    LOGE("NormalShape::initShapeData");
    mModelMatrix = glm::translate(x,y,z);
    if(vertexBuffer == nullptr){
        vertexBuffer = new VertexBuffer;
    }
    vertexBuffer->setSize(count);
    changeDrawCount(count);
    vertexBuffer->setColor(0,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(1,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(2,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(3,0.3f,0.3f,0.3f,1.0f);
    //在android 屏幕的坐标系，左上角为0,0 ，跟opengl 左下角为0，0,所以得倒过来，才能对应
    //要不会出现图像倒转
    vertexBuffer->setTexcoord(0,0.0f,0.0f);
    vertexBuffer->setTexcoord(1,1.0f, 0.0f);
    vertexBuffer->setTexcoord(2,0.0f, 1.0f);
    vertexBuffer->setTexcoord(3,1.0f, 1.0f);

    vertexBuffer->setPosition(0,-1.0f, -1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(1,1.0f, -1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(2,-1.0f, 1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(3,1.0f, 1.0f, 0.0f,1.0f);
}