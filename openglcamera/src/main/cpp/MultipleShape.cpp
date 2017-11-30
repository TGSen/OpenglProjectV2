/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/1 00:09
 * Des    : 这个是多边行的，假如无数的边，那么就成为圆形
 */
#include "camera/MultipleShape.h"
MultipleShape::MultipleShape(float rotateAngle,int preInt){
    LOGE("MultipleShapeV::MultipleShape()");
    this->rotateAngle = rotateAngle;
    this->preInt = preInt;
}
MultipleShape::~MultipleShape(){
    LOGE("MultipleShapeV::~MultipleShape()");
}
//初始化矩阵
void MultipleShape::initMVP( float width,float height,glm::vec3 carmeaPos){
    LOGE("MultipleShapeV::initMVP");
    mViewMatrix = glm::lookAt(carmeaPos,glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.0f,1.0f,0.0f));
    //mModelMatrix = glm::mat4(1.0f);
    mModelMatrix = glm::rotate(glm::mat4(1.0f),rotateAngle,glm::vec3(0.0f,0.0f,1.0f));
    float ratio= width > height ? (float)width / height : (float)height / width;
//    mProjectionMatrix= glm::perspective(60.0f,ratio,0.1f,100.0f);
    if (width > height) {
        // 横屏
        mProjectionMatrix =    glm::ortho(  -ratio, ratio, -1.0f, 1.0f,  1.0f, 10.0f);

    } else {
        mProjectionMatrix =  glm::ortho(  -1.0f, 1.0f, -ratio, ratio, 1.0f, 10.0f);
    }
}
//初始化顶点 ,假如多边形为200 为圆形
void MultipleShape::initShapeData(float x,float y,float z,int count, float size){
    LOGE("MultipleShapeV::initShapeData %d -- %f",count,shapeSize);
    mModelMatrix = glm::translate(x,y,z);
    if(vertexBuffer == nullptr){
        vertexBuffer = new VertexBuffer;
    }
    //默认产生200个
    shapeSize = size;
    vertexBuffer->setSize(count);
    //实质要画的，这里我偷个懒，如果要改变形状，只是改变要画的顶点数，要不又要重新生成顶点(这个方案已放弃，先保留想法)
    changeDrawCount(count);
    float sinValue;
    float cosValue;
    double angle ;
    int i;
    for ( i = 0; i <vertexBuffer->mVertexCount; i++) {
        angle= 2 * M_PI * i / vertexBuffer->mVertexCount;
        sinValue= sin(angle);
        cosValue = cos(angle);
        vertexBuffer->setPosition(i,shapeSize * cosValue,shapeSize *sinValue, 0);
        vertexBuffer->setColor(i, 0.7f, 0.3f, 0.1f, 1.0f);
        //vertexBuffer->setTexcoord(i,(-cosValue + 1.0f)*0.5f,(sinValue + 1.0f)*0.5f);
        vertexBuffer->setTexcoord(i,(preInt* cosValue + 1.0f)*0.5f,(sinValue + 1.0f)*0.5f);
        //vertexBuffer->setTexcoord(i,abs(cosValue)*0.5f,abs(sinValue )*0.5f);
    }
}