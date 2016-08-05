package dawizards.eatting.mvp.presenter;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import dawizards.eatting.bean.Moment;

/**
 * Created by WQH on 2016/8/5  20:31.
 */
public class MomentPresenter {

    public void queryBatch(BmobQuery<Moment> query, FindListener<Moment> mLoadView) {
        query.findObjects(mLoadView);
    }

    public void post(Moment aMoment, SaveListener<String> mLoadView) {
        aMoment.save(mLoadView);
    }
}
