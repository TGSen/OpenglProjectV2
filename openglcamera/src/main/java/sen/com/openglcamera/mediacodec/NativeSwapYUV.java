package sen.com.openglcamera.mediacodec;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/7 13:54
 * Des    :
 */

public class NativeSwapYUV {
    public static native byte[] nV21ToI420(byte[] data, int width,int height, int length);
    public static native byte[] yV12ToI420(byte[] data, int width,int height, int length);
}
