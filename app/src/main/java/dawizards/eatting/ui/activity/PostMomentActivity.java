package dawizards.eatting.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.Moment;
import dawizards.eatting.bean.User;
import dawizards.eatting.mvp.presenter.MomentPresenter;
import dawizards.eatting.ui.base.ToolbarActivity;
import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2016/8/3  21:35.
 */
public class PostMomentActivity extends ToolbarActivity {
    private static final String TAG = "PostMomentActivity";

    @Bind(R.id.moment_content)
    EditText mMomentContent;

    @Bind(R.id.rootView)
    View mRootView;

    MomentPresenter mMomentPresenter;

    @Override
    protected int layoutId() {
        return R.layout.activity_post_moment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMomentPresenter = new MomentPresenter();
    }


    private void postMoment() {
        String content = mMomentContent.getText().toString();
        if (!isValid(content))
            return;

        Moment mMoment = new Moment();
        mMoment.belongSchool = BmobUser.getCurrentUser(User.class).getBelongSchool();
        mMoment.content = content;
        mMoment.createdBy = BmobUser.getCurrentUser(User.class).getUsername();
        mMoment.userImageUrl = BmobUser.getCurrentUser(User.class).getUserImage();
        mMomentPresenter.post(mMoment, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    finish();
                } else {
                    Snackbar.make(mRootView, "由于某种原因，评论失败了", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValid(String text) {
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToast("不能为空！");
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    /**
     * When toolbar set menu.If too;bar can response the home button,MUST set it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post:
                postMoment();
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }
}
