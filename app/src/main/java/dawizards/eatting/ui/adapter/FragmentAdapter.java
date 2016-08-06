package dawizards.eatting.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import dawizards.eatting.ui.base.BaseFragment;

/**
 * Created by WQH on 2016/4/11  17:24.
 * <p/>
 * A adapter for <code>Fragment<code/>
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragmentList;

    /**
     * @param fragmentList the list of the fragment which will be shown
     */
    public FragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).toString();
    }
}