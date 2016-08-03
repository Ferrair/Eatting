package dawizards.eatting.ui.adapter.event;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by WQH on 2016/5/16  21:53.
 *
 * Load data from server in nextPage.
 */

public class LoadMoreLayoutListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "LoadMoreLayoutListener";
    OnBottomListener mOnBottomListener;
    private boolean loading = false;
    public static int currentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public LoadMoreLayoutListener(LinearLayoutManager linearLayoutManager, OnBottomListener mOnBottomListener) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.mOnBottomListener = mOnBottomListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = mLinearLayoutManager.getItemCount();
        int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (!loading && (lastVisibleItem > totalItemCount - 2) && dy > 0) {
            currentPage++;
            mOnBottomListener.onLoadMore(currentPage);
            loading = true;
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
