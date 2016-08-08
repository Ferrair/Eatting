package dawizards.eatting.mvp.presenter;


import android.app.Dialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import dawizards.eatting.bean.User;
import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2016/8/7  13:13.
 */
public class UserPresenter {
    public void update(User aUser) {
        aUser.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                ToastUtil.showToast("Update Success");
            }
        });
    }

    public void update(User aUser, UpdateListener mLoadView) {
        aUser.update(BmobUser.getCurrentUser().getObjectId(), mLoadView);
    }

    public void signUp(User aUser, Dialog mDialog, SaveListener<User> mLoadView) {
        aUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User o, BmobException e) {
                if (e == null) {
                    login(aUser, mLoadView);
                } else {
                    mDialog.dismiss();
                    if (e.getErrorCode() == 202) {
                        ToastUtil.showToast("用户已存在");
                    }
                }
            }
        });
    }


    public void login(User aUser, SaveListener<User> mLoadView) {
        aUser.login(mLoadView);
    }
}
