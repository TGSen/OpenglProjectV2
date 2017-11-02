/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/1 00:13
 * Des    : 11-2 矩阵变换，操作形状的大小
 */
//

#include "CameraShape.h"
CameraShape::CameraShape(){
    LOGE("CameraShape::CameraShape()");
    vertexBuffer = nullptr;
}
CameraShape::~CameraShape(){
    LOGE("CameraShape::~CameraShape()");
    if(vertexBuffer!= nullptr){
        //1.将数组内存操作或循环赋值完成数组清空 。2.数字初始化形式来
        vertexBuffer->mVertexes->color[4] = {0};
        vertexBuffer->mVertexes->position[4] = {0};
        vertexBuffer->mVertexes->texcoord[4] = {0};
        vertexBuffer->mVertexes->normal[4] = {0};
        delete (vertexBuffer->mVertexes);
        delete(vertexBuffer);
    }

    LOGE("CameraShape::~CameraShape() over");


}
//初始化矩阵
void CameraShape::initMVP( float width,float height,glm::vec3 carmeaPos){
    LOGE("CameraShape::initMVP");
}
//初始化顶点
void CameraShape::initShapeData(float x,float y,float z,int count, float shapeSize){
    LOGE("CameraShape::initShapeData");
    //开始时，要画的就等于这个
    drawCount = count;
}

int CameraShape::getDrawCount(){
    return drawCount;

}
void CameraShape::changeDrawCount(int count){
    if(count >vertexBuffer->mVertexCount){
        count =vertexBuffer->mVertexCount;
    }else if(count <3){
        count =3;
    }
    this->drawCount = count;
}

void CameraShape::changeShapeSize(float size){
    LOGE("CameraShape:: changeShapeSize%f",size);
    //每次用初始化为单位矩阵glm::mat4(1.0f)
    mModelMatrix = glm::scale(glm::mat4(1.0f),glm::vec3(size,size,1.0f));
    //还需要旋转过来
    mModelMatrix = glm::rotate(mModelMatrix,90.0f,glm::vec3(0.0f,0.0f,1.0f));
}