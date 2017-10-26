package sen.com.openglcamera.commadapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/31 10:05
 * Des    :
 */

public class HeadFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {
    //这个是普通的列表adapter
    private RecyclerView.Adapter mAdapter;

    private SparseArray<View> mHeader, mFooter;

    private static int BASE_HEADER_KEY = 1000000;
    private static int BASE_FOOTER_KEY = 2000000;

    RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
           notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeRemoved(fromPosition, toPosition);
        }
    };

    public HeadFootAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
        mHeader = new SparseArray<>();
        mFooter = new SparseArray<>();

        mAdapter.registerAdapterDataObserver(adapterDataObserver);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeader.indexOfKey(viewType)>=0){
            return createHeaderFooterViewHodler(mHeader.get(viewType));
        }else if(mFooter.indexOfKey(viewType)>=0){
            return createHeaderFooterViewHodler(mFooter.get(viewType));
        }
        return mAdapter.onCreateViewHolder(parent,viewType);
    }
//    createHeaderFooterViewHodler
    private RecyclerView.ViewHolder createHeaderFooterViewHodler(View view) {
        return new RecyclerView.ViewHolder(view) {

        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //头部和底部都不用绑定数据
        // Header
        int numHeaders = mHeader.size();
        if (position < numHeaders ) {
            return;
        }
        // Adapter
         int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
               mAdapter.onBindViewHolder(holder,adjPosition);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeader.size() + mFooter.size();
    }

    //提供方法移除header and footer
    public void removeHeader(View view) {
        int posistion = mHeader.indexOfValue(view);
        if (posistion >= 0) {
            mHeader.removeAt(posistion);
        }
    }

    public void removeFooter(View view) {
        int posistion = mFooter.indexOfValue(view);
        if (posistion >= 0) {
            mFooter.removeAt(posistion);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Header
        int numHeaders = mHeader.size();
        if (position < numHeaders) {
            return mHeader.keyAt(position);
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        // Footer
        return mFooter.keyAt(adjPosition - adapterCount);
    }

    //提供方法add header and footer
    public void addHeader(View view) {
        if (mHeader.indexOfValue(view) == -1) {
            mHeader.put(BASE_HEADER_KEY++, view);
        }
    }

    public void addFooter(View view) {
        if (mFooter.indexOfValue(view) == -1) {
            mFooter.put(BASE_FOOTER_KEY++, view);
        }
    }




}
