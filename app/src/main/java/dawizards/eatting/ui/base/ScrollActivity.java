package dawizards.eatting.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import butterknife.Bind;
import dawizards.eatting.R;

/**
 * Created by DA Wizards on 2015/11/10.
 *
 * A Activity that have a SwipeRefreshLayout(with layout id swipeRefreshLayout) and a RecyclerView(with layout id recyclerView)
 * NOTE: in subclass's XML file,those layout's is MUST be the same as above.
 */
public abstract class ScrollActivity extends ToolbarActivity implements CanScroll {
    private static final String TAG = "ScrollActivity";
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @Override
    public boolean canRefresh() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mRefreshLayout == null)
            throw new NullPointerException("This Activity must have a SwipeRefreshLayout with id:swipeRefreshLayout");
        if (mRecyclerView == null)
            throw new NullPointerException("This Activity must have a RecyclerView with id:recyclerView");

        mRefreshLayout.setEnabled(canRefresh());
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            onRefreshDelayed();
            if (mRefreshLayout != null) {
                mRefreshLayout.setRefreshing(false);
            } else {
                Log.e(TAG, "onRefresh: mRefreshLayout is null");
            }
        }, 2000);
    }

    /**
     * All the ScrollActivity can scroll to top when click the Toolbar.
     */
    @Override
    protected void onToolbarClick() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            Log.e(TAG, "onRefresh: mRefreshLayout is null");
        }
    }
}
