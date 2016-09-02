package dawizards.eatting.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import butterknife.Bind;
import dawizards.eatting.R;

/**
 * Created by WQH on 2016/5/3  21:23.
 *
 * A Fragment that have a SwipeRefreshLayout(with layout id swipeRefreshLayout) and a RecyclerView(with layout id recyclerView)
 * NOTE: in subclass's XML file,those layout's is MUST be the same as above.
 */
public abstract class ScrollFragment extends BaseFragment implements CanScroll {
    private static final String TAG = "ScrollFragment";

    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @Override
    public boolean canRefresh() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mRefreshLayout == null)
            throw new NullPointerException("This Fragment must have a SwipeRefreshLayout with id:swipeRefreshLayout");
        if (mRecyclerView == null)
            throw new NullPointerException("This Fragment must have a RecyclerView with id:recyclerView");

        mRefreshLayout.setEnabled(canRefresh());
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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

    //Call in Activity
    public void onToolbarClick() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            Log.e(TAG, "onRefresh: mRefreshLayout is null");
        }
    }
}
