#include "camera/utils.h"
#include "camera/Model3D.h"

Model::Model() {
    //做一些属性的初始化

}

void Model::initModel(const char *path) {
    //申明一个结构体用于保存V系列的数据
    struct FLoatData {
        float data[3];
    };
    //申明绘制指令的结构体
    struct VertexDefine {
        //这些索引，表示指向之前保存的v 系列数据的索引
        int positionIndex; //位置的索引
        int textcoordIndex;
        int normalIndex;
    };
    int fileSize = 0;
    unsigned char *file = loadFile(path, fileSize);
    if (file == nullptr)
        return;

    std::vector<FLoatData> positions, textcoords, normals;
    std::vector<VertexDefine> vertexDefines;
    std::string temp;

    //然后把模型大致的分类，主要拿v 开头的数据，和f开头的数据（绘制指令，用来控制v开头的数据）
    //使用stringstream 来提取数据，将文件的内存块赋值进去
    std::stringstream sfile((char *) file);
    //从文件中读取某一行数据
    char szOneLine[256];
    //不断读取数据
    while (!sfile.eof()) {
        //先清0
        memset(szOneLine, 0, 256);
        sfile.getline(szOneLine, 256);
        //如果有数据,将数据分类
        if (strlen(szOneLine) > 0) {
            if (szOneLine[0] == 'v') {
                std::stringstream sOneLine(szOneLine);
                //v 系列的数据
                if (szOneLine[1] == 't') {
                    //texcoord
                    LOGE("texcoord %s\n", szOneLine);
                    FLoatData fLoatData;
                    sOneLine >> temp;
                    sOneLine >> fLoatData.data[0];
                    sOneLine >> fLoatData.data[1];
                    textcoords.push_back(fLoatData);
                    LOGE("texcoord fLoatData %f-%f\n", fLoatData.data[0], fLoatData.data[1]);

                } else if (szOneLine[1] == 'n') {
                    //normal
                    LOGE("normal %s\n", szOneLine);
                    FLoatData fLoatData;
                    sOneLine >> temp;
                    sOneLine >> fLoatData.data[0];
                    sOneLine >> fLoatData.data[1];
                    sOneLine >> fLoatData.data[2];
                    normals.push_back(fLoatData);
                    LOGE("normal fLoatData %f-%f-%f\n", fLoatData.data[0], fLoatData.data[1],
                         fLoatData.data[2]);
                } else {
                    //position
                    LOGE("position %s\n", szOneLine);
                    FLoatData fLoatData;
                    sOneLine >> temp;
                    sOneLine >> fLoatData.data[0];
                    sOneLine >> fLoatData.data[1];
                    sOneLine >> fLoatData.data[2];
                    positions.push_back(fLoatData);
                    LOGE("position fLoatData %f-%f-%f\n", fLoatData.data[0], fLoatData.data[1],
                         fLoatData.data[2]);
                }
            } else if (szOneLine[0] == 'f') {
                //f 系列数据，face ,表示每个面有那个些点组成,渲染指令
                LOGE("draw f command %s\n", szOneLine);
                //将字符窜 塞进流里
                std::stringstream fOneLine(szOneLine);
                //了解f 数据： 1/1/1  2/2/2  3/3/3
                //所以输出三次
                fOneLine >> temp;
                std::string subVertex; //这个表示 1/1/1 三段中其中一段
                for (int i = 0; i < 3; i++) {
                    //输出到   subVertex
                    fOneLine >> subVertex;
                    //然后解析一段
                    size_t fristPos = subVertex.find_first_of('/');
                    //第一个是position index
                    std::string subPosIndex = subVertex.substr(0, fristPos);
                    size_t secondPos = subVertex.find_first_of('/', fristPos + 1);
                    //第二个是纹理坐标的index
                    std::string subTexcoordIndex = subVertex.substr(fristPos + 1,
                                                                    secondPos - 1 - fristPos);
                    //第三个是narmal 法向index
                    std::string subNormalIndex = subVertex.substr(secondPos + 1,
                                                                  subVertex.length() - 1 -
                                                                  secondPos);

                    //然后将String -》 int
                    VertexDefine vertexDefine;
                    //atoi (表示 ascii to integer)是把字符串转换成整型数的一个函数
                    vertexDefine.positionIndex = atoi(subPosIndex.c_str());
                    vertexDefine.textcoordIndex = atoi(subTexcoordIndex.c_str());
                    vertexDefine.normalIndex = atoi(subNormalIndex.c_str());

                    vertexDefines.push_back(vertexDefine);
                }
            }
        }
    }
    //解码完成
    delete file;

    int vertexSize = vertexDefines.size();
    vertexBuffer = new VertexBuffer;
    vertexBuffer->setSize(vertexSize);
    //生产opengl vertex 数据
    for (int i = 0; i < vertexSize; ++i) {
        //模型是从1开始的，opengl 从0,所以得减一
        //获取位置的起始地址
        float *temp = positions[vertexDefines[i].positionIndex - 1].data;
        vertexBuffer->setPosition(i, temp[0], temp[1], temp[2]);
        //获取texcoord
        temp = textcoords[vertexDefines[i].textcoordIndex - 1].data;
        vertexBuffer->setTexcoord(i, temp[0], temp[1]);
        temp = normals[vertexDefines[i].normalIndex - 1].data;
        vertexBuffer->setNarmal(i, temp[0], temp[1], temp[2]);
    }
    mShader = new SShader;
//    mShader->init("Res/model.vs", "Res/model.fs");
//    //设置光的环境光分量,白光
//    setAmbientMaterial(0.1f, 0.1f, 0.1f, 1.0f);
//    setSpecularMaterial(1.0f, 1.0f, 1.0f, 1.0f);
//    setDiffusseMaterial(0.6f, 0.6f, 0.6f, 1.0f);
//
//    mShader->setUiformVec4("U_LightAmbient", 1.0f, 1.0f, 1.0f, 1.0f);
//    //设置模型对环境光的反射系数分量,通过接口setAmbientMaterial 来设置
//    //mShader->setUiformVec4("U_AmbientMaterial",0.1f,0.1f,0.1f,1.0f);
//    //设置光源的位置
//    mShader->setUiformVec4("U_LightPos", 1.0f, 0.0f, 1.0f, 0.0f);
//    mShader->setUiformVec4("U_LightDiffuse", 1.0f, 1.0f, 1.0f, 1.0f);
//    //  mShader->setUiformVec4("U_DiffuseMaterial",0.6f,0.6f,0.6f,1.0f);
//    //设置镜面反射光的参数
//    mShader->setUiformVec4("U_LigthSpecular", 1.0f, 1.0f, 1.0f, 1.0f);
//    //   mShader->setUiformVec4("U_SpecularMaterial",1.0f,1.0f,1.0f,1.0f);
//    mShader->setUiformVec4("U_CameraPos", 1.0f, 1.0f, 1.0f, 1.0f);
//    mShader->setUiformVec4("U_LightOpt", 32.0f, 0.0f, 0.0f, 0.0f);




}

void Model::drawModel(glm::mat4 &mViewMatrix, glm::mat4 &mProjectionMatrix, float x, float y,float z) {
    glEnable(GL_DEPTH_TEST);
    //由于光照需要摄像机的位置
    mShader->setUiformVec4("U_CameraPos",x,y,z,1.0);
    vertexBuffer->bind();
    if(mShader->mProgram>0){
        mShader->bind(glm::value_ptr(mModelMatrix), glm::value_ptr(mViewMatrix),
                      glm::value_ptr(mProjectionMatrix));
        //计算法向矩阵，转置矩阵，将modeMatrix 转置矩阵就是了
        glm::mat4 it = glm::inverseTranspose(mModelMatrix);
       GLint location = glGetUniformLocation(mShader->mProgram, "IT_ModelMatrix");
        glUniformMatrix4fv(location, 1, GL_FALSE, glm::value_ptr(it));

        location = glGetUniformLocation(mShader->mProgram,"LightViewMatrix");
        if(location >=0 && mLightViewMatrix != nullptr){
            glUniformMatrix4fv(location, 1, GL_FALSE, mLightViewMatrix);
        }
        location = glGetUniformLocation(mShader->mProgram,"LightProjectMatrix");
        if(location >=0 && mLightProjectMatrix != nullptr){
            glUniformMatrix4fv(location, 1, GL_FALSE, mLightProjectMatrix);
        }

    }

    glDrawArrays(GL_TRIANGLES, 0, vertexBuffer->mVertexCount);
    vertexBuffer->unBind();
}

void Model::setModelPosition(float x, float y, float z) {
    mModelMatrix = glm::translate(x, y, z);
}

void Model::setAmbientMaterial(float r, float g, float b, float a) {
    mShader->setUiformVec4("U_AmbientMaterial",0.1f,0.1f,0.1f,1.0f);
}

void Model::setDiffusseMaterial(float r, float g, float b, float a) {
    mShader->setUiformVec4("U_DiffuseMaterial", 0.6f, 0.6f, 0.6f, 1.0f);

}

void Model::setSpecularMaterial(float r, float g, float b, float a) {
    mShader->setUiformVec4("U_SpecularMaterial",1.0f,0.0f,1.0f,1.0f);
}
void Model::setModelTexture(const char *imagePath){
    mShader->setTexture("U_Texture",imagePath);
};

void Model::setModelTexture(GLuint texture){
    mShader->setTexture("U_Texture",texture);
};
