package dawizards.eatting.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import dawizards.eatting.R;
import dawizards.eatting.bean.User;
import dawizards.eatting.ui.adapter.FragmentAdapter;
import dawizards.eatting.ui.customview.DrawerDelegate;

/**
 * Created by WQH on 2015/11/27 19:53.
 *
 * Handle business login, and get component via callback.
 */
public abstract class BaseMain extends ToolbarActivity implements DrawerDelegate.DrawerListener, OnContentCreate {

    private static final String TAG = "BaseMain";

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    /**
     * A Delegate that holds Left-Drawer.
     */
    private DrawerDelegate mDrawerDelegate;

    private List<Fragment> mContentList = new ArrayList<>();

    private List<Button> mButtonList = new ArrayList<>();

    private List<String> mTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        initContentView();
        initDrawer();
    }

    private void initComponents() {
        mContentList = onFragmentCreate();
        mButtonList = onButtonCreate();
        mTitleList = onTitleCreate();
    }

    private void initContentView() {
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mContentList));
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());

        for (int i = 0; i < mButtonList.size(); i++) {
            mButtonList.get(i).setOnClickListener(new ChangeStateListener(i));
        }

        selectState(0);
    }

    private void initDrawer() {
        mDrawerDelegate = new DrawerDelegate(this, mToolbar, this);
        mDrawerDelegate.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //IntentUtil.goToOtherActivity(this, SearchActivity.class);
                break;
        }
        return true;
    }

    /**
     * Dispatch click action on <code>Toolbar</code> on each ScrollFragment.
     */
    @Override
    protected void onToolbarClick() {
        ((ScrollFragment) mContentList.get(mViewPager.getCurrentItem())).onToolbarClick();
    }


    @Override
    protected boolean canBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        mDrawerDelegate.destroy();
        mDrawerDelegate = null;
        super.onDestroy();
    }

    protected void selectStateButton(int which) {
        for (int i = 0; i < mButtonList.size(); i++) {
            if (i == which)
                mButtonList.get(i).setSelected(true);
            else
                mButtonList.get(i).setSelected(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            mDrawerDelegate.setName(currentUser.getUsername());
            mDrawerDelegate.setEmail(currentUser.getType() == User.UserType.CANTEEN ? currentUser.getBelongCanteen() : "学生");
        }
    }

    protected void selectStateTitle(int which) {
        mToolbar.setTitle(mTitleList.get(which));
        setSupportActionBar(mToolbar);
    }


    public void selectStateTitle(String newTitle) {
        mToolbar.setTitle(newTitle);
        setSupportActionBar(mToolbar);
    }

    /**
     * Call when user scroll the ViewPager or click the Button.
     *
     * @param which which Fragment page is shown to user.
     */
    protected void selectState(int which) {
        /*
         * Change Button.
         */
        selectStateButton(which);
        /*
         * Change ViewPager.
         */
        mViewPager.setCurrentItem(which);
        /*
         * Change Title.
         */
        selectStateTitle(which);
        /*
         * Change Menu.
         */
        // mToolbar.setOnMenuItemClickListener(this);
        // 使得原来的菜单无效
        // invalidateOptionsMenu();
    }


    protected class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 2) {
                selectState(mViewPager.getCurrentItem());
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
        }
    }

    protected class ChangeStateListener implements View.OnClickListener {
        int index;

        public ChangeStateListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            selectState(index);
        }
    }


}
