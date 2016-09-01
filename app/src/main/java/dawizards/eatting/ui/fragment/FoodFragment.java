package dawizards.eatting.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.clans.fab.FloatingActionButton;
import com.litesuits.orm.LiteOrm;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.User;
import dawizards.eatting.manager.RxBus;
import dawizards.eatting.mvp.presenter.FoodPresenter;
import dawizards.eatting.ui.activity.ItemFoodActivity;
import dawizards.eatting.ui.activity.PostFoodActivity;
import dawizards.eatting.ui.adapter.FoodAdapter;
import dawizards.eatting.ui.base.BaseMain;
import dawizards.eatting.ui.base.ScrollFragment;
import dawizards.eatting.util.CollectionUtil;
import dawizards.eatting.util.IntentUtil;
import dawizards.eatting.manager.SharePreferenceManager;
import dawizards.eatting.util.TimeUtil;
import dawizards.eatting.util.ToastUtil;
import rx.Subscription;

import static dawizards.eatting.app.Constants.FOOD_ATTEND_NUM;

/**
 * Created by WQH on 2015/11/19 11:21.
 *
 * Show Food.Student can select which food they like.
 */
public class FoodFragment extends ScrollFragment {
    private static final String TAG = "FoodFragment";

    @Bind(R.id.fab)
    FloatingActionButton mFab;

    FoodPresenter mFoodPresenter;
    FoodAdapter mAdapter;
    User.UserType mType;
    BmobQuery<Food> mQuery;
    BmobDate mBmobDate;
    private static LiteOrm mLiteOrm;
    List<Food> mListDara;
    Subscription mBus;

    @Override
    public int layoutId() {
        return R.layout.fragment_food;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBus = RxBus.getDefault().toObservable(Food.class).subscribe(o -> {
            Log.i(TAG, "RxBus(Food) 收到了一条消息");
            mAdapter.addAtHead((Food) o);
            mRecyclerView.smoothScrollToPosition(0);
        });

        mType = BmobUser.getCurrentUser(User.class).getType();
        mAdapter = new FoodAdapter(mContext);
        mFoodPresenter = new FoodPresenter();
        mBmobDate = new BmobDate(TimeUtil.getTodayStart());
        mQuery = new BmobQuery<>();

        initData();

        setClickListener();
    }

    private void initData() {
        // mQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        mQuery.addWhereEqualTo("belongSchool", BmobUser.getCurrentUser(User.class).getBelongSchool());
        mQuery.addWhereGreaterThan("updatedAt", mBmobDate);
        if (mType == User.UserType.CANTEEN) {
            mQuery.addWhereEqualTo("belongCanteen", BmobUser.getCurrentUser(User.class).getBelongCanteen());
            if (mLiteOrm == null) {
                mLiteOrm = LiteOrm.newSingleInstance(mContext, "eatting.db");
                mLiteOrm.setDebugged(true);
            }
        }

        mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
    }

    /**
     * Set Listener to component.
     */
    private void setClickListener() {
        /*
         * Common Listener for all type user.
         * Go to ItemFoodActivity in order to get more info about this Food.
         */
        mAdapter.setOnItemClickListener(R.id.food_layout, (view, data) -> IntentUtil.goToOtherActivity(mContext, ItemFoodActivity.class, "itemFood", data));
        /*
         * Listener for Student.
         * Schedule this food.
         * Change Canteen.
         */
        if (mType == User.UserType.STUDENT) {
            mAdapter.setOnItemClickListener(R.id.food_schedule, (view, data) -> {
                // Schedule food here.
                scheduleFood(data);
            });

            String schools[] = CollectionUtil.getCanteenInCurrentSchool(mContext, BmobUser.getCurrentUser(User.class).getBelongSchool());
            mFab.setLabelText("选择食堂");
            mFab.setOnClickListener(view -> new AlertDialog.Builder(mContext).setItems(schools, (dialog, which) -> {

                ToastUtil.showToast("已切换到" + schools[which]);
                ((BaseMain) getActivity()).setTitleDynamic(schools[which]);
                mQuery = new BmobQuery<>();
                mQuery.addWhereGreaterThan("updatedAt", mBmobDate);
                mQuery.addWhereEqualTo("belongSchool", BmobUser.getCurrentUser(User.class).getBelongSchool());
                mQuery.addWhereEqualTo("belongCanteen", schools[which]);
                mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
            }).create().show());

        }

        /*
         * Listener for Canteen.
         * Delete or shelf this food.
         * Set Query Condition,(One day,One week,One mouth)
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

            mFab.setLabelText("上传菜品");
            mFab.setOnClickListener(view -> IntentUtil.goToOtherActivity(mContext, PostFoodActivity.class));
        }
    }

    /**
     * Log the attend num for current user,in order to select ingredient.
     */
    private void scheduleFood(Food data) {
        User currentUser = BmobUser.getCurrentUser(User.class);
        SharePreferenceManager mShareNum = SharePreferenceManager.newInstance(mContext, FOOD_ATTEND_NUM);
        String key = TimeUtil.today();

        if (data.isAttend(currentUser)) {
            data.removeAttend(currentUser);
            mShareNum.saveIntData(key, mShareNum.getIntData(key) - 1);
        } else {
            data.addAttend(currentUser);
            mShareNum.saveIntData(key, mShareNum.getIntData(key) + 1);
        }

        mAdapter.fill(data);
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
    public void onRefreshDelayed() {
        // mQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
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
        mListDara = data;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mBus.isUnsubscribed()) {
            mBus.unsubscribe();
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
                mAdapter.fill(toUpdate);
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
