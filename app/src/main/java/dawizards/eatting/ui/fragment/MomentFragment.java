package dawizards.eatting.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.Moment;
import dawizards.eatting.bean.User;
import dawizards.eatting.mvp.presenter.MomentPresenter;
import dawizards.eatting.ui.adapter.MomentAdapter;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.base.ScrollFragment;
import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2016/8/5  20:47.
 * Todo : UI
 */
public class MomentFragment extends ScrollFragment {

    private static final String TAG = "MomentFragment";
    User currentUser;
    MomentAdapter mAdapter;
    MomentPresenter mMomentPresenter;
    BmobQuery<Moment> mQuery;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new MomentAdapter(mContext);
        mAdapter.setLoadState(LayoutState.GONE);
        currentUser = BmobUser.getCurrentUser(User.class);

        mAdapter.setOnItemClickListener(R.id.moment_like, (view, data) -> {
            if (data.isAttend(currentUser)) {
                ToastUtil.showToast("你已经赞过了");
                return;
            }
            data.addAttend(currentUser);
            mAdapter.fill(data);
            data.save();
        });

        mMomentPresenter = new MomentPresenter();
        mQuery = new BmobQuery<>();
        mQuery = new BmobQuery<>();
        mQuery.addWhereEqualTo("belongSchool", currentUser.getBelongSchool());
        mMomentPresenter.queryBatch(mQuery, new MomentFindListener());
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        // Do nothing.
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_moment;
    }

    @Override
    public void onRefreshDelayed() {
        mMomentPresenter.queryBatch(mQuery, new MomentFindListener());
    }

    /**
     * Show view by given data.
     */
    private void showContent(List<Moment> data) {
        mAdapter.fill(data);
        mRecyclerView.setAdapter(mAdapter);
    }


    private class MomentFindListener extends FindListener<Moment> {
        @Override
        public void done(List<Moment> list, BmobException e) {
            if (e == null) {
                showContent(list);
            } else {
                Log.e(TAG, "Fail-> " + e.getMessage());
            }
        }
    }
}
