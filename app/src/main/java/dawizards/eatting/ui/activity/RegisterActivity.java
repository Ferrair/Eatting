package dawizards.eatting.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.User;
import dawizards.eatting.ui.base.ToolbarActivity;
import dawizards.eatting.util.IntentUtil;
import dawizards.eatting.util.ToastUtil;

public class RegisterActivity extends ToolbarActivity {

    public static final String TAG = "RegisterActivity";

    @Bind(R.id.mEditTextUserRegisterName)
    protected EditText mEditTextUserRegisterName; //Username

    @Bind(R.id.mEditTextRegisterPassword)
    protected EditText mEditTextRegisterPassword; //Password

    @Bind(R.id.mEditTextRepeat)
    protected EditText mEditTextRepeat;   //Password Check
    MaterialDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new MaterialDialog.Builder(this).content(R.string.register_now).progress(true, 0).build();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_register;
    }


    @OnClick(R.id.mButtonRegister)
    protected void register() {
        String userName = mEditTextUserRegisterName.getText().toString();
        String password = mEditTextRegisterPassword.getText().toString();
        String passwordAgain = mEditTextRepeat.getText().toString();

        //if the input info is illegal,return
        if (illegalInputInfo(userName, password, passwordAgain))
            return;


        mDialog.show();

        User mUser = new User();
        mUser.setUsername(userName);
        mUser.setPassword(password);
        mUser.setType(User.UserType.STUDENT);
        mUser.signUp(new SaveListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (o == null) {
                    IntentUtil.goToOtherActivity(RegisterActivity.this, MainActivityStudent.class);
                    mDialog.dismiss();
                    ToastUtil.showToast(getString(R.string.register_success));
                } else {
                    mDialog.dismiss();
                    Log.e(TAG, "注册失败 -> " + e.getMessage());
                }
            }
        });
    }

    private boolean illegalInputInfo(String userName, String password, String passwordAgain) {
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showToast(getResources().getString(R.string.username_empty));
            return true;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(getResources().getString(R.string.user_password_empty));
            return true;
        }
        if (!passwordAgain.equals(password)) {
            ToastUtil.showToast(getResources().getString(R.string.password_not_equal));
            return true;
        }
        return false;
    }
}