package sen.com.openglcamera.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import sen.com.openglcamera.R;
import sen.com.openglcamera.fragment.picture.EffectsFragment;
import sen.com.openglcamera.fragment.picture.FilterFragment;
import sen.com.openglcamera.fragment.picture.ShapeFragment;
import sen.com.openglcamera.natives.BaseGLNative;
import sen.com.openglcamera.renderer.RenderLinstener;
import sen.com.openglcamera.utils.PermissionsUitls;
import sen.com.openglcamera.view.LoadingView;
import sen.com.openglcamera.view.PictureSGLSurfaceView;

/**
 * Author : 唐家森
 * Version: 1.0
 * Des    : 图像处理
 */
public class PictureHandleActivity extends AppCompatActivity implements View.OnClickListener, RenderLinstener {
    PictureSGLSurfaceView mSGlSurfaceView;
    private final String LTag = "sen_";
    private View rootView;
    String[] mPermissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CAMERA_WRITE_CODE = 2;
    private static final int APP_SETTINGS_CODE = 0x002;
    private int requestPermissions;
    private Handler mThreadHandler;
    private HandlerThread mHandlerThread;
    private String mRootPath;
    private final int MSG_INIT_BITMAP = 0;

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private String mTiltes[] = {"形色","滤镜","特效"};
    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //初始化AssetManager
        BaseGLNative.initAssetManager(getAssets(),BaseGLNative.NATIVE_SENCE_PICTURE);
        rootView = View.inflate(this, R.layout.activity_picture_opengl, null);
        setContentView(rootView);

    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSGlSurfaceView = (PictureSGLSurfaceView) rootView.findViewById(R.id.camera_glview);
        mSGlSurfaceView.init().setRenderListener(this);
        mTablayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        loadingView = findViewById(R.id.loadingView);
        loadingView.startAnimator();

        for (int i = 0;i<mTiltes.length;i++){
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
        }
         List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ShapeFragment());
        fragments.add(new FilterFragment());
        EffectsFragment effectsFragment = new EffectsFragment();
        effectsFragment.setOnAddObjecteDrawFrameLisenter(mSGlSurfaceView.getRenderer());
        fragments.add(effectsFragment);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), mTiltes, fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onDrawOneFrameFinish() {
        loadingView.stopAnimator();
        loadingView.setVisibility(View.GONE);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private String[] titleList;
        private List<Fragment> fragmentList;

        public ViewPagerAdapter(FragmentManager fm, String[] titleList, List<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList[position];
        }
    }


    //对button需要做多次点击才行，要不会弹出多个
    //对camera setting
    public void settingView(View view) {


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSGlSurfaceView != null) ;
        if (PermissionsUitls.hasPermissions(this, mPermissions)) {
            initHandlerThread();
        } else {
            ActivityCompat.requestPermissions(PictureHandleActivity.this,
                    mPermissions,
                    REQUEST_CAMERA_WRITE_CODE);
        }

    }


    private void initHandlerThread() {
        mHandlerThread = new HandlerThread("getBitmap");
        mHandlerThread.start();

        mThreadHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_INIT_BITMAP:

                        break;
                }

            }
        };
        if (mSGlSurfaceView == null) {
            mThreadHandler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSGlSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        //释放资源
        if (mSGlSurfaceView != null) {
            mSGlSurfaceView.destroyDrawingCache();
        }
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
        BaseGLNative.releaseNative(BaseGLNative.NATIVE_SENCE_PICTURE);
        super.onDestroy();

    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.takePicture:
//                if (PermissionsUitls.hasPermissions(this, mPermissions)) {
//                } else {
//                    ActivityCompat.requestPermissions(PictureHandleActivity.this,
//                            mPermissions,
//                            REQUEST_CAMERA_WRITE_CODE);
//                }
//                break;
//        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CAMERA_WRITE_CODE == requestCode) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //保证camera 和写入权限都有，这种情况下肯定camera 有的
                // cameraButtonView.handleClick();
            } else {
                requestPermissions++;
                if (requestPermissions >= 2) {
                    //被拒两次后就去设置，这种情况有可能是禁止后不再询问
                    Snackbar.make(rootView, "请设置Camera,存储权限",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("设置", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivityForResult(
                                            new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                    .setData(Uri.fromParts("package", getPackageName(), null)),
                                            APP_SETTINGS_CODE);
                                }
                            })
                            .show();
                } else {
                    //再次尝试申请一次
                    ActivityCompat.requestPermissions(PictureHandleActivity.this,
                            mPermissions,
                            REQUEST_CAMERA_WRITE_CODE);

                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_SETTINGS_CODE) {
            //当去设置camera camera 有可能被取消了
            if (PermissionsUitls.hasPermissions(this, mPermissions)) {

            } else {
                Snackbar.make(rootView, "请设置必要权限",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("设置", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivityForResult(
                                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                .setData(Uri.fromParts("package", getPackageName(), null)),
                                        APP_SETTINGS_CODE);
                            }
                        })
                        .show();
            }
        }
    }
}


