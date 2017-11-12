package sen.com.openglcamera.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/10 19:22
 * Des    : 这个View 是处理百分比的View
 *
 */

public class HandlePercentView extends View{

    public HandlePercentView(Context context) {
        super(context);
    }

    public HandlePercentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HandlePercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }


}
