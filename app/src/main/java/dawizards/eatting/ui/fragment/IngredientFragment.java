package dawizards.eatting.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import dawizards.eatting.R;
import dawizards.eatting.app.Constants;
import dawizards.eatting.bean.Ingredient;
import dawizards.eatting.bean.Moment;
import dawizards.eatting.bean.User;
import dawizards.eatting.manager.SharePreferenceManager;
import dawizards.eatting.mvp.presenter.IngredientPresenter;
import dawizards.eatting.mvp.presenter.MomentPresenter;
import dawizards.eatting.ui.adapter.IngredientAdapter;
import dawizards.eatting.ui.base.ScrollFragment;
import dawizards.eatting.util.CollectionUtil;
import dawizards.eatting.util.TimeUtil;
import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2016/8/5  14:10.
 */
public class IngredientFragment extends ScrollFragment {

    private static final String TAG = "IngredientFragment";

    User currentUser;
    IngredientAdapter mAdapter;
    IngredientPresenter mIngredientPresenter;
    BmobQuery<Ingredient> mQuery;
    List<Ingredient> mSelected = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentUser = BmobUser.getCurrentUser(User.class);
        mAdapter = new IngredientAdapter(mContext);
        mIngredientPresenter = new IngredientPresenter();

        initView();

        initData();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(R.id.item_layout, (view, data) -> {
            if (!mSelected.contains(data)) {
                view.findViewById(R.id.mIngredientSelector).setVisibility(View.VISIBLE);
                mSelected.add(data);
            } else {
                view.findViewById(R.id.mIngredientSelector).setVisibility(View.GONE);
                mSelected.remove(data);
            }
        });
    }

    private void initData() {
        if (currentUser.getType() == User.UserType.CANTEEN) {
            showContent(CollectionUtil.generateIngredients(mContext.getResources().getStringArray(R.array.ingredients), currentUser));
        }

        if (currentUser.getType() == User.UserType.STUDENT) {

            mQuery = new BmobQuery<>();
            // mQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            mQuery.addWhereEqualTo("belongSchool", BmobUser.getCurrentUser(User.class).getBelongSchool());
            mQuery.addWhereEqualTo("belongCanteen", BmobUser.getCurrentUser(User.class).getBelongCanteen() == null ? "紫荆食堂" : BmobUser.getCurrentUser(User.class).getBelongCanteen());
            mIngredientPresenter.queryBatch(mQuery, new IngredientFindListener());
        }
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_ingredient;
    }

    @Override
    public void onRefreshDelayed() {
        if (currentUser.getType() == User.UserType.STUDENT) {
            // mQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            mIngredientPresenter.queryBatch(mQuery, new IngredientFindListener());
        }
    }

    @Override
    public void onLoadMore(int toToLoadPage) {

    }

    /**
     * Show view by given data.
     */
    private void showContent(List<Ingredient> data) {
        mAdapter.fill(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class IngredientFindListener extends FindListener<Ingredient> {
        @Override
        public void done(List<Ingredient> list, BmobException e) {
            if (e == null) {
                showContent(list);
            } else {
                Log.e(TAG, "Fail-> " + e.getMessage());
            }
        }
    }

    @OnClick(R.id.fab)
    public void upload() {
        if (currentUser.getType() == User.UserType.CANTEEN) {
            postCanteen();
        } else {
            postStudent();
        }
    }

    private void postCanteen() {
        if (mSelected.size() == 0) {
            ToastUtil.showToast("您还没有选择啊");
            return;
        }
        new MaterialDialog.Builder(mContext)
                .content("您选择了" + mSelected.size() + "进行上传?\n学生可以根据您上传的菜品来进行选择")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive((dialog, which) -> {
                    Log.i(TAG, "Begin Save");
                    Stream.of(mSelected).forEach(item -> {
                        item.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null)
                                    Log.e(TAG, +e.getErrorCode() + e.getMessage());
                            }
                        });
                    });
                })
                .show();
    }

    private void postStudent() {
        if (mSelected.size() == 0) {
            ToastUtil.showToast("您还没有选择啊");
            return;
        }
        SharePreferenceManager mUtil = SharePreferenceManager.newInstance(getActivity(), Constants.FOOD_ATTEND_NUM);
        String key = TimeUtil.today();
        if (mUtil.getIntData(key) < Constants.CAN_SELECT_INGREDIENT) {
            ToastUtil.showToast("预定次数大于 " + Constants.CAN_SELECT_INGREDIENT + "次,才行啊," + "当前次数 " + mUtil.getIntData(key));
            return;
        }

        new MaterialDialog.Builder(mContext)
                .content("您选择了" + mSelected.size() + "进行自定义菜品?")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive((dialog, which) -> {
                    StringBuilder sb = new StringBuilder();
                    for (Ingredient ingredient : mSelected) {
                        sb.append(ingredient.name).append(" ");
                    }

                    Moment mMoment = new Moment();
                    mMoment.belongSchool = BmobUser.getCurrentUser(User.class).getBelongSchool();
                    mMoment.content = "我把这些食材" + sb.toString() + "组成了一道菜, 大家快啦看看!!";
                    mMoment.createdBy = BmobUser.getCurrentUser(User.class).getUsername();
                    mMoment.userImageUrl = BmobUser.getCurrentUser(User.class).getUserImage();
                    MomentPresenter mMomentPresenter = new MomentPresenter();
                    mMomentPresenter.post(mMoment, new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                            } else {
                                Snackbar.make(mRootView, "由于某种原因，评论失败了", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                })
                .show();


    }
}
