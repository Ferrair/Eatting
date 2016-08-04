package dawizards.eatting.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import dawizards.eatting.R;
import dawizards.eatting.ui.adapter.FoodAdapter;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.base.ScrollFragment;

/**
 * Created by WQH on 2016/8/4  19:01.
 */
public class DrawerFragment extends ScrollFragment {

    FoodAdapter mAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragmnet_drawer;
    }

    @Override
    public void onRefreshDelayed() {

    }

    @Override
    public void onLoadMore(int toToLoadPage) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mAdapter = new FoodAdapter(mContext);
        mAdapter.setLoadState(LayoutState.GONE);
    }
}
