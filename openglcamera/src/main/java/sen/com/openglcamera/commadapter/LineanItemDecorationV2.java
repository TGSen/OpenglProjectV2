package sen.com.openglcamera.commadapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/24.
 * Des    :
 */

public class LineanItemDecorationV2 extends  RecyclerView.ItemDecoration{

    private final Drawable mDrawable;

    public LineanItemDecorationV2(Context context, int resDrawableId) {
        mDrawable = ContextCompat.getDrawable(context,resDrawableId);
    }

    /**
     * 这个方法是留出分割线的位置
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //最后一个不需要分割线的
        int position = parent.getChildAdapterPosition(view);
        if (position!=0) {
            //不画第一个的顶部，也可以完成
            outRect.top =mDrawable.getIntrinsicHeight() ;
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right =parent.getWidth()- parent.getPaddingRight();
        for (int i = 1; i < count; i++) {
            //分割线的底部是在每个child的头部
            rect.bottom =parent.getChildAt(i).getTop();
            rect.top = rect.bottom -mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(rect);
            mDrawable.draw(c);
        }
    }

}
