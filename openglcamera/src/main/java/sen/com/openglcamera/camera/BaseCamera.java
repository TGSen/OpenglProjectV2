package sen.com.openglcamera.camera;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/5 15:38
 * Des    : camera 的生命周期跟使用者的Activity 或者Fragment 绑定
 */

public interface BaseCamera {
    void onResume();

    void onPause();

    void onDestroy();
}
