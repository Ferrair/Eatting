package dawizards.eatting.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by WQH on 2016/8/5  20:03.
 */
public class Moment extends BmobObject {

    public String content;
    public String belongSchool;
    public String createdBy; // UserName
    public String userImageUrl;

    List<String> imageList = new ArrayList<>();
    List<User> likePeople = new ArrayList<>();


    /*
   * Operate on likePeople and likePeople.
   */
    public void removeAttend(User aUser) {
        likePeople.remove(aUser);
    }

    public void addAttend(User aUser) {
        likePeople.add(aUser);
    }

    public int getAttendPeopleNum() {
        return likePeople.size();
    }

    public boolean isAttend(User aUser) {
        return likePeople.contains(aUser);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Moment && ((Moment) other).getObjectId().equals(this.getObjectId());
    }
}
