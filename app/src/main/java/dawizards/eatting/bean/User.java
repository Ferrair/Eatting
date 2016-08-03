package dawizards.eatting.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by WQH on 2016/8/1  20:25.
 */
public class User extends BmobUser {

    public enum UserType {CANTEEN, STUDENT}

    private UserType type;
    private String belongSchool;
    private String belongCanteen;
    private String userImage;

    public String getBelongCanteen() {
        return belongCanteen;
    }

    public void setBelongCanteen(String belongCanteen) {
        this.belongCanteen = belongCanteen;
    }

    public String getBelongSchool() {
        return belongSchool;
    }

    public void setBelongSchool(String belongSchool) {
        this.belongSchool = belongSchool;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof User && ((User) other).getObjectId().equals(getObjectId());
    }
}
