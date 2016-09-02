package dawizards.eatting.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by WQH on 2016/4/11  18:47  18:56.
 * <p>
 * BaseFragment for all fragment, provides an abstract method that return a layout ID in /res/layout dir
 */
public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected Context mContext;

    public abstract int layoutId();

    //subclass can not override this method,and MUST override onActivityCreated()
    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(mContext).inflate(layoutId(), container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}

