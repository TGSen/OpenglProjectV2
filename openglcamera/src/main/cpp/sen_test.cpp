//
// Created by Administrator on 2017/10/13.
//

#include <map>
#include "sen_test.h"
#include<string>
#include "android/log.h"
#define LOG_TAG    "sgllog"
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)
using namespace std;
void testCMap(){
    map<int ,int > maps ;
    maps [1] = 10;
    maps [2] = 20;
    maps [3] = 30;
    maps [4] = 40;
    maps [5] = 50;
//    map<string,int>::iterator iterator;   定义一个迭代指针iterator。
//    it->first 为索引键值，it->second 为值。
    map<int ,int >::iterator iterators;
    for (iterators=maps.begin();iterators!=maps.end();++iterators){

       LOGE("key-> %d ;value -> %d",iterators->first,iterators->second);
    }
}
