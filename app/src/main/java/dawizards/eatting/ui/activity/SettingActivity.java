package dawizards.eatting.ui.activity;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import dawizards.eatting.R;
import dawizards.eatting.ui.base.ToolbarActivity;

/**
 * Created by WQH on 2015/11/13 15:54.
 */
public class SettingActivity extends ToolbarActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new SettingsFragment()).commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
