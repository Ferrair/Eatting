package dawizards.eatting.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dawizards.eatting.R;
import dawizards.eatting.app.Constants;
import dawizards.eatting.bean.Food;
import dawizards.eatting.bean.FoodDB;
import dawizards.eatting.bean.Ingredient;
import dawizards.eatting.bean.User;

/**
 * Created by WQH on 2016/5/9  19:42.
 */
public class CollectionUtil {

    private static final String TAG = "CollectionUtil";

    /**
     * Make Array data to List.(DO NOT use Arrays.asList)
     */
    @SafeVarargs
    public static <T> List<T> asList(T... arrayData) {
        List<T> mList = new ArrayList<>();
        Collections.addAll(mList, arrayData);
        return mList;
    }


    public static <T> void addAllDistinct(List<T> src, List<T> des) {
        for (T item : des) {
            if (!src.contains(item)) {
                src.add(item);
            }
        }
    }

    public static <T> List<T> asList(Collection<T> data) {
        List<T> list = new ArrayList<>();
        list.addAll(data);
        return list;
    }


    /**
     * Return all canteen by given currentSchool.
     */
    @NonNull
    public static String[] getCanteenInCurrentSchool(Context mContext, String currentSchool) {
        String[] mSchool = mContext.getResources().getStringArray(R.array.school);
        int mSchoolIndex = 0;
        for (String itemSchool : mSchool) {
            if (itemSchool.equals(currentSchool))
                break;
            ++mSchoolIndex;
        }
        return mContext.getResources().getStringArray(R.array.canteen)[mSchoolIndex].split("-");
    }

    //分离","把(name,url)变为 SimpleBean
    public static List<Ingredient> generateIngredients(String[] dis, User currentUser) {
        List<Ingredient> mList = new ArrayList<>();
        Ingredient mIngredient;
        for (String item : dis) {
            mIngredient = new Ingredient();
            String[] splitString = item.split("`");
            mIngredient.name = splitString[0];
            mIngredient.belongSchool = currentUser.getBelongSchool();
            mIngredient.belongCanteen = currentUser.getBelongCanteen();

            if (splitString.length > 1) {
                mIngredient.imageUrl = splitString[1];
            } else
                mIngredient.imageUrl = null;

            mList.add(mIngredient);
        }
        return mList;
    }


    public static List<Food> convert(List<FoodDB> rawData) {
        List<Food> foods = new ArrayList<>();
        for (FoodDB foodDB : rawData) {
            Food food = new Food();
            foods.add(food.convert(foodDB));
        }
        return foods;
    }
}
