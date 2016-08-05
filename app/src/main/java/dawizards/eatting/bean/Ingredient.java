package dawizards.eatting.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by WQH on 2016/8/5  14:29.
 */
public class Ingredient extends BmobObject {
    public String name;
    public String imageUrl;
    public String belongSchool;
    public String belongCanteen;

    private List<User> attendPeople = new ArrayList<>(); //预定菜品的人数

    /*
    * Operate on attendPeople and likePeople.
    */
    public void removeAttend(User aUser) {
        attendPeople.remove(aUser);
    }

    public void addAttend(User aUser) {
        attendPeople.add(aUser);
    }


    public int getAttendPeopleNum() {
        return attendPeople.size();
    }

    public boolean isAttend(User aUser) {
        return attendPeople.contains(aUser);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Ingredient && ((Ingredient) other).name.equals(name);
    }
}
