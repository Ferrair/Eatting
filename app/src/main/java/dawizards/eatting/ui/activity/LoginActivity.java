package dawizards.eatting.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import dawizards.eatting.R;
import dawizards.eatting.bean.User;
import dawizards.eatting.ui.base.ToolbarActivity;
import dawizards.eatting.util.IntentUtil;
import dawizards.eatting.util.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends ToolbarActivity {
    // UI references.
    @Bind(R.id.userName)
    protected EditText mUserNameView;

    @Bind(R.id.userPassword)
    protected EditText mPasswordView;

    @Bind(R.id.userAvatar)
    protected CircleImageView mHead;

    MaterialDialog mDialog;

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grantPermission();
        if (BmobUser.getCurrentUser(User.class) != null) {
            if (BmobUser.getCurrentUser(User.class).getType() == User.UserType.STUDENT)
                IntentUtil.goToOtherActivity(this, MainActivityStudent.class);
            else
                IntentUtil.goToOtherActivity(this, MainActivityCanteen.class);
            finish();
        }
        //Set SoftKey
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mUserNameView.addTextChangedListener(new ShowAvatar());
        mDialog = new MaterialDialog.Builder(this).content(R.string.login_now).progress(true, 0).build();
    }

    private boolean grantPermission() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                ToastUtil.showToast("Give me Permission for Phone State");
                return false;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 10);
                return true;
            }
        }
        return true;
    }

    private void attemptLogin() {

        String userName = mUserNameView.getText().toString();
        String userPassword = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showToast(getResources().getString(R.string.username_empty));
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            ToastUtil.showToast(getResources().getString(R.string.user_password_empty));
            return;
        }
        mDialog.show();
        BmobUser.loginByAccount(userName, userPassword, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    ToastUtil.showToast(getString(R.string.login_success));
                    // if (user.getBelongSchool() == null)
                    //     IntentUtil.goToOtherActivity(LoginActivity.this, SelectOptionsActivity.class, Constants.SELECT_TYPE, Constants.SELECT_SCHOOL);
                    if (user.getType() == User.UserType.STUDENT)
                        IntentUtil.goToOtherActivity(LoginActivity.this, MainActivityStudent.class);
                    else if (user.getType() == User.UserType.CANTEEN) {
                        IntentUtil.goToOtherActivity(LoginActivity.this, MainActivityCanteen.class);
                    }
                    IntentUtil.goToOtherActivity(LoginActivity.this, MainActivityStudent.class);
                    mDialog.dismiss();
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick(R.id.go_register)
    public void gotoRegister(View v) {
        IntentUtil.goToOtherActivity(this, RegisterActivity.class);
    }

    @OnClick(R.id.loginButton)
    public void login(View v) {
        attemptLogin();
    }

    private class ShowAvatar implements TextWatcher {

        @Override
        public void afterTextChanged(Editable userName) {
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("username", userName.toString());
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (list != null && list.size() > 0) {
                        String avatarUrl = list.get(0).getUserImage();
                        if (avatarUrl != null)
                            ImageLoader.getInstance().displayImage(avatarUrl, mHead);
                        else
                            mHead.setImageResource(R.mipmap.head);
                    }
                }
            });
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count == 0)
                ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.head, mHead);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
    }
}

