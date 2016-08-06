package dawizards.eatting.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import cn.bmob.v3.BmobUser;
import dawizards.eatting.R;
import dawizards.eatting.bean.User;
import dawizards.eatting.ui.base.BaseActivity;
import dawizards.eatting.util.IntentUtil;


public class SplashActivity extends BaseActivity {

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (BmobUser.getCurrentUser(User.class) == null)
                IntentUtil.goToOtherActivity(SplashActivity.this, LoginActivity.class);
            else if (BmobUser.getCurrentUser(User.class).getType() == User.UserType.CANTEEN)
                IntentUtil.goToOtherActivity(SplashActivity.this, MainActivityCanteen.class);
            else
                IntentUtil.goToOtherActivity(SplashActivity.this, MainActivityStudent.class);
            finish();
        }
    };

    @Override
    protected int layoutId() {
        return R.layout.activity_splash;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Into Immersive mode.
         */
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        task();
        new Thread(() -> {
            handler.sendMessageDelayed(Message.obtain(), 2000);
        }).start();
    }

    private void task() {
    }

}