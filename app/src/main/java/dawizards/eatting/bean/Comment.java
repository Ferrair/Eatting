package dawizards.eatting.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by WQH on 2016/8/3  19:46.
 */
public class Comment extends BmobObject {
    public String commentTo; //评论的Food的objectId
    public String commentBy; //评论所属的User的username
    public String userUrl; //评论所属的User的avatarUrl
    public String content;
}
