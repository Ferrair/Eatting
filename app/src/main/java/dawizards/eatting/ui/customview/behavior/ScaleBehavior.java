package dawizards.eatting.ui.customview.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by WQH on 2016/5/12  17:57.
 * Animation : Scale.
 */
@SuppressWarnings("unused")
public class ScaleBehavior extends VerticalBehavior {

    public ScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void actionHide(View target, ViewPropertyAnimator animationAttr) {
        animationAttr.scaleX(0.0F);
        animationAttr.scaleY(0.0F);
    }

    @Override
    protected void actionShow(View target, ViewPropertyAnimator animationAttr) {
        animationAttr.scaleX(1.0F);
        animationAttr.scaleY(1.0F);
    }
}
