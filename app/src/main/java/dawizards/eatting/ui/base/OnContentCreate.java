package dawizards.eatting.ui.base;

import android.support.v4.app.Fragment;
import android.widget.Button;

import java.util.List;

/**
 * Created by WQH on 2016/8/2  20:39.
 * Create content in MainActivity.
 */
public interface OnContentCreate {

    /*
     * onFragmentCreate() : set Fragment content.
     * onButtonCreate() : set Button in the bottom.
     * onTitleCreate() : set Title in Toolbar
     *
     * Those methods are set basic component in this activity by subclass callback.
     */
      List<BaseFragment> onFragmentCreate();

      List<Button> onButtonCreate();

      List<String> onTitleCreate();
}
