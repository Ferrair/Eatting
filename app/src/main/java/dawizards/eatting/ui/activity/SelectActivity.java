package dawizards.eatting.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.Select;
import dawizards.eatting.bean.User;
import dawizards.eatting.mvp.presenter.UserPresenter;
import dawizards.eatting.ui.adapter.SelectAdapter;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.base.ScrollActivity;
import dawizards.eatting.util.IntentUtil;

public class SelectActivity extends ScrollActivity {
    public static final int SELECT_SCHOOL = 0;
    public static final int SELECT_CANTEEN = 1;
    private static final String TAG = "SelectActivity";

    SelectAdapter mAdapter = null;
    private User.UserType mUserType;
    private static List<Select> mSchool = new ArrayList<>();
    private static List<Select> mCanteen = new ArrayList<>();
    UserPresenter mUserPresenter;

    @Override
    protected int layoutId() {
        return R.layout.activity_select;
    }

    // Todo: Dialog here.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type", -1);
        if (type == -1)
            throw new NullPointerException(" ");

        mUserPresenter = new UserPresenter();
        mUserType = BmobUser.getCurrentUser(User.class).getType();
        if (type == SELECT_SCHOOL) {
            setTitleDynamic("选择学校");
            mAdapter = new SelectAdapter(this, mSchool);
            mAdapter.setOnItemClickListener(R.id.item_layout, (view, data) -> {
                /*
                 * Update The User.
                 */
                User mUser = new User();
                mUser.setBelongSchool(data.name);
                mUserPresenter.update(mUser, new UserUpdateListener(type));
            });
        }

        if (type == SELECT_CANTEEN) {
            setTitleDynamic("选择食堂");
            mAdapter = new SelectAdapter(this, mCanteen);
            mAdapter.setOnItemClickListener(R.id.item_layout, (view, data) ->
            {
                /*
                 * Update The User.
                 */
                User mUser = new User();
                mUser.setBelongCanteen(data.name);
                mUserPresenter.update(mUser, new UserUpdateListener(type));
            });
        }

        mAdapter.setLoadState(LayoutState.GONE);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean canRefresh() {
        return false;
    }

    @Override
    public void onRefreshDelayed() {
        //Do nothing.
    }

    @Override
    public void onLoadMore(int toToLoadPage) {
        //Do nothing.
    }

    static {
        mSchool.add(new Select("电子科技大学", "http://b.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=e7f19aa67cd98d1076d40b371904df33/8326cffc1e178a82702d73faf603738da977e853.jpg"));

        mCanteen.add(new Select("紫荆食堂", null));
        mCanteen.add(new Select("银桦食堂", null));
        mCanteen.add(new Select("芙蓉食堂", null));
        mCanteen.add(new Select("学子食堂", null));
        mCanteen.add(new Select("思源食堂", null));
        mCanteen.add(new Select("家园食堂", null));
        mCanteen.add(new Select("阳光食堂", null));
        mCanteen.add(new Select("风华食堂", null));
        mCanteen.add(new Select("万友食堂", null));
        mCanteen.add(new Select("桂苑食堂", null));
    }

    private class UserUpdateListener extends UpdateListener {
        int type;

        public UserUpdateListener(int type) {
            this.type = type;
        }

        @Override
        public void done(BmobException e) {
            if (e != null) {
                Log.e(TAG, e.getMessage());
                return;
            }
            if (type == SELECT_SCHOOL) {
                IntentUtil.goToOtherActivity(SelectActivity.this, SelectActivity.class, "type", SELECT_CANTEEN);
                finish();
            } else {
                if (mUserType == User.UserType.CANTEEN)
                    IntentUtil.goToOtherActivity(SelectActivity.this, MainActivityCanteen.class);
                else
                    IntentUtil.goToOtherActivity(SelectActivity.this, MainActivityStudent.class);
            }
        }
    }
}
