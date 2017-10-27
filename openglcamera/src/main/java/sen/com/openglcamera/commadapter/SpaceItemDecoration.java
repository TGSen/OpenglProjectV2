package sen.com.openglcamera.commadapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/23.
 * Des    :只是设置两个item 的空隙
 */

public class SpaceItemDecoration extends  RecyclerView.ItemDecoration {
    private  int count;
    int mSpace ;

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int space, int count) {
        this.mSpace = space;
        this.count = count;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = count;
        int pos = parent.getChildAdapterPosition(view);
        outRect.left = 0;
        outRect.top = 0;
        outRect.bottom = 0;


        if (pos != (itemCount -1)) {
            outRect.right = mSpace;
        } else {
            outRect.right = 0;
        }
    }
}
