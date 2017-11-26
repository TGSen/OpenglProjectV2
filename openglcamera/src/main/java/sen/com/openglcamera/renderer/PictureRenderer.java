package sen.com.openglcamera.renderer;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Environment;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import sen.com.openglcamera.natives.BaseGLNative;
import sen.com.openglcamera.utils.BitmapUtils;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 13:40
 * Des    : 图片的渲染器
 */

public class PictureRenderer implements GLSurfaceView.Renderer {

    private final String mRootPath;

    public PictureRenderer() {
        mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Bitmap bitmap = BitmapUtils.decodeFile(mRootPath + "test.jpg", 300, 300);
        BaseGLNative.onBeforeSurfaceCreated(bitmap);
        BaseGLNative.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        BaseGLNative.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        BaseGLNative.onDrawFrame(null,0,0);
    }

}
