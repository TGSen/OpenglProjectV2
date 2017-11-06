## OpenglProjectV2<br>
##### 2017-10-26提交： <br>
1.opengl ndk实现预览滤镜效果 <br>
2.opengl 粒子简单版 <br>
3.拍照功能 <br>

目前还是用的是OpenGL2.0<br>


##### 2017-10-27更新

1.增加修改滤镜参数的功能 <br>
2.增加修改滤镜vs shader 和fs shader 文件的功能 <br>
3.整理c++ 代码<br>
4.调整UI 样式 <br>



##### 2017-10-28更新

1.预设几个好看滤镜, <br>
2.将之前ndk实现的美白效果，改成opengl shader实现 <br>

##### 2017-10-29上午更新 <br>
1.增加几个预览画面形状，可是有个bug(图像旋转了90度) <br>

##### 2017-10-31 更新<br>
1.加入MD 控件 <br>
2.调整java 与c++ 参数，java层可控滤镜等参数 <br>

##### 2017-11-1更新<br>
1.修改UI-2 <br>
2.review c++ 代码 <br>

##### 2017-11-2更新 <br>
1.review c++ 使用矩阵变换来操作形状大小变化，改变以前的方式（重新创建顶点数据，来修改形状的大小，浪费资源） <br>
2.修复以前的2017-10-29上午的bug， <br>
 修复解决：也是通过矩阵变换，旋转90度， <br>
 当时尝试重贴图坐标来弄，没想到办法，如果有人会这个得话，请指教，谢谢 <br>
 
 ##### 2017-11-3 更新
 1.整理readme 并添加图片 <br>
 2.解决一些魅族5.0的bug <br>

<br>
<div>
 <img src="https://github.com/TGSen/OpenglProjectV2/blob/master/openglcamera/captures/Screenshot_2017-11-02-23-13-55.png?raw=true" width = "180" height = "280" alt="1" align=center /> 
 <img src="https://github.com/TGSen/OpenglProjectV2/blob/master/openglcamera/captures/Screenshot_2017-11-02-23-14-31.png?raw=true" width = "180" height = "280" alt="2" align=center />

 <img src="https://github.com/TGSen/OpenglProjectV2/blob/master/openglcamera/captures/Screenshot_2017-11-02-23-14-41.png?raw=true" width = "180" height = "280" alt="3" align=center /> 
 <img src="https://github.com/TGSen/OpenglProjectV2/blob/master/openglcamera/captures/Screenshot_2017-11-02-23-15-04.png?raw=true" width = "180" height = "280" alt="4" align=center />
 
</div>
<br>
<div>
<img src="https://github.com/TGSen/OpenglProjectV2/blob/master/openglcamera/captures/Screenshot_2017-11-02-23-15-37.png?raw=true" width = "180" height = "280" alt="5" align=center /> 
 <img src="https://github.com/TGSen/OpenglProjectV2/blob/master/openglcamera/captures/Screenshot_2017-11-02-23-15-59.png" width = "180" height = "280" alt="6" align=center />
 <img src="https://github.com/TGSen/OpenglProjectV2/blob/master/openglcamera/captures/Screenshot_2017-11-02-23-16-19.png?raw=true" width = "180" height = "280" alt="7" align=center /> 
 <img src="https://github.com/TGSen/OpenglProjectV2/blob/master/openglcamera/captures/Screenshot_2017-11-02-23-16-37.png" width = "180" height = "280" alt="8" align=center />
 
</div>
<br>
  ##### 2017-11-6 更新
  1.Camera2的应用，不过后续开发还是接着Camera1，等完善到差不多再来封装Camera1和2 <br>
  2.加入android 6.0权限管理 <br>

