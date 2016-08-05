package dawizards.eatting.mvp.presenter;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import dawizards.eatting.bean.Ingredient;

/**
 * Created by WQH on 2016/8/5  14:36.
 */
public class IngredientPresenter {

    public void queryBatch(BmobQuery<Ingredient> query, FindListener<Ingredient> mLoadView) {
        query.findObjects(mLoadView);
    }

    public void post(Ingredient aIngredient, SaveListener<String> mLoadView) {
        aIngredient.save(mLoadView);
    }
}
