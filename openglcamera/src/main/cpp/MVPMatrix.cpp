/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/12/3 20:31
 * Des    : 
 */
//

#include "camera/MVPMatrix.h"

MVPMatrix::MVPMatrix(float rotateAngle) {
    mViewMatrix = glm::mat4(1.0f);
    mModelMatrix = glm::mat4(1.0f);
    mProjectionMatrix = glm::mat4(1.0f);
    this->rotateAngle = rotateAngle;
}

MVPMatrix::~MVPMatrix() {

}

void
MVPMatrix::initMVPMatrix(float sreenWidth, float sreenHeight, float imageWidth, float imageHeight,
                         glm::vec3 carmeaPos,
                         float &ratio) {
    if (imageWidth > 0 && imageHeight > 0 && sreenWidth > 0 && sreenHeight > 0) {
        float sWH = sreenWidth / sreenHeight;
        ratio = sWH;
        float wHImg = imageWidth / imageHeight;
        if (wHImg > sWH) {
            mProjectionMatrix = glm::ortho(-1.0f, 1.0f, -wHImg / sWH, wHImg / sWH, 0.1f, 100.0f);
        } else {
            mProjectionMatrix = glm::ortho(-sWH / wHImg, sWH / wHImg, -1.0f, 1.0f, 0.1f, 100.0f);
        }
        mViewMatrix = glm::lookAt(carmeaPos, glm::vec3(0.0f, 0.0f, 0.0f),
                                  glm::vec3(0.0f, 1.0f, 0.0f));
        mModelMatrix = glm::rotate(glm::mat4(1.0f), rotateAngle, glm::vec3(0.0f, 0.0f, 1.0f));
    }
}


void
MVPMatrix::initMVPMatrixV2(float sreenWidth, float sreenHeight, glm::vec3 carmeaPos, float &ratio) {
    if (sreenWidth > 0 && sreenHeight > 0) {
        float radio =
                sreenWidth > sreenHeight ? sreenHeight / sreenWidth : sreenWidth / sreenHeight;
        ratio = radio;
        if (sreenWidth > sreenHeight) {
            mProjectionMatrix = glm::ortho(-1.0f, 1.0f, -radio, radio, 0.1f, 100.0f);
        } else {
            mProjectionMatrix = glm::ortho(-radio, radio, -1.0f, 1.0f, 0.1f, 100.0f);
        }
        mViewMatrix = glm::lookAt(carmeaPos, glm::vec3(0.0f, 0.0f, 0.0f),
                                  glm::vec3(0.0f, 1.0f, 0.0f));
        mModelMatrix = glm::rotate(glm::mat4(1.0f), rotateAngle, glm::vec3(0.0f, 0.0f, 1.0f));
    }

}

void MVPMatrix::resetModleMatrix(){
    mModelMatrix =  glm::rotate(glm::mat4(1.0f), rotateAngle, glm::vec3(0.0f, 0.0f, 1.0f));
}

