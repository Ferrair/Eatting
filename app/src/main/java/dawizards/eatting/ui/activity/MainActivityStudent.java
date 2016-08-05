package dawizards.eatting.ui.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import dawizards.eatting.R;
import dawizards.eatting.ui.base.BaseMain;
import dawizards.eatting.ui.fragment.FoodFragment;
import dawizards.eatting.ui.fragment.IngredientFragment;
import dawizards.eatting.ui.fragment.MomentFragment;
import dawizards.eatting.util.IntentUtil;

/**
 * MainActivity for Student.
 * Consistent of three Fragment: FoodFragment,IngredientsFragment，MomentsFragment。
 */
public class MainActivityStudent extends BaseMain {

    @Bind(R.id.mButtonCanteen)
    Button mButtonCanteen;
    @Bind(R.id.mButtonIngredient)
    Button mButtonIngredient;
    @Bind(R.id.mButtonMoments)
    Button mButtonMoments;

    @Override
    protected int layoutId() {
        return R.layout.activity_main_activity_student;
    }

    @Override
    public List<Fragment> onFragmentCreate() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FoodFragment());
        fragmentList.add(new IngredientFragment());
        fragmentList.add(new MomentFragment());
        return fragmentList;
    }

    @Override
    public List<Button> onButtonCreate() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(mButtonCanteen);
        buttons.add(mButtonIngredient);
        buttons.add(mButtonMoments);
        return buttons;
    }

    @Override
    public List<String> onTitleCreate() {
        List<String> titles = new ArrayList<>();
        titles.add("食堂");
        titles.add("食材");
        titles.add("动态");
        return titles;
    }

    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        switch (position) {
            case 3:
                IntentUtil.goToOtherActivity(this, PostMomentActivity.class);
                break;
            case 4:
                BmobUser.logOut();
                IntentUtil.goToOtherActivity(this, LoginActivity.class);
                break;
        }
        return false;
    }

    @NonNull
    @Override
    public List<IDrawerItem> onDrawerMenuCreate() {
        List<IDrawerItem> drawerItemList = new ArrayList<>();
        drawerItemList.add(new PrimaryDrawerItem().withName("我的预定").withIcon(ContextCompat.getDrawable(this, R.mipmap.ic_order)));
        drawerItemList.add(new PrimaryDrawerItem().withName("我的动态").withIcon(ContextCompat.getDrawable(this, R.mipmap.ic_msg)));
        drawerItemList.add(new PrimaryDrawerItem().withName("发布动态").withIcon(ContextCompat.getDrawable(this, R.mipmap.ic_msg)));
        drawerItemList.add(new PrimaryDrawerItem().withName("退出登录").withIcon(FontAwesome.Icon.faw_gavel));
        return drawerItemList;
    }

}
