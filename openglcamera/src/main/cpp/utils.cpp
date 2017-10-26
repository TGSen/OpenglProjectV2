//
// Created by Administrator on 2017/10/11.
//
#include "utils.h"

GLuint complieShader(GLenum shaderType, const char *shaderCode) {
    GLuint shader = glCreateShader(shaderType);
    //1 代表的是1个shader 代码因为就存在shaderCode变量里，shaderCode代表着一个vs fs 文件里代码
    glShaderSource(shader, 1, &shaderCode, nullptr);
    glCompileShader(shader);
    //查看编译的结果
    GLint compileRes = GL_TRUE;
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compileRes);
    if (compileRes == GL_FALSE) {
        char slog[1024] = {0};
        GLsizei logLen = 0;
        glGetShaderInfoLog(shader, 1024, &logLen, slog);
        LOGE("Compile Shader fail log: %s : \n shader code :\n%s\n", slog, shaderCode);
        //需要删除
        glDeleteShader(shader);
        shader = 0;
    }
    return shader;

}
//将vs shader fs shader 链接程序,组成一个整体，用来画图的

GLuint createProgram(GLuint vsShader, GLuint fsShader) {
    //创建一个渲染程序
    GLuint program = glCreateProgram();
    glAttachShader(program, vsShader);
    glAttachShader(program, fsShader);
    glLinkProgram(program);
    glDetachShader(program, vsShader);
    glDetachShader(program, fsShader);
    GLint res = GL_TRUE;
    glGetProgramiv(program, GL_LINK_STATUS, &res);
    if (res == GL_FALSE) {
        char slog[1024] = {0};
        GLsizei logLen = 0;
        glGetProgramInfoLog(program, 1024, &logLen, slog);
        LOGE("glLinkProgram Shader fail log: %s : ", slog);
        glDeleteProgram(program);
        program = 0;
    }
    return program;
}

//获取毫秒级的时间，理论上来说把秒级的sec*1000+微秒级的usec/1000
float getTime() {
    static unsigned long long lastTime = 0, curentTime = 0;
    timeval current;
    gettimeofday(&current, nullptr);
    curentTime = current.tv_sec * 1000 + current.tv_usec / 1000;
    //lastTime 为0 时是第一帧
    unsigned long long frameTime = lastTime == 0 ? 0 : curentTime - lastTime;
    lastTime = curentTime;
    return frameTime / 1000.0f;

}


unsigned char *decodeBmp(unsigned char *bmpFileData, int &width, int &height) {
    //先判断是否是位图
    if (0x4D42 == *((unsigned short *) bmpFileData)) {
        int pixelDataOffset = *(int *) (bmpFileData + 10);
        width = *(int *) (bmpFileData + 18);
        height = *(int *) (bmpFileData + 22);
        //像素数据的起始位置
        unsigned char *pixelData = bmpFileData + pixelDataOffset;
        //bgr  需要转成rgb
        for (int i =0; i < width * height * 3; i += 3) {
            unsigned char temp = pixelData[i];
            pixelData[i] = pixelData[i + 2];
            pixelData[i + 2] = temp;
        }
        return pixelData;
    }
    LOGE("decodeBmp nullptr");
    return nullptr;
}


GLuint createTexture2D(unsigned char *piexlData, int width, int height, GLenum type) {
    GLuint textureId;
    //创建一个纹理对象
    glGenTextures(1, &textureId);
    /**
     * target —— 纹理被绑定的目标，它只能取值GL_TEXTURE_1D、GL_TEXTURE_2D、GL_TEXTURE_3D或者GL_TEXTURE_CUBE_MAP；
     */
    glBindTexture(GL_TEXTURE_2D, textureId);
    /**1、glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
　　GL_TEXTURE_2D: 操作2D纹理.
　　GL_TEXTURE_WRAP_S: S方向上的贴图模式.
　　GL_CLAMP: 将纹理坐标限制在0.0,1.0的范围之内.如果超出了会如何呢.不会错误,只是会边缘拉伸填充.
　　2、glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
　　这里同上,只是它是T方向
　　3、glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
　　这是纹理过滤
　　GL_TEXTURE_MAG_FILTER: 放大过滤
　　GL_LINEAR: 线性过滤, 使用距离当前渲染像素中心最近的4个纹素加权平均值.
　　5、glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
　　GL_TEXTURE_MIN_FILTER: 缩小过滤
　　GL_LINEAR_MIPMAP_NEAREST: 使用GL_NEAREST对最接近当前多边形的解析度的两个层级贴图进行采样,然后用这两个值进行线性插值.
     *
     */
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

    /**GL_REPEAT:坐标的整数部分被忽略，重复纹理，这是OpenGL纹理默认的处理方式.
    GL_MIRRORED_REPEAT: 纹理也会被重复，但是当纹理坐标的整数部分是奇数时会使用镜像重复。
    GL_CLAMP_TO_EDGE: 坐标会被截断到[0,1]之间。结果是坐标值大的被截断到纹理的边缘部分，形成了一个拉伸的边缘(stretched edge pattern)。
    GL_CLAMP_TO_BORDER: 不在[0,1]范围内的纹理坐标会使用用户指定的边缘颜色。
    */
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    /**
     * 参数说明：
target 指定目标纹理，这个值必须是GL_TEXTURE_2D。
level 执行细节级别。0是最基本的图像级别，n表示第N级贴图细化级别。
internalformat 指定纹理中的颜色组件。可选的值有GL_ALPHA,GL_RGB,GL_RGBA,GL_LUMINANCE, GL_LUMINANCE_ALPHA 等几种。
width 指定纹理图像的宽度，必须是2的n次方。纹理图片至少要支持64个材质元素的宽度
height 指定纹理图像的高度，必须是2的m次方。纹理图片至少要支持64个材质元素的高度
border 指定边框的宽度。必须为0。
format 像素数据的颜色格式, 不需要和internalformatt取值必须相同。可选的值参考internalformat。
type 指定像素数据的数据类型。可以使用的值有GL_UNSIGNED_BYTE,GL_UNSIGNED_SHORT_5_6_5,GL_UNSIGNED_SHORT_4_4_4_4,GL_UNSIGNED_SHORT_5_5_5_1。
pixels 指定内存中指向图像数据的指针
     */
    glTexImage2D(GL_TEXTURE_2D, 0, type, width, height, 0, type, GL_UNSIGNED_BYTE, piexlData);
    //上面已经生成了texture,这里置为0，避免被其他程序修改，良好习惯
    glBindTexture(GL_TEXTURE_2D,0);
    return textureId;

}

GLuint crateTexture2dFromBmp(const char* bmpPath){
    int fileSize = 0;
    //加载文件
    unsigned char *file =   loadFile(bmpPath,fileSize);
    if (file == nullptr){
        LOGE("crateTexture2dFromBmp file nullptr");
        //0号纹理一般黑色的
        return 0;
    }
    int width = 0,height = 0;

    //解析图片
    unsigned char * pixelData = decodeBmp(file,width,height);
    LOGE("crateTexture2dFromBmp %d * %d",width,height);
    if (pixelData == nullptr){
        LOGE("crateTexture2dFromBmp pixelData nullptr");
        delete file;
        return 0;
    }
    //加载纹理
    GLuint  textId = createTexture2D(pixelData,width,height,GL_RGB);
    delete file;
//    delete pixelData;
    LOGE("crateTexture2dFromBmp yes");
    return textId;
}

GLuint createBufferObj(GLenum bufferType,GLsizeiptr size,GLenum usage, void *data){
    GLuint  obj;
    glGenBuffers(1,&obj);
    glBindBuffer(bufferType,obj);
    glBufferData(bufferType,size,data,usage);
    glBindBuffer(bufferType,0);
    return obj;
}
//Openl 程序纹理
GLuint createProcedureTexture(int size) {
    unsigned char *imageData = new unsigned char[size*size * 4];
    float halfSize = (float)size / 2.0f;
    float maxDistance = sqrtf(halfSize*halfSize + halfSize*halfSize);
    float centerX = halfSize;
    float centerY = halfSize;
    for (int y = 0; y < size; ++y) {
        for (int x = 0; x < size; ++x) {
            int currentPixelOffset = (x + y*size) * 4;
            imageData[currentPixelOffset] = 255;
            imageData[currentPixelOffset + 1] = 255;
            imageData[currentPixelOffset + 2] = 255;
            float deltaX = (float)x - centerX;
            float deltaY = (float)y - centerY;
            float distance = sqrtf(deltaX*deltaX + deltaY*deltaY);
            float alpha = powf(1.0f - (distance / maxDistance), 8.0f);
            alpha = alpha > 1.0f ? 1.0f : alpha;
            imageData[currentPixelOffset + 3] = (unsigned char)(alpha*255.0f);
        }
    }
    GLuint texture = createTexture2D(imageData, size, size, GL_RGBA);
    return texture;
}


