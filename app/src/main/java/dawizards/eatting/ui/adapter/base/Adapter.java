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
     * Update the Adapter by the given newDataList.
     * Means clear the last data.
     */
    void update(List<DataType> newDataList);

    /**
     * Update the Adapter by given newData.
     * Only update the place where to update.
     */
    void update(DataType newData);

    /**
     * Add newData to this Adapter.
     * instead of #update,this method do NOT clear the last data.
     * <p>
     * One more thing:the Adapter MUST judge whether the Adapter holds data before.
     */
    void addAll(List<DataType> newData);

    void addOne(DataType data, int position);

    void addAtTail(DataType data);

    void addAtHead(DataType data);

    void removeOne(DataType item);

    void removeOne(int position);

    void removeAll();

    List<DataType> getAllData();

    DataType getOne(int which);
}
