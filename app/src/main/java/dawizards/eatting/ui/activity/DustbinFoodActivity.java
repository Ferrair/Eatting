package dawizards.eatting.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.log.OrmLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import dawizards.eatting.R;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.FoodDB;
import dawizards.eatting.mvp.presenter.FoodPresenter;
import dawizards.eatting.ui.adapter.FoodAdapter;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.base.ScrollActivity;
import dawizards.eatting.util.CollectionUtil;

public class DustbinFoodActivity extends ScrollActivity {
    @Bind(R.id.rootView)
    View mRootView;

    private static final String TAG = "DustbinFoodActivity";
    private FoodAdapter mAdapter;
    private Menu mMenu;
    private List<Food> mSelected = new ArrayList<>();
    private boolean edit = false;
    private static LiteOrm mLiteOrm;

    @Override
    protected int layoutId() {
        return R.layout.activity_dustbin_food;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new FoodAdapter(this);
        mAdapter.setLoadState(LayoutState.GONE);
        mAdapter.setOnItemClickListener(R.id.food_select, new SelectListener());

        /*
         * Load data from custom database.
         */
        if (mLiteOrm == null) {
            mLiteOrm = LiteOrm.newSingleInstance(this, "eatting.db");
            mLiteOrm.setDebugged(true);
        }
        List<FoodDB> list = mLiteOrm.query(FoodDB.class);
        Log.i(TAG,list.size() + " ");
        showContent(CollectionUtil.convert(list));
    }

    @Override
    public void onRefreshDelayed() {

    }

    @Override
    public void onLoadMore(int toToLoadPage) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_food, menu);
        mMenu = menu;
        return true;
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

    //Todo : not working.
    private void upload() {
        if (mSelected.size() == 0) {
            Snackbar.make(mRootView, "您好像没有进行选择哦", Snackbar.LENGTH_SHORT);
            return;
        }
        new MaterialDialog.Builder(this)
                .content("您选择了" + mSelected.size() + "进行更新?")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive((dialog, which) -> {
                    Stream.of(mSelected).forEach(item -> {
                        item.release();
                        item.save();
                    });
                    finish();
                })
                .show();
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

    /**
     * Show view by given data.
     */
    private void showContent(List<Food> data) {
        mAdapter.update(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    private class SelectListener implements dawizards.eatting.ui.adapter.event.OnItemClickListener<Food> {

        @Override
        public void onItemClick(View view, Food data) {
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
