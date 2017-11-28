/* DO NOT EDIT THIS FILE - it is machine generated
 * 加载文件的工具
 * */
#pragma once
#include "sggl.h"
#include <sys/time.h>
// 第二个参数是作为返回值，返回加载成功后文件多大
unsigned char * loadFile(const char *path, int &fileSize);
//将asset 文件写入到SD卡
void writeFileToSdcard(const char *path, const char *outPath);

/**
 * 编译Shader
 * shaderType: vertex shader 顶点Shader ,fragment shader 片着色器
 * shaderCode: 在Res 写的fs vs 代码
 */

GLuint complieShader(GLenum shaderType,const char * shaderCode);
//将vs shader fs shader 链接程序,组成一个整体，用来画图的
//这个Program 是在显卡上跑的，而不是在cpu 跑的
GLuint createProgram (GLuint vsShader,GLuint fsShader);

//获取每帧的时间,也可以用测试在代码的执行时间
float getTime();

//解码bmp 图片,返回时bmp 像素的起始地址
unsigned char* decodeBmp(unsigned char* bmpFileData,int &width, int & height);

//创建纹理对象，返回是纹理标识符，参数：像素数据起始位置，最后一个是Rgb 还是RGBA
GLuint createTexture2D( char* piexlData,int width,int height,GLenum type);

void createTexture2DByData( GLuint textureId,unsigned char *piexlData, int width, int height, GLenum type);
//封装bmp 位图创建纹理对象
GLuint crateTexture2dFromBmp(const char* bmpPath);

//通过ndk bitmap 获取像素 来创建纹理
//如果opengl需要文字纹理，可以让java 通过写文字（字体可以定义）来创建bitmap 提供纹理
GLuint createTexture2dFromBitmap(JNIEnv *env,float& reqW,float& reqH, jobject bitmap);

//封装
GLuint createBufferObj(GLenum bufferType,GLsizeiptr size,GLenum usage, void *data);

GLuint createProcedureTexture(int size);

float checkData(float data);





