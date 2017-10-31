package sen.com.openglcamera.natives;

import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/11 13:19
 * Des    :
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
    public static native void onChangeShape(int  cameraShape) ;

    //稀放
    public static native void releaseNative();

    public static boolean isStop = false;

    //修改背景颜色，请传0-1之间的值
    public static native void onChangeBgColor(float r, float g, float b, float a);
}
