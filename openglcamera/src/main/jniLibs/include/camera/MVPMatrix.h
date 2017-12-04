/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/12/3 20:31
 * Des    : Mvp 矩阵初始化
 */
//

#ifndef OPENGLPROJECTV2_MVPMATRIX_H
#define OPENGLPROJECTV2_MVPMATRIX_H

#include "sggl.h"

class MVPMatrix {
public:
    MVPMatrix(float rotateAngle);
    ~MVPMatrix();
    void initMVPMatrix(float sreenWidth,float sreenHeight,float imageWidth,float imageHeight,glm::vec3 carmeaPos,float &ratio);
    void initMVPMatrixV2(float sreenWidth, float sreenHeight,glm::vec3 carmeaPos,float& ratio);
    void resetModleMatrix();
public :
    //矩阵相关
    glm::mat4 mModelMatrix;
    glm::mat4 mViewMatrix;
    glm::mat4 mProjectionMatrix;
    float rotateAngle;//旋转角度

};


#endif //OPENGLPROJECTV2_MVPMATRIX_H
