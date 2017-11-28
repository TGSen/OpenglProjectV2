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

glm::vec3 carmeaPosss(0.0f, 0.0f, 7.0f);
Picture *mPicture;
GLuint sdcardPicTexurid;

FrameBuffer *mFrameBuffer;
FullScreenQuad *fullScreenQuad;
PictureSence::PictureSence() {}

PictureSence::~PictureSence() {}

void PictureSence::onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj) {

    sdcardPicTexurid = createTexture2dFromBitmap(env, mPicture->mResWidth, mPicture->mResHeight,
                                                 bitmapObj);
}

void PictureSence::onSurfaceCreated() {
    mPicture = new Picture;
    mPicture->initVertex(0.0f, 0.0f, 0.0f, 4);

    fullScreenQuad = new FullScreenQuad;
    fullScreenQuad->init();
    fullScreenQuad->mShader->init("resource/camera/picture_normal.vs",
                                  "resource/camera/picture_normal.fs");

}

void PictureSence::onSurfaceChanged(float width, float height) {

    glViewport(0, 0, width, height);
    mPicture->initMVP(width, height, mPicture->mResWidth, mPicture->mResHeight, carmeaPosss);
    mFrameBuffer = new FrameBuffer;
    mFrameBuffer->attachColorBuffer("color", GL_COLOR_ATTACHMENT0,(int)width,(int)height);
    mFrameBuffer->attachDepthBuffer("depth", (int)width,(int)height);
    mFrameBuffer->finish();
}

//绘制场景
void PictureSence::onDrawFrame(void *data, int width, int height) {
    float time = getTime();
    glClearColor(0.8f, 0.0f, 0.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    mFrameBuffer->bind();
    mPicture->mShader->setTexture("U_Texture", sdcardPicTexurid);
    fullScreenQuad->draw();
  //  mPicture->draw();
    mFrameBuffer->unBind();
    mPicture->setTexture("U_Texture", mFrameBuffer->getBufferByName("color"));
    mPicture->draw();
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

