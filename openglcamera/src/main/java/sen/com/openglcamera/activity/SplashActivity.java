package sen.com.openglcamera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/30 11:47
 * Des    : 闪屏页，其实啥也没做，只是做了一个主题加载
 *          为了冷启动，就是显示图标而不是白屏
 */

public class SplashActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,CameraMainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}
