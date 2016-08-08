package dawizards.eatting.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.bmob.v3.BmobUser;
import dawizards.eatting.R;
import dawizards.eatting.bean.User;
import dawizards.eatting.util.IntentUtil;

/**
 * Full Screen Mode.(Immersive Mode,hide status bar nad navigation bar)
 * See Manifest.xml.
 */
public class SplashActivity extends Activity {

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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        task();
        new Thread(() -> {
            handler.sendMessageDelayed(Message.obtain(), 2000);
        }).start();
    }

    private void task() {
    }

}