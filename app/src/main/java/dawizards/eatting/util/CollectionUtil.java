package dawizards.eatting.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by WQH on 2016/5/9  19:42.
 */
public class CollectionUtil {

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
}
