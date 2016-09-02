package dawizards.eatting.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
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
import dawizards.eatting.manager.SharePreferenceManager;
import dawizards.eatting.mvp.presenter.FoodPresenter;
import dawizards.eatting.ui.adapter.FoodAdapter;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.base.ScrollActivity;
import dawizards.eatting.util.IntentUtil;
import dawizards.eatting.util.TimeUtil;
import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2015/11/12 17:28.
 * 用于查看原来的菜品 有一键发布的功能 并且可以自定义选择 添加过滤的功能
 */
public class OldFoodActivity extends ScrollActivity {
    @Bind(R.id.rootView)
    View mRootView;
    private static final String TAG = "OldFoodActivity";
    private FoodAdapter mAdapter;
    private FoodPresenter mFoodPresenter;
    private BmobQuery<Food> mQuery;
    private SharePreferenceManager mUtil;
    private Menu mMenu;
    private List<Food> mSelected = new ArrayList<>();
    private static final String[] mCondition = new String[]{"今天", "一周以内", "一个月以内"};
    /**
     * Check whether the user in edit-model.
     */
    private boolean edit = false;

    @Override
    protected int layoutId() {
        return R.layout.activity_old_food;
    }

    @Override
    public boolean canRefresh() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new FoodAdapter(this);
        mAdapter.setLoadState(LayoutState.GONE);
        mAdapter.setOnItemClickListener(R.id.food_select, new SelectListener());
        mAdapter.setOnItemClickListener(R.id.food_layout, (view, data) -> {
            if (!edit)
                IntentUtil.goToOtherActivity(OldFoodActivity.this, ItemFoodActivity.class, "itemFood", data);
        });
        /*
         * Query Data.
         */
        getCurrentCondition();
        mQuery = new BmobQuery<>();
        mFoodPresenter = new FoodPresenter();
        mQuery.addWhereEqualTo("belongSchool", BmobUser.getCurrentUser(User.class).getBelongSchool());
        mQuery.addWhereEqualTo("belongCanteen", BmobUser.getCurrentUser(User.class).getBelongCanteen());
        BmobDate bmobDate = map(mUtil.getStringData("mCondition").equals("null") ? "今天" : mUtil.getStringData("mCondition"));
        mQuery.addWhereGreaterThan("updatedAt", bmobDate);
        mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_food, menu);
        mMenu = menu;
        return true;
    }

    /**
     * 获得当前的查询条件
     */
    private void getCurrentCondition() {
        mUtil = SharePreferenceManager.newInstance(this, "eatting");
        ToastUtil.showToast("当前查询条件为 " + mUtil.getStringData("mCondition"));
    }

    /**
     * When toolbar set menu.If too;bar can response the home button,MUST set it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                /*
                 * Enter Edit Model.
                 */
                if (!edit) {
                    enterEdit();
                }
                /*
                 * Enter Upload Model.
                 */
                else {
                    upload();
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }

    private void upload() {
        if (mSelected.size() == 0) {
            Snackbar.make(mRootView, "您好像没有进行选择哦", Snackbar.LENGTH_SHORT);
            return;
        }
        new MaterialDialog.Builder(this)
                .content("您选择了" + mSelected.size() + "进行更新?")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive((dialog, which) -> Stream.of(mSelected).forEach(item -> {
                    item.release();
                    item.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                Log.e(TAG, e.getMessage());
                            } else {
                                RxBus.getDefault().post(item, RxBus.EVENT_ADD);
                                Log.i(TAG, "Success Upload");
                            }
                        }
                    });
                }))
                .show();
    }

    @OnClick(R.id.fab_condition)
    public void selectCondition() {
        new AlertDialog.Builder(this).setItems(mCondition, (dialog, which) -> {
            mUtil.saveStringData("mCondition", mCondition[which]);
            mQuery = new BmobQuery<>();
            BmobDate bmobDate = map(mCondition[which]);
            mQuery.addWhereEqualTo("belongSchool", BmobUser.getCurrentUser(User.class).getBelongSchool());
            mQuery.addWhereEqualTo("belongCanteen", BmobUser.getCurrentUser(User.class).getBelongCanteen());
            mQuery.addWhereGreaterThan("updatedAt", bmobDate);
            mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
            Snackbar.make(mRootView, "当前查询条件:" + mCondition[which], Snackbar.LENGTH_SHORT);
        }).create().show();
    }

    private BmobDate map(String condition) {
        BmobDate bmobDate = null;
        switch (condition) {
            case "今天":
                bmobDate = new BmobDate(TimeUtil.getTodayStart());
                break;
            case "一周以内":
                bmobDate = new BmobDate(TimeUtil.getWeekStart());
                break;
            case "一个月以内":
                bmobDate = new BmobDate(TimeUtil.getMonthStart());
                break;
        }
        return bmobDate;
    }

    @Override
    public void onBackPressed() {
        if (edit) {
            exitEdit();
        } else {
            super.onBackPressed();
        }
    }

    private void enterEdit() {
        edit = true;
        mAdapter.enterEdit();
        mRecyclerView.setAdapter(mAdapter);
        updateMenu();
    }

    private void exitEdit() {
        edit = false;
        mAdapter.exitEdit();
        mRecyclerView.setAdapter(mAdapter);
        updateMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRefreshDelayed() {
        mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
    }


    /**
     * Or we can use onPrepareOptionsMenu that call before we click the menuItem.
     */
    private void updateMenu() {
        MenuItem item = mMenu.findItem(R.id.edit);
        if (edit) {
            item.setTitle("上传");
        } else {
            item.setTitle("编辑");
        }

    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        // Do nothing.
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

    private class SelectListener implements dawizards.eatting.ui.adapter.event.OnItemClickListener<Food> {

        @Override
        public void onItemClick(View view, Food data) {
            // Todo : Can unselect.
            RadioButton mButton = (RadioButton) view;
            if (!mButton.isChecked()) {
                mButton.setChecked(false);
                mSelected.remove(data);
            } else {
                mButton.setChecked(true);
                mSelected.add(data);
            }
        }
    }
}
