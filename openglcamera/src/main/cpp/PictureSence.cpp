/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 13:59
 * Des    : 多次个Fbo 的使用
 */
//
#include "camera/PictureSence.h"
#include "camera/Picture.h"
#include "camera/Model3D.h"
#include "camera/FrameBuffer.h"
#include "camera/FullScreenQuad.h"
glm::vec3 carmeaPoss(0.0f, 0.0f, 6.0f);
glm::vec3 carmeaModelPos(4.0f, 3.0f, 4.0f);
Picture *mPicture;
glm::mat4 mModelViewMatrix;
glm::mat4 mModelProjectionMatrix;
FullScreenQuad *combineSQ;
GLuint sdcardPicTexurid;
PictureSence::PictureSence() {}

PictureSence::~PictureSence() {}

void PictureSence::onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj) {

    sdcardPicTexurid = createTexture2dFromBitmap(env,bitmapObj);
}

void PictureSence::onSurfaceCreated() {


    mModelViewMatrix = glm::lookAt(carmeaModelPos,glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.0f,1.0f,0.0f));



    combineSQ = new FullScreenQuad;
    combineSQ->init();
    combineSQ->mShader->init("resource/effects/fullsrceenfbo.vs",
                             "resource/effects/2dpicture/edgeDetect.fs");
}

void PictureSence::onSurfaceChanged(float width, float height) {
    glViewport(0, 0, width, height);
    // mPicture->initMVP(width, height, carmeaPoss);
    mModelProjectionMatrix = glm::perspective(50.0f, width / height, 0.1f, 100.0f);



}

//绘制场景
void PictureSence::onDrawFrame(int width,int height,vector<Rect2f> faces) {
    float time = getTime();
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


    combineSQ->mShader->setTexture("U_Texture",sdcardPicTexurid);
    //combineSQ->mShader->setTexture("U_Texture1",crateTexture2dFromBmp("resource/picture/testv2.bmp"));
    combineSQ->draw();
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

