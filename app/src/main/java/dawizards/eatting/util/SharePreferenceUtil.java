package dawizards.eatting.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * 存储一些比较少量的信息 比如说设置信息
 * 提供一下几种存储方式:
 * 1.存储String类型
 * 2.存储int类型
 * 3.存储boolean类型
 * <p>
 * 使用方式:
 * SharePreferenceUtil s = SharePreferenceUtil.newInstance(Context context,String tableName);
 * 存储数据时:  s.saveBooleanData(String itemName,boolean status);
 * 取出数据时:  s.getBooleanData(String itemName);
 */
public class SharePreferenceUtil {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public final static String STRING_NULL = "null";

    private SharePreferenceUtil(Context context, String tableName) {
        mSharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }


    public static SharePreferenceUtil newInstance(Context context, String tableName) {
        return new SharePreferenceUtil(context, tableName);
    }

    public void saveBooleanData(String itemName, boolean status) {
        mEditor.putBoolean(itemName, status).commit();
    }

    public boolean getBooleanData(String itemName, boolean defaultValue) {
        return mSharedPreferences.getBoolean(itemName, defaultValue);
    }

    public void saveStringData(String itemName, String itemString) {
        mEditor.putString(itemName, itemString).commit();
    }

    /**
     * @return 默认返回"null"
     */
    public String getStringData(String itemName) {
        return mSharedPreferences.getString(itemName, STRING_NULL);
    }

    public void saveIntData(String itemName, int number) {
        mEditor.putInt(itemName, number).commit();
    }

    /**
     * @return 默认返回 0
     */
    public int getIntData(String itemName) {
        return mSharedPreferences.getInt(itemName, 0);
    }

    public void saveStringSetData(String key, Set<String> mSet) {
        mEditor.putStringSet(key, mSet).commit();
    }

    /**
     * @return 默认返回 null
     */
    public Set<String> getStringSet(String key) {
        return mSharedPreferences.getStringSet(key, null);
    }


}
