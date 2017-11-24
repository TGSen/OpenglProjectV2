/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 13:59
 * Des    : 
 */
//
#include "camera/PictureSence.h"
#include "camera/Picture.h"
#include "camera/Model3D.h"

//glm::vec3 carmeaPoss(0.0f, 0.0f, 6.0f);
glm::vec3 carmeaModelPos(4.0f, 3.0f, 4.0f);
Picture *mPicture;
Model *model;
glm::mat4 mModelViewMatrix;
glm::mat4 mModelProjectionMatrix;

PictureSence::PictureSence() {}

PictureSence::~PictureSence() {}

void PictureSence::onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj) {
   // mPicture = new Picture;
    model = new Model;

   // mPicture->textureId = createTexture2dFromBitmap(env, bitmapObj);

}

void PictureSence::onSurfaceCreated() {
  //  mPicture->initVertex(0.0f, 0.0f, 0.0f, 4);
    model->initModel("resource/model/Cube.obj");
    model->setModelPosition(0.0f,0.0f,0.0f);
    model->mShader->init("resource/model/shader/rgbcube.vs","resource/model/shader/rgbcube.fs");
    mModelViewMatrix = glm::lookAt(carmeaModelPos,glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.0f,1.0f,0.0f));
}

void PictureSence::onSurfaceChanged(float width, float height) {
//    glViewport(0, 0, width, height);
    //mPicture->initMVP(width, height, carmeaPoss);
    mModelProjectionMatrix = glm::perspective(50.0f,width/height,0.1f,100.0f);
}

//绘制场景
void PictureSence::onDrawFrame(unsigned char* data) {
    float time = getTime();
//    glClearColor(mPicture->mBgColor.r, mPicture->mBgColor.g, mPicture->mBgColor.b,
//                 mPicture->mBgColor.a);
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
   // mPicture->draw();
    LOGE("sen ------");
    model->drawModel(mModelViewMatrix,mModelProjectionMatrix,carmeaModelPos.x,carmeaModelPos.y,carmeaModelPos.z);
    LOGE("sen ------1");
    //良好习惯，当绘制完毕后，将程序置为0 号程序
    glUseProgram(0);
}

//增加一个方法，这个方法在onSurfaceCreated 之前调用

void PictureSence::releaseNative(JNIEnv *env) {
}

void PictureSence::changeFilter(jint r, jint g, jint b, jint a) {
    if (mPicture)
        mPicture->changeFilter(r, g, b, a);
}

void PictureSence::changeVSFS(const char *vs, const char *fs) {
    if (mPicture)
        mPicture->changeVSFS(vs, fs);

}

void PictureSence::changeShape(int cameraShape, int count) {
    if (mPicture) {
        mPicture->changeShape(cameraShape, count);
    }

}

void PictureSence::changeBgColor(glm::vec4 bgcolor) {
    if (mPicture) {
        mPicture->changeBgColor(bgcolor);
    }
}

void PictureSence::changeShapeSize(float size) {
    if (mPicture) {
        mPicture->changeShapeSize(size);
    }
    //
}

void PictureSence::changeShapeDrawCount(int count) {
    if (mPicture) {
        mPicture->changeShapeDrawCount(count);
    }
}

void PictureSence::changeFileterZoom(float temp) {
    if (mPicture) {
        mPicture->changeFileterZoom(temp);
    }
}

