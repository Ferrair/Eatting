package dawizards.eatting.ui.customview.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * Created by WQH on 2016/5/2  16:24.
 * <p>
 * A abstract VerticalBehavior that added this behavior that can disappear in scroll-down action,and show in scroll-up action.
 * Note:scroll-up and scroll-down action are associated with <code>app:layout_scrollFlags<code/> in <code>android.support.design.widget.CoordinatorLayout</code>
 */
public abstract class VerticalBehavior extends CoordinatorLayout.Behavior {


    private boolean isHided = false;
    private boolean isHiding = false;
    private boolean isShown = true;
    private boolean isShowing = false;
    private static final Interpolator interpolator = new FastOutSlowInInterpolator();
    private long duration = 700;

    public VerticalBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * In which method that the target view show/hide in user's eyes.
     * can scale or transfer.
     */
    protected abstract void actionHide(View target, ViewPropertyAnimator animationAttr);

    protected abstract void actionShow(View target, ViewPropertyAnimator animationAttr);

    /**
     * Call when the user scroll vertical(ViewCompat.SCROLL_AXIS_VERTICAL).
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /**
     * Called when a nested scroll in progress is about to fill, before the target has
     * consumed any of the scrolled distance.
     *
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param dx    the raw horizontal number of pixels that the user attempted to scroll
     * @param dy    the raw vertical number of pixels that the user attempted to scroll
     * @see {CoordinatorLayout.Behavior#onNestedScroll}
     */

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (!isHided && !isHiding && dy > 2) {
            hide(child);
        }
        if (!isShowing && !isShown && dy < -2) {
            show(child);
        }
    }


    private void hide(final View view) {
        isHiding = true;
        ViewPropertyAnimator animationAttr = view.animate()
                .setDuration(duration)
                .setInterpolator(interpolator)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        isHiding = false;
                        isHided = true;
                        isShown = false;
                        view.setVisibility(View.GONE);
                    }
                });
        actionHide(view, animationAttr);
        animationAttr.start();
    }

    private void show(View view) {
        isShowing = true;
        view.setVisibility(View.VISIBLE);
        ViewPropertyAnimator animationAttr = view.animate()
                .setDuration(duration)
                .setInterpolator(interpolator)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        isShowing = false;
                        isShown = true;
                        isHided = false;
                    }
                });

        actionShow(view, animationAttr);
        animationAttr.start();
    }
}
