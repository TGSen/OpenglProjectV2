//
// Created by Administrator on 2017/10/13.
//

#ifndef OPENGLSTUDYV1_MODEL_H
#define OPENGLSTUDYV1_MODEL_H

#include "vertexbuffer.h"
#include "shader.h"
class Model {
public:

    Model();
    VertexBuffer *vertexBuffer;
    SShader *mShader;
    glm::mat4 mModelMatrix;
    void initModel(const char *path);
    void drawModel(glm::mat4 &mViewMatrix, glm::mat4 &mProjectionMatrix,float x, float y,float z);
    void setModelPosition(float x,float y, float z);
    void setAmbientMaterial(float r,float g,float b,float a);
    void setDiffusseMaterial(float r,float g,float b,float a);
    void setSpecularMaterial(float r,float g,float b,float a);
    void setModelTexture(const char *imagePath);
};


#endif //OPENGLSTUDYV1_MODEL_H
