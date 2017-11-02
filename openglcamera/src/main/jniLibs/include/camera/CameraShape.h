/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/1 00:13
 * Des    : 虚析构函数，delete父类指针指向子类时，父类的析构执行，子类也会执行，这样不会造成内存溢出
 */
//
#pragma once

#include "sggl.h"
#include "vertexbuffer.h"
class CameraShape {
private:
    int drawCount;
public:
     CameraShape();
    virtual ~CameraShape();
    VertexBuffer *vertexBuffer;
    //由于opengl画时可以指定，从哪些点开始画，那么我一次性就先画个圆的数量，但修改多边形时，不必要删除了，只要指定数量就行

    //矩阵相关
    glm::mat4 mModelMatrix;
    glm::mat4 mViewMatrix;
    glm::mat4 mProjectionMatrix;
    //初始化矩阵 ,加上virtual 成为虚函数，这样子类，可以调用各自的方法
    virtual void initMVP( float width,float height,glm::vec3 carmeaPos);
    //初始化顶点
    virtual void initShapeData(float x,float y,float z,int count, float shapeSize);
    //调用父类方法，不需virtual ，需要在加上
     int getDrawCount();
    void changeDrawCount(int count);

    void changeShapeSize(float size);
};


