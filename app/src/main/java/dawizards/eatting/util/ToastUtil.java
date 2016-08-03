package dawizards.eatting.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Context mContext;

    public static void showToast(final String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }


    public static void register(Context context) {
        mContext = context.getApplicationContext();
    }

}