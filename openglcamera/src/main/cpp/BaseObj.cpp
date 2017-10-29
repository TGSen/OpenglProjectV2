//
// Created by Administrator on 2017/10/20.
//

#include <sggl.h>
#include "BaseObj.h"


BaseObj::BaseObj() {
    isChangeVSFS = false;
    fsPath = nullptr;
    vsPath = nullptr;
}

BaseObj::~BaseObj(){
    if (fsPath!= nullptr){
        delete fsPath;
    }

    if(vsPath!= nullptr){
        delete vsPath;
    }
    if(mShader!= nullptr){
        delete mShader;
    }

   if(vertexBuffer){
       free(vertexBuffer);
   }
    LOGE("稀放完毕");
}



void BaseObj::init(float x,float y,float z) {

}

void BaseObj::drawModel(glm::mat4 &mViewMatrix, glm::mat4 &mProjectionMatrix) {

}
//修改 shader 变量参数
void BaseObj::changeFilter(float cr,float cg, float cb , float ca){
    mShader->setUiformVec4("U_MultipleFilter",cr,cg,cb,ca);

}

//修改 vs shader ,和fs shader
void BaseObj::changeVSFS(const char* vspath, const char*fspath){
    //给camera vsPath ，fsPath 中赋值，到时用完后得 清内存
    size_t vsSize = strlen(vspath);
    vsPath = (char *) malloc(vsSize);
    memset(vsPath, 0, vsSize + 1);
    memcpy(vsPath, vspath, vsSize);

    size_t fsSize = strlen(fspath);
    fsPath = (char *) malloc(fsSize);
    memset(fsPath, 0, fsSize + 1);
    memcpy(fsPath, fspath, fsSize);
    LOGE("changeVSFS:%s,%s",vsPath,fsPath);
    isChangeVSFS = true;
}



