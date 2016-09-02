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
import dawizards.eatting.bean.Comment;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.User;
import dawizards.eatting.manager.RxBus;
import dawizards.eatting.mvp.presenter.CommentPresenter;
import dawizards.eatting.ui.base.ToolbarActivity;
import dawizards.eatting.util.IntentUtil;
import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2016/8/3  21:35.
 */
public class PostCommentActivity extends ToolbarActivity {
    private static final String TAG = "PostCommentActivity";

    @Bind(R.id.comment_content)
    EditText mCommentContent;

    @Bind(R.id.rootView)
    View mRootView;

    Food mFoodItem;
    CommentPresenter mCommentPresenter;

    @Override
    protected int layoutId() {
        return R.layout.activity_post_comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFoodItem = (Food) getIntent().getSerializableExtra("itemFood");
        mCommentPresenter = new CommentPresenter();
    }


    private void postComment() {
        String comment = mCommentContent.getText().toString();
        if (!isValid(comment))
            return;

        Comment aComment = new Comment();
        aComment.userUrl = BmobUser.getCurrentUser(User.class).getUserImage();
        aComment.commentTo = mFoodItem.getObjectId();
        aComment.commentBy = BmobUser.getCurrentUser(User.class).getUsername();
        aComment.content = comment;
        mCommentPresenter.post(aComment, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    RxBus.getDefault().post(aComment,RxBus.EVENT_UPDATE);
                    finish();
                } else {
                    Snackbar.make(mRootView, "由于某种原因，评论失败了", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean isValid(String text) {
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToast("评论不能为空！");
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
                postComment();
                item.setEnabled(false);
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }
}
