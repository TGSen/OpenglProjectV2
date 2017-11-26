package sen.com.openglcamera.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import sen.com.openglcamera.R;


/**
 * Author : 唐家森
 * Version: 1.0
 * Des    :
 *
 */

public abstract class BaseDialogFragment extends DialogFragment {
    public Activity mActivity;
    private Dialog dialog;
    //有些手机走这个
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity =  activity;
    }
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mActivity = (Activity) activity;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(setContentLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView( view);
        initData();
    }

    public abstract  int setContentLayoutId();
    public abstract void initView(View view);
    public abstract void initData();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar);
        initDialog();
        return dialog;
    }

    private void initDialog() {
        dialog = new Dialog(mActivity, R.style.Dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogAnimation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = mActivity.getResources().getDisplayMetrics().heightPixels * 2 / 3;
        lp.dimAmount = 0.0f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

}
