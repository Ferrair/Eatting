package dawizards.eatting.mvp.presenter;

import android.content.Context;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dawizards.eatting.bean.Comment;

/**
 * Created by WQH on 2016/8/3  19:58.
 */
public class CommentPresenter {

    Context mContext;

    public CommentPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void queryBatch(BmobQuery<Comment> query, FindListener<Comment> mLoadView) {
        query.findObjects(mLoadView);
    }

    public void post(Comment aComment, SaveListener<String> mLoadView) {
        aComment.save(mLoadView);
    }
}
