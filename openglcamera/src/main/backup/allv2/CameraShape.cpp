/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/1 00:13
 * Des    : 11-2 矩阵变换，操作形状的大小
 */
//

#include "camera/CameraShape.h"
CameraShape::CameraShape(){
    LOGE("CameraShape::CameraShape()");
    vertexBuffer = nullptr;
    mMvpMatrix = nullptr;
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

void
CameraShape::initMVPMatirxV2(float width, float height, glm::vec3 carmeaPos, float rotateAngle,float ratio) {}

void CameraShape::initMVPMatirx(float width, float height, float reqWidth, float reqHeight,
                                glm::vec3 carmeaPos, float rotateAngle,float ratio) {}







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
