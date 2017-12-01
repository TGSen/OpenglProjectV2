package sen.com.openglcamera.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import sen.com.openglcamera.R;
import sen.com.openglcamera.utils.PermissionsUitls;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/30 11:47
 * Des    : 闪屏页，做了一个主题加载
 *          为了冷启动，就是显示图标而不是白屏
 *          还有Camera 权限的检查
 */

public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_CODE = 0;
    private static final int APP_SETTINGS_CODE = 0x001;
    private View rootView;
    String[] mPermissions = {Manifest.permission.CAMERA};
    private int requestPermissions = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_splash,null);
        setContentView(rootView);
        checkPermission();
    }

    private void checkPermission() {
        // 检查摄像头权限是否已经有效
        if (PermissionsUitls.hasPermissions(this, mPermissions)) {
            // 摄像头权限还未得到用户的同意
            // 权限有效
            hasPaermissions();

        } else {
            // 如果用户已经拒绝过，那么提供额外的权限，说明那么应该弹出去设置
            //但是这种情况下，就是，当用户点击了禁止，并不再询问，
            ActivityCompat.requestPermissions(SplashActivity.this,
                    mPermissions,
                    REQUEST_CAMERA_CODE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CAMERA_CODE == requestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPaermissions();

            } else {
                requestPermissions++;
                if (requestPermissions >= 2) {
                    //被拒两次后就去设置，这种情况有可能是禁止后不再询问
                    Snackbar.make(rootView, "请设置Camera权限",
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
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            mPermissions,
                            REQUEST_CAMERA_CODE);

                }
            }
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_SETTINGS_CODE) {
            if (PermissionsUitls.hasPermissions(this, mPermissions)) {
                hasPaermissions();
            } else {
                Snackbar.make(rootView, "请设置Camera权限",
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

    private void hasPaermissions() {
        Intent intent = new Intent(this,PictureVideoActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }


}
