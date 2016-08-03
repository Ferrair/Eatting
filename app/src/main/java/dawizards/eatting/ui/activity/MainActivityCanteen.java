package dawizards.eatting.ui.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import dawizards.eatting.R;
import dawizards.eatting.ui.base.BaseMain;
import dawizards.eatting.ui.fragment.FoodFragment;

/**
 * MainActivity for Canteen.
 * Consistent of three Fragment: FoodFragment,IngredientsFragment，MomentsFragment。
 */
public class MainActivityCanteen extends BaseMain {

    @Bind(R.id.mButtonCanteen)
    Button mButtonCanteen;
    @Bind(R.id.mButtonData)
    Button mButtonData;
    @Bind(R.id.mButtonIngredient)
    Button mButtonIngredient;
    @Bind(R.id.mButtonMoments)
    Button mButtonMoments;

    @Override
    protected int layoutId() {
        return R.layout.activity_main_activity_canteen;
    }

    @Override
    public List<Fragment> onFragmentCreate() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FoodFragment());
        return fragmentList;
    }

    @Override
    public List<Button> onButtonCreate() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(mButtonCanteen);
        buttons.add(mButtonData);
        buttons.add(mButtonIngredient);
        buttons.add(mButtonMoments);
        return buttons;
    }

    @Override
    public List<String> onTitleCreate() {
        List<String> titles = new ArrayList<>();
        titles.add("今日菜品");
        titles.add("数据浏览");
        titles.add("发布食材");
        titles.add("学生动态");
        return titles;
    }

    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        return false;
    }

    @NonNull
    @Override
    public List<IDrawerItem> onDrawerMenuCreate() {
        List<IDrawerItem> drawerItemList = new ArrayList<>();
        drawerItemList.add(new PrimaryDrawerItem().withName("发布菜品").withIcon(ContextCompat.getDrawable(this, R.mipmap.ic_new)));
        drawerItemList.add(new PrimaryDrawerItem().withName("昔日菜品").withIcon(ContextCompat.getDrawable(this, R.mipmap.ic_yest)));
        drawerItemList.add(new PrimaryDrawerItem().withName("下架菜品").withIcon(ContextCompat.getDrawable(this, R.mipmap.ic_moved)));
        return drawerItemList;
    }

}
