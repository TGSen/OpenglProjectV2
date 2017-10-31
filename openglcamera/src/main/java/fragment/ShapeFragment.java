package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sen.com.openglcamera.R;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/31 16:45
 * Des    :
 */

public class ShapeFragment extends Fragment {
    public View initParentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_change_shader, container, false);
        return rootView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_change_shader, container, false);
        Log.e("sen","onCreateView"+rootView);
        return rootView;
    }


}
