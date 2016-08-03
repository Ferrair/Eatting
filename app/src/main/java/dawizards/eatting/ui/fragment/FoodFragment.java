package dawizards.eatting.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.litesuits.orm.LiteOrm;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.FoodDB;
import dawizards.eatting.bean.User;
import dawizards.eatting.mvp.presenter.FoodPresenter;
import dawizards.eatting.ui.activity.ItemFoodActivity;
import dawizards.eatting.ui.adapter.FoodAdapter;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.base.ScrollFragment;
import dawizards.eatting.util.IntentUtil;

/**
 * Created by WQH on 2015/11/19 11:21.
 *
 * Show Food.Student can select which food they like.
 */
public class FoodFragment extends ScrollFragment {
    private static final String TAG = "FoodFragment";

    FoodPresenter mFoodPresenter;
    FoodAdapter mAdapter;
    User.UserType mType;
    BmobQuery<Food> mQuery;
    private static LiteOrm mLiteOrm;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mType = BmobUser.getCurrentUser(User.class).getType();
        mAdapter = new FoodAdapter(mContext);
        mAdapter.setLoadState(LayoutState.GONE);
        mFoodPresenter = new FoodPresenter(mContext);
        mQuery = new BmobQuery<>();

        if (mType == User.UserType.CANTEEN && mLiteOrm == null) {
            mLiteOrm = LiteOrm.newSingleInstance(mContext, "eatting.db");
            mLiteOrm.setDebugged(true);
        }

        mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());

        setClickListener();
    }

    private void setClickListener() {
        /*
         * Common Listener for all type user.
         * Go to ItemFoodActivity in order to get more info about this Food.
         */
        mAdapter.setOnItemClickListener(R.id.food_layout, (view, data) -> IntentUtil.goToOtherActivity(mContext, ItemFoodActivity.class, "itemFood", data));
        /*
         * Listener for Student.
         * Schedule this food.
         */
        if (mType == User.UserType.STUDENT) {
            mAdapter.setOnItemClickListener(R.id.food_schedule, (view, data) -> {
                // Schedule food here.
                scheduleFood(data);
            });

        }
        /*
         * Listener for Canteen.
         * Delete or shelf this food.
         */
        if (mType == User.UserType.CANTEEN) {
            mAdapter.setOnItemLongClickListener(R.id.food_layout, (view, data) -> new AlertDialog.Builder(mContext).setItems(new String[]{"删除菜品", "下架菜品"}, (dialog, which) -> {
                switch (which) {
                    case 0:
                        deleteFood(data);
                        break;
                    case 1:
                        shelfFood(data);
                        break;
                }
            }).create().show());
        }
    }

    private void scheduleFood(Food data) {
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (data.isAttend(currentUser)) {
            data.removeAttend(currentUser);
        } else {
            data.addAttend(currentUser);
        }

        mAdapter.update(data);
        mFoodPresenter.update(data, new FoodUpdateListener(data));

    }

    private void shelfFood(Food data) {
        mLiteOrm.save(data.convert());
        mFoodPresenter.delete(data, new FoodDeleteListener(data));
    }

    private void deleteFood(Food data) {
        mFoodPresenter.delete(data, new FoodDeleteListener(data));
    }

    @Override
    public int layoutId() {
        return R.layout.layout_list;
    }

    @Override
    public void onRefreshDelayed() {
        mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        // Do noting here.
    }

    /**
     * Show view by given data.
     */
    private void showContent(List<Food> data) {
        mAdapter.addAll(data);
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

    private class FoodUpdateListener extends UpdateListener {
        Food toUpdate;

        public FoodUpdateListener(Food toUpdate) {
            this.toUpdate = toUpdate;
        }

        @Override
        public void done(BmobException e) {
            if (e == null) {
                mAdapter.update(toUpdate);
            }
        }
    }

    private class FoodDeleteListener extends UpdateListener {
        Food toDelete;

        public FoodDeleteListener(Food toDelete) {
            this.toDelete = toDelete;
        }

        @Override
        public void done(BmobException e) {
            if (e == null) {
                mAdapter.removeOne(toDelete);
            }
        }
    }
}
