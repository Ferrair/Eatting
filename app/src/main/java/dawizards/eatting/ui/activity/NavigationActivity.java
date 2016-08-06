package dawizards.eatting.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.SimplePagerFragment;
import com.cleveroad.slidingtutorial.TransformItem;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dawizards.eatting.R;
import dawizards.eatting.manager.SharePreferenceManager;
import dawizards.eatting.ui.base.BaseActivity;
import dawizards.eatting.util.IntentUtil;

public class NavigationActivity extends BaseActivity {


    private static SharePreferenceManager mUtil;

    @Override
    protected int layoutId() {
        return R.layout.activity_navigation;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUtil = SharePreferenceManager.newInstance(this, "eatting");

        if (!mUtil.getBooleanData("first", true)) {
            IntentUtil.goToOtherActivity(this, SplashActivity.class);
            finish();
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new CustomPresentationPagerFragment()).commit();
    }


    public static class CustomPresentationPagerFragment extends SimplePagerFragment {
        @Override
        protected int getPagesCount() {
            return 3;
        }

        @Override
        protected PageFragment getPage(int position) {
            if (position == 0)
                return new FirstCustomPageFragment();
            if (position == 1)
                return new SecondCustomPageFragment();
            if (position == 2)
                return new ThirdCustomPageFragment();

            throw new IllegalArgumentException("Unknown position: " + position);
        }

        @ColorInt
        @Override
        protected int getPageColor(int position) {
            if (position == 0)
                return ContextCompat.getColor(getActivity(), android.R.color.holo_orange_dark);
            if (position == 1)
                return ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark);
            if (position == 2)
                return ContextCompat.getColor(getActivity(), android.R.color.holo_blue_dark);
            return Color.TRANSPARENT;
        }

        @Override
        protected boolean isInfiniteScrollEnabled() {
            return true;
        }

        @Override
        protected boolean onSkipButtonClicked(View skipButton) {
            mUtil.saveBooleanData("first", false);
            IntentUtil.goToOtherActivity(getActivity(), SplashActivity.class);
            return true;
        }
    }

    public static class FirstCustomPageFragment extends PageFragment {

        @Override
        protected int getLayoutResId() {
            return R.layout.fragment_page_first;
        }

        @Override
        protected TransformItem[] provideTransformItems() {
            return new TransformItem[]{
                    new TransformItem(R.id.mImage, true, 20),

            };
        }
    }

    public static class SecondCustomPageFragment extends PageFragment {

        @Override
        protected int getLayoutResId() {
            return R.layout.fragment_page_second;
        }

        @Override
        protected TransformItem[] provideTransformItems() {
            return new TransformItem[]{
                    new TransformItem(R.id.mImage, false, 6),
            };
        }
    }

    public static class ThirdCustomPageFragment extends PageFragment {

        @OnClick(R.id.go)
        public void go() {
            mUtil.saveBooleanData("first", false);
            IntentUtil.goToOtherActivity(getActivity(), SplashActivity.class);
        }

        @Override
        protected int getLayoutResId() {
            return R.layout.fragment_page_third;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        protected TransformItem[] provideTransformItems() {
            return new TransformItem[]{
                    new TransformItem(R.id.mImage, true, 8),
            };
        }
    }
}
