package sen.com.openglcamera.renderer;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;

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
    private int mWidth;
    private int mHeight;
    public PictureRenderer() {
        mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        BaseGLNative.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Bitmap bitmap = BitmapUtils.decodeFile(mRootPath + "test.jpg", 200, 300);
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
        BaseGLNative.onBeforeSurfaceCreated(bitmap);
        // PictureSGLNativie.onSurfaceChanged(width, height);
        BaseGLNative.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e("sen_","onDrawFrame");
        BaseGLNative.onDrawFrame(null,0,0);
    }

}
