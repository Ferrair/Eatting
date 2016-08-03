package dawizards.eatting.bean;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/*
 * Created by WQH on 2015/10/23 14:55.
 */
public class Food extends BmobObject {
    public String name;
    public Float price;
    public String belongSchool;
    public String belongCanteen;
    public String imageUrl;

    List<User> attendPeople = new ArrayList<>(); //预定菜品的人数
    List<User> likePeople = new ArrayList<>();   //点赞人数

    public void removeAttend(User aUser) {
        attendPeople.remove(aUser);
    }

    public void addAttend(User aUser) {
        attendPeople.add(aUser);
    }

    public int getLikePeopleNum() {
        return likePeople.size();
    }

    public boolean isAttend(User aUser) {
        return attendPeople.contains(aUser);
    }

    /**
     * Convert the Food.class to FoodDB.class.
     * Because of Food is extend BmobObject that can not assign the primary key in Database.
     */
    public FoodDB convert() {
        FoodDB foodDB = new FoodDB();
        foodDB.id = this.getObjectId();
        foodDB.name = this.name;
        foodDB.price = this.price;
        foodDB.belongSchool = this.belongSchool;
        foodDB.belongCanteen = this.belongCanteen;
        foodDB.imageUrl = this.imageUrl;
        return foodDB;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Food && ((Food) other).getObjectId().equals(getObjectId());
    }


    public boolean isLike(User aUser) {
        return likePeople.contains(aUser);
    }

    public void addLike(User aUser) {
        likePeople.add(aUser);
    }
}
