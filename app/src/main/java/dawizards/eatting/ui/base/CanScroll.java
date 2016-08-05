package dawizards.eatting.ui.base;

import android.support.v4.widget.SwipeRefreshLayout;

import dawizards.eatting.ui.adapter.event.OnBottomListener;

/**
 * Created by WQH on 2016/5/17  16:03.
 *
 * If a RecyclerView can scroll,provided scroll-down and scroll-up method.
 */
public interface CanScroll extends SwipeRefreshLayout.OnRefreshListener, OnBottomListener {
    /**
     * A hook method in OnRefreshListener.onRefresh().
     * This method only do the load thing.
     */
    void onRefreshDelayed();

    /**
     * Whether can user fill when scroll-down at top.
     * The default is true.If this method return false,onRefreshDelayed() will make no sense.
     */
    boolean canRefresh();

}
