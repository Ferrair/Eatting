package dawizards.eatting.ui.adapter.base;

import java.util.List;

/**
 * Created by WQH on 2016/4/11  21:21.
 * <p>
 * Interface for all <code>Adapter<code/>.
 * subclass MUST have a <code>List<DataType><code/> stores the data.
 */
public interface Adapter<DataType> {

    boolean isEmpty();

    /**
     * Fill the Adapter by the given newDataList.
     * Means clear the last data.
     */
    void fill(List<DataType> newDataList);

    /**
     * Update the Adapter by given newData.
     * Only fill the place where to fill.
     */
    void fill(DataType newData);


    void addOne(DataType data, int position);

    void addAtTail(DataType data);

    void addAtHead(DataType data);

    void removeOne(DataType item);

    void removeOne(int position);

    void removeAll();

    List<DataType> getAllData();

    DataType getOne(int which);
}
