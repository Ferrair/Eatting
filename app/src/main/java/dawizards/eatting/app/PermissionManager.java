package dawizards.eatting.app;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import dawizards.eatting.util.ToastUtil;

/**
 * Created by WQH on 2016/8/4  17:36.
 */
public class PermissionManager {
    /**
     * Request Permission.
     *
     * @return true -> user grant this permission.
     *
     * SDCard : Manifest.permission.WRITE_EXTERNAL_STORAGE
     * Camera : Manifest.permission.CAMERA
     */
    public static boolean grantPermission(Activity mActivity, String permission) {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                ToastUtil.showToast("没有权限");
                return false;
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{permission}, 10);
                return true;
            }
        }
        return true;
    }
}

