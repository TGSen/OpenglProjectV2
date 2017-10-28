//
// Created by Administrator on 2017/10/12.
//
#include "shader.h"

void SShader::init(const char *vsPath, const char *fsPath) {
    int fileSize = 0;
    unsigned char *shaderCode = loadFile(vsPath, fileSize);
    LOGE("shaderCode");
    GLuint vsShader = complieShader(GL_VERTEX_SHADER, (const char *) shaderCode);
    delete shaderCode;
    if (vsShader == 0){
        LOGE("vsShader == 0");
        return;
    }


    shaderCode = loadFile(fsPath, fileSize);
    GLuint fsShader = complieShader(GL_FRAGMENT_SHADER, (const char *) shaderCode);
    delete shaderCode;
    if (fsShader == 0)
        return;
    mProgram = createProgram(vsShader,fsShader);
    glDeleteShader(vsShader);
    glDeleteShader(fsShader);

    if (mProgram==0){
        LOGE("mProgram ==0");
        return;
    }
    LOGE("mProgram !=0");
    //从shader 读取属性
    positionLocation = glGetAttribLocation(mProgram,"poistion");
    colorLocation = glGetAttribLocation(mProgram,"color");
    normalLocation = glGetAttribLocation(mProgram,"normal");
    texcoordLocation= glGetAttribLocation(mProgram,"textcoord");
    projectionMatrixLocation = glGetUniformLocation(mProgram,"ProjectionMatrix");
    viewMatrixLocation = glGetUniformLocation(mProgram,"ViewMatrix");
    modelMatrixLocation = glGetUniformLocation(mProgram,"ModelMatrix");



}

void SShader::bind(float *M, float *V, float *P) {
    glUseProgram(mProgram);
    glUniformMatrix4fv(modelMatrixLocation,1,GL_FALSE,M);
    glUniformMatrix4fv(viewMatrixLocation,1,GL_FALSE,V);
    glUniformMatrix4fv(projectionMatrixLocation,1,GL_FALSE,P);

    ////多重贴图的做法
    int index = 0;
    for(auto iterators = uniformTextures.begin(); iterators !=uniformTextures.end();++iterators){
        //需要激活
        glActiveTexture(GL_TEXTURE0+index);
        glBindTexture(GL_TEXTURE_2D,iterators->second->mTexture);
        glUniform1i(iterators->second->mLocation,index++);
    }



    /* 单个贴图做法
     * if (uniformTexture.mLocation!=-1){
         glBindTexture(GL_TEXTURE_2D,uniformTexture.mTexture);
         glUniform1i(uniformTexture.mLocation,0);
     }*/

    for (auto iterators = uniformVec4s.begin();iterators!=uniformVec4s.end();++iterators) {
        glUniform4fv(iterators->second->mLocation,1,iterators->second->data);
    }

    //启动
    glEnableVertexAttribArray(positionLocation);
    glVertexAttribPointer(positionLocation,4,GL_FLOAT,GL_FALSE, sizeof(Vertex),0);

    glEnableVertexAttribArray(colorLocation);
    glVertexAttribPointer(colorLocation, 4, GL_FLOAT, GL_FALSE, sizeof(Vertex),
                          (const void *) (sizeof(float) * 4));

    glEnableVertexAttribArray(texcoordLocation);
    glVertexAttribPointer(texcoordLocation, 4, GL_FLOAT, GL_FALSE, sizeof(Vertex),
                          (const void *) (sizeof(float) * 8));

    glEnableVertexAttribArray(normalLocation);
    glVertexAttribPointer(normalLocation, 4, GL_FLOAT, GL_FALSE, sizeof(Vertex),
                          (const void *) (sizeof(float) * 12));



}

void SShader::setTextureEexternalOes(GLuint textureId){
    glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureId);
    glActiveTexture(GL_TEXTURE0);
    glUniform1i(texcoordLocation, 0);
}

//单个的做法设置贴图
//void SShader::setTexture(const char* name , const char* imagePath){
//    if (uniformTexture.mLocation ==-1){
//        uniformTexture.mLocation  = glGetUniformLocation(mProgram,name);
//        if (uniformTexture.mLocation !=-1){
//            LOGE("setTexture yes");
//            uniformTexture.mTexture = crateTexture2dFromBmp(imagePath);
//        }
//
//    }
//
//}
//设置贴图
void SShader::setTexture(const char* name , const char* imagePath){
    auto iterators = uniformTextures.find(name);
    if (iterators ==uniformTextures.end()){
        //找不到就创建
        GLuint  location  = glGetUniformLocation(mProgram,name);
        if (location !=-1){
            UniformTexture *uniformTexture =new UniformTexture;
            LOGE("setTexture yes");
            uniformTexture->mTexture = crateTexture2dFromBmp(imagePath);
            uniformTexture->mLocation = location;
            uniformTextures.insert(std::pair<std::string,UniformTexture *>(name,uniformTexture));
        }
    } else{
        //重新赋值
        glDeleteTextures(1, &iterators->second->mTexture);
        iterators->second->mTexture = crateTexture2dFromBmp(imagePath);
    }

}

//设置Uniformvec4
void SShader::setUiformVec4(const char *name, float x, float y, float z, float w) {
    auto iterators = uniformVec4s.find(name);
    if (iterators ==uniformVec4s.end()){
        //找不到就创建
        GLuint  location  = glGetUniformLocation(mProgram,name);
        if (location !=-1){
            UniformVec4 *uniformVec4 =new UniformVec4;
            uniformVec4->mLocation = location;
            uniformVec4->data[0] = x;
            uniformVec4->data[1] = y;
            uniformVec4->data[2] = z;
            uniformVec4->data[3] = w;
            uniformVec4s.insert(std::pair<std::string,UniformVec4 *>(name,uniformVec4));
        }
    } else{
        //重新赋值
        iterators->second->data[0] = x;
        iterators->second->data[1] = y;
        iterators->second->data[2] = z;
        iterators->second->data[3] = w;
    }

}


void SShader::setTexture(char * name,GLuint texture){
    auto iterators = uniformTextures.find(name);
    if (iterators ==uniformTextures.end()){
        //找不到就创建
        GLint  location  = glGetUniformLocation(mProgram,name);
        if (location !=-1){
            UniformTexture *uniformTexture =new UniformTexture;
            LOGE("setTexture yes");
            uniformTexture->mTexture = texture;
            uniformTexture->mLocation = location;
            uniformTextures.insert(std::pair<std::string,UniformTexture *>(name,uniformTexture));
        }
    } else{
        //重新赋值
        glDeleteTextures(1, &iterators->second->mTexture);
        iterators->second->mTexture = texture;
    }

}

//void SShader::setUiformVec3f(char * name,float r,float g ,float b){
//
//    glUniform3f(glGetUniformLocation(mProgram, "U_MultipleFilter"), r, g,b);
////    GLint  location  = glGetUniformLocation(mProgram,"U_MultipleFilter");
////    glUniform3f(location,r,g,b);
//}






