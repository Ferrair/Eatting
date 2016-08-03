package dawizards.eatting.util;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

/**
 * Created by WQH on 2015/11/30 14:51.
 * A Intent wrapper that can go to another Activity by those static method in this class.
 */
public class IntentUtil {

    public static void goToOtherActivity(Context mContext, Class<?> cla) {
        mContext.startActivity(new Intent(mContext, cla));
    }

    public static void goToOtherActivity(Context mContext, Intent aIntent) {
        mContext.startActivity(aIntent);
    }

    public static void goToOtherActivity(Context mContext, Class<?> cla, String name, Serializable mObject) {
        Intent intent = new Intent(mContext, cla);
        intent.putExtra(name, mObject);
        mContext.startActivity(intent);
    }

    public static void goToOtherActivity(Context mContext, Class<?> cla, String key, int value) {
        Intent intent = new Intent(mContext, cla);
        intent.putExtra(key, value);
        mContext.startActivity(intent);
    }


    public static void startService(Context mContext, Class<?> cla) {
        Intent intent = new Intent(mContext, cla);
        mContext.startService(intent);
    }
}