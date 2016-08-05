package dawizards.eatting.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.User;
import dawizards.eatting.mvp.presenter.FoodPresenter;
import dawizards.eatting.ui.adapter.DataStatisticsAdapter;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.base.ScrollFragment;
import dawizards.eatting.ui.customview.DividerItemDecoration;
import dawizards.eatting.util.TimeUtil;

/**
 * Created by WQH on 2015/11/15 18:27.
 * Data Statistics for canteen.
 */
public class DataStatisticsFragment extends ScrollFragment {

    private static final String TAG = "DataStatisticsFragment";
    DataStatisticsAdapter mAdapter;
    FoodPresenter mFoodPresenter;
    BmobQuery<Food> mQuery;
    BmobDate mBmobDate;

    @Override
    public int layoutId() {
        return R.layout.fragment_data;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new DataStatisticsAdapter(mContext);
        mAdapter.setLoadState(LayoutState.GONE);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mFoodPresenter = new FoodPresenter(mContext);
        mBmobDate = new BmobDate(TimeUtil.getTodayStart());
        mQuery = new BmobQuery<>();
        mQuery.addWhereEqualTo("belongSchool", BmobUser.getCurrentUser(User.class).getBelongSchool());
        mQuery.addWhereEqualTo("belongCanteen", BmobUser.getCurrentUser(User.class).getBelongCanteen());
        mQuery.addWhereGreaterThan("updatedAt", mBmobDate);
        mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
    }

    @Override
    public void onRefreshDelayed() {
        mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        //Do nothing.
    }

    /**
     * Show view by given data.
     */
    private void showContent(List<Food> data) {
        mAdapter.fill(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class FoodLoadListener extends FindListener<Food> {
        @Override
        public void done(List<Food> list, BmobException e) {
            if (e == null) {
                showContent(list);
            } else {
                Log.e(TAG, "Fail-> " + e.getMessage());
            }
        }
    }
}
