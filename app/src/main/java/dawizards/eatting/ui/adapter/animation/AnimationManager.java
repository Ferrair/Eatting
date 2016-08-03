package dawizards.eatting.ui.adapter.animation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by WQH on 2016/5/17  21:41.
 */
public class AnimationManager {
    public static final int Alpha = 0;
    public static final int EnterInBottom = 1;
    public static final int EnterInRight = 2;

    @IntDef({Alpha, EnterInBottom, EnterInRight})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    public static BaseAnimation get(@AnimationType int type) {
        BaseAnimation animation;
        switch (type) {
            case Alpha:
                animation = new AlphaAnimation();
                break;
            case EnterInBottom:
                animation = new EnterInBottomAnimation();
                break;
            default:
                animation = new EnterInBottomAnimation();
                break;
            case EnterInRight:
                animation = new EnterInRightAnimation();
                break;
        }
        return animation;
    }
}
