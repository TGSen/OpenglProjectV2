package sen.com.openglcamera.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/31 16:40
 * Des    :
 */

public abstract class BaseFragment extends Fragment {
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;
    private View parentView;
    public Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = initParentView(inflater, container, savedInstanceState);

        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onLoadData();
        } else {
            isVisible = false;
        }
    }

    protected abstract void onLoadData();

    /**
     * 初始化view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public abstract View initParentView(LayoutInflater inflater,
                                        ViewGroup container, Bundle savedInstanceState);


}
