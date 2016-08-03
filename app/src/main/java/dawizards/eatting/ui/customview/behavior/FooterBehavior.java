package dawizards.eatting.ui.customview.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by WQH on 2016/5/2  16:24.
 * Animation : Transfer.
 */
@SuppressWarnings("unused")
public class FooterBehavior extends VerticalBehavior {

    public FooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void actionHide(View target, ViewPropertyAnimator animationAttr) {
        animationAttr.translationY(target.getHeight());
    }

    @Override
    protected void actionShow(View target, ViewPropertyAnimator animationAttr) {
        animationAttr.translationY(0);
    }


}

