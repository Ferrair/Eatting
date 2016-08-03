package dawizards.eatting.bean;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by WQH on 2016/8/3  18:13.
 */
@Table("food")
public class FoodDB {
    @PrimaryKey(AssignType.BY_MYSELF)
    public String id;
    public String name;
    public Float price;
    public String belongSchool;
    public String belongCanteen;
    public String imageUrl;
}