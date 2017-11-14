package sen.com.openglcamera.natives;

import android.graphics.SurfaceTexture;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/11 13:19
 * Des    :CameraShape 需要跟c++ 定义顺序保持一致
 */

public class CameraSGLNative extends BaseGLNative {


    /**
     * JNI 创建SurfaceTexture
     * @return
     */
    public static native SurfaceTexture getSurfaceTexture();


}
