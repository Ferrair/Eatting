package dawizards.eatting.mvp.presenter;


import android.content.Context;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import dawizards.eatting.bean.Food;

/**
 * Created by WQH on 2016/8/2  20:56.
 */
public class FoodPresenter {

    Context mContext;

    public FoodPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void queryBatch(BmobQuery<Food> query, FindListener<Food> mLoadView) {
        query.findObjects(mLoadView);
    }

    public void queryById(String id, QueryListener<Food> mLoadView) {
        new BmobQuery<Food>().getObject(id, mLoadView);
    }


    public void update(Food aFood, UpdateListener mLoadView) {
        aFood.update(mLoadView);
    }


    public void delete(Food aFood, UpdateListener mLoadView) {
        aFood.delete(mLoadView);
    }

    public void post() {
        Food aFood = new Food();
        aFood.name = "红烧代码";
        aFood.price = 123f;
        aFood.belongSchool = "电子科技大学";
        aFood.belongCanteen = "紫荆餐厅";
        aFood.imageUrl = "http://upload.jianshu.io/users/upload_avatars/1896125/325119cdd728.jpeg?imageMogr/thumbnail/90x90/quality/100";

        aFood.save();
    }
}
