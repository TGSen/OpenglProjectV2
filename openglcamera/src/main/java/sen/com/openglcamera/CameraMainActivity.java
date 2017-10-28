package sen.com.openglcamera;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

import sen.com.openglcamera.bean.CameraSettingInfo;
import sen.com.openglcamera.bean.CurrentCameInfo;
import sen.com.openglcamera.natives.CameraSGLNative;
import sen.com.openglcamera.view.CameraButtonView;
import sen.com.openglcamera.view.CameraSGLSurfaceView;

public class CameraMainActivity extends AppCompatActivity implements View.OnClickListener, CameraInfoFragment.OnSettingChangeLinstener {
    CameraSGLSurfaceView mSGlSurfaceView;
    private int mCameraId;
    private CameraOldVersion mCamera;
    private CameraButtonView takePicture;
    private final String LTag = "sen_";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //初始化AssetManager
        CameraSGLNative.initAssetManager(getAssets());
        setContentView(R.layout.activity_camera_opengl);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSGlSurfaceView = (CameraSGLSurfaceView) findViewById(R.id.camera_glview);
        mSGlSurfaceView.setVisibility(View.VISIBLE);
        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        DisplayMetrics dm = new DisplayMetrics();
        mCamera = new CameraOldVersion(this);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;
        mCamera.setTakePicturePath(path);
        if (mCamera.openCamera(dm.widthPixels, dm.heightPixels, mCameraId)) {
            mSGlSurfaceView.init(mCamera, false, this);
            takePicture = (CameraButtonView) findViewById(R.id.takePicture);
            takePicture.setOnClickListener(this);
        }

    }
    //对camera setting
    public void settingView(View view){
        CameraSettingInfo info =mCamera.getCameraSettingInfo();
        //获取当前的设置
        CurrentCameInfo currentCameInfo = mCamera.getCurrentSettingInfo();
        CameraInfoFragment dialog = new CameraInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("CameraSettingInfo",info);
        bundle.putSerializable("CurrentCameInfo",currentCameInfo);
        dialog.setArguments(bundle);
        dialog.setOnSettingChangeLinstener(this);
        dialog.show(getFragmentManager(), "CameraInfoFragment");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCamera!=null){
            mCamera.releaseCamera();
        }
        CameraSGLNative.releaseNative();
    }
    //点击屏幕对焦
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() ==MotionEvent.ACTION_DOWN){
            if (mCamera!=null){
                mCamera.requestCameraFocus();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takePicture:
                if (mCamera != null)
                    mCamera.takePhoto();
                break;
        }

    }

    @Override
    public void onSettingChange(CurrentCameInfo currentCameInfo) {
        mCamera.setCameraInfo(currentCameInfo);
//        Log.e("sen_",currentCameInfo.toString());
    }
}


