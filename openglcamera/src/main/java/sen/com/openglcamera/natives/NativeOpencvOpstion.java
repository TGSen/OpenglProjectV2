package sen.com.openglcamera.natives;

import android.graphics.Bitmap;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/28 16:42
 * Des    : 关于底层opencv 的操作
 */

public class NativeOpencvOpstion {
    static{
        System.loadLibrary("sgles");
    }
    //保存图片 ,宽高是指camera 设置的pictureSize
    public static native void saveBitmapByData(byte[] data,int width,int height);

    public static native void saveBitmap(Bitmap bitmap);
}
