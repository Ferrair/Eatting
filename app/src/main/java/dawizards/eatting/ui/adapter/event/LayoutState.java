package dawizards.eatting.ui.adapter.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by WQH on 2016/5/23  15:47.
 */
public interface LayoutState {
    int LOAD = 0;
    int FINISHED = 1;
    int GONE = 2;

    @IntDef({LOAD, FINISHED, GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {
    }
}
