package sen.com.openglcamera.natives;

import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/11 13:19
 * Des    :CameraShape 需要跟c++ 定义顺序保持一致
 */

public class CameraSGLNative {




    public enum CameraShape{

        Normal(0),Circle(1),Multiple(2);
        private int value;
        CameraShape(int i) {
            this.value=i;
        }

        public int getValue() {
            return value;
        }
    }
    static{
        System.loadLibrary("sgles");
    }
    /**
     * 为了让底层去获取android 资源
     * @param assets
     */
    public static native void initAssetManager(AssetManager assets);
    /**
     * 初始化opengl
     */
    public static native void onSurfaceCreated();

    public static native void onSurfaceChanged(int width, int height);
    //更新一帧画面
    public static native void onDrawFrame();

    /**
     * JNI 创建SurfaceTexture
     * @return
     */
    public static native SurfaceTexture getSurfaceTexture();

    //修改滤镜参数
    public static native void onChangeFileter(int r,int g,int b,int a,int max);
    //修改 滤镜文件
    public static native void onChangeVSFS(String vs, String fs) ;

    //修改形状
    public static native void onChangeShape(int  cameraShape,int count) ;

    //稀放
    public static native void releaseNative();

    public static boolean isStop = false;

    //修改背景颜色，请传0-1之间的值
    public static native void onChangeBgColor(float r, float g, float b, float a);
    //修改形状的大小
    public static native void onChangeShapeSize(int size,int max) ;
    //修改及几遍形
    public static native void onChangeShapeCount(int count) ;
    //修改要渲染的区域
    public static native void onChangeFileterZoom(int i, int max) ;
}
