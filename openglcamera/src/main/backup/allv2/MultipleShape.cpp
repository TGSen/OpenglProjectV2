/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/1 00:09
 * Des    : 这个是多边行的，假如无数的边，那么就成为圆形
 */
#include "camera/MultipleShape.h"
MultipleShape::MultipleShape(){
    LOGE("MultipleShapeV::MultipleShape()");
}
MultipleShape::~MultipleShape(){
    LOGE("MultipleShapeV::~MultipleShape()");
}

//初始化矩阵
 void MultipleShape::initMVPMatirxV2( float width,float height,glm::vec3 carmeaPos,float rotateAngle,float ratio){
    mMvpMatrix = new MVPMatrix(rotateAngle);
    mMvpMatrix->initMVPMatrixV2(width,height,carmeaPos,ratio);

}
 void MultipleShape::initMVPMatirx( float width,float height,float reqWidth,float reqHeight,glm::vec3 carmeaPos,float rotateAngle,float ratio){
    mMvpMatrix = new MVPMatrix(rotateAngle);
    mMvpMatrix->initMVPMatrix(width,height,reqWidth,reqHeight,carmeaPos,ratio);
}


//初始化顶点 ,假如多边形为200 为圆形
void MultipleShape::initShapeData(float x,float y,float z,int count, float size){
    LOGE("MultipleShapeV::initShapeData %d -- %f",count,size);
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
        vertexBuffer->setTexcoord(i,( cosValue + 1.0f)*0.5f,(sinValue + 1.0f)*0.5f);
        //vertexBuffer->setTexcoord(i,abs(cosValue)*0.5f,abs(sinValue )*0.5f);
    }
}