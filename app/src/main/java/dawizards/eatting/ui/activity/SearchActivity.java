package dawizards.eatting.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.User;
import dawizards.eatting.mvp.presenter.FoodPresenter;
import dawizards.eatting.ui.adapter.FoodAdapter;
import dawizards.eatting.ui.base.OnSearch;
import dawizards.eatting.ui.base.ToolbarActivity;
import dawizards.eatting.util.TimeUtil;

public class SearchActivity extends ToolbarActivity implements OnSearch {
    private static final String TAG = "SearchActivity";
    @Bind(R.id.searchView)
    SearchView mSearchView;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    BmobQuery<Food> mQuery;
    BmobDate mBmobDate;
    FoodAdapter mAdapter;
    FoodPresenter mFoodPresenter = new FoodPresenter();

    @Override
    protected int layoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FoodAdapter(this);

        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new Handler().postDelayed(() -> onSearch(query), 1000);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onSearch(String newText) {
        mBmobDate = new BmobDate(TimeUtil.getTodayStart());
        mQuery = new BmobQuery<>();

        mQuery.addWhereEqualTo("belongSchool", BmobUser.getCurrentUser(User.class).getBelongSchool());
        mQuery.addWhereGreaterThan("updatedAt", mBmobDate);
        mQuery.addWhereEqualTo("belongCanteen", BmobUser.getCurrentUser(User.class).getBelongCanteen());
        mQuery.addWhereContains("name", newText);
        mFoodPresenter.queryBatch(mQuery, new FoodLoadListener());
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
