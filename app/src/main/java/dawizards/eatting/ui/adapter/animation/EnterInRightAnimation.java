package dawizards.eatting.ui.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by WQH on 2016/5/17  21:49.
 */
public class EnterInRightAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0)};
    }
}
