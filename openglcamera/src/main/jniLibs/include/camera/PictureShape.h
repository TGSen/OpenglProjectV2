/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/1 00:09
 * Des    : 
 */
//
#pragma once

#include "CameraShape.h"
class PictureShape:public CameraShape{
public:
    PictureShape();

    virtual ~PictureShape();
    //初始化矩阵 ,子类可加可不加，不加编译器自动加，加了可读性高
    virtual void initMVP( float width,float height,glm::vec3 carmeaPos);
    virtual void initMVPV2(int mOffset,
                           float left, float right, float bottom, float top,
                           float near, float far,glm::vec3 carmeaPos);
    //初始化顶点
    virtual void initShapeData(float x,float y,float z,int count,float shapeSize);
};


