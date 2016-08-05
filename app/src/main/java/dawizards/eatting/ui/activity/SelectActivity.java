package dawizards.eatting.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import dawizards.eatting.R;
import dawizards.eatting.bean.Select;
import dawizards.eatting.bean.User;
import dawizards.eatting.ui.adapter.SelectAdapter;
import dawizards.eatting.ui.adapter.event.LayoutState;
import dawizards.eatting.ui.base.ScrollActivity;
import dawizards.eatting.util.IntentUtil;

public class SelectActivity extends ScrollActivity {
    public static final int SELECT_SCHOOL = 0;
    public static final int SELECT_CANTEEN = 1;

    SelectAdapter mAdapter = null;
    private User.UserType mUserType;
    private static List<Select> mSchool = new ArrayList<>();
    private static List<Select> mCanteen = new ArrayList<>();

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

        mUserType = BmobUser.getCurrentUser(User.class).getType();
        if (type == SELECT_SCHOOL) {
            setTitleDynamic("选择学校");
            mAdapter = new SelectAdapter(this, mSchool);
            mAdapter.setOnItemClickListener(R.id.item_layout, (view, data) -> {
                if (mUserType == User.UserType.CANTEEN) {
                    IntentUtil.goToOtherActivity(SelectActivity.this, SelectActivity.class, "type", SELECT_CANTEEN);
                    finish();
                } else {
                    IntentUtil.goToOtherActivity(SelectActivity.this, SelectActivity.class, "type", SELECT_CANTEEN);
                }
            });
        }

        if (type == SELECT_CANTEEN) {
            setTitleDynamic("选择食堂");
            mAdapter = new SelectAdapter(this, mCanteen);
            mAdapter.setOnItemClickListener(R.id.item_layout, (view, data) -> IntentUtil.goToOtherActivity(SelectActivity.this, MainActivityCanteen.class));
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
        mSchool.add(new Select("电子科技大学", ""));

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
}
