package dawizards.eatting.manager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by WQH on 2016/2/29.
 *
 * Publisher : RxBus.getDefault().post();
 * Subject : RxBus.getDefault().toObservable(Food.class).subscribe(Subscription subscription ); (Destroy it in onDestroy)
 */
public class RxBus {
    private static RxBus INSTANCE;
    private final Subject<Object, Object> mBus;
    private List<String> mNameList = new ArrayList<>();

    public RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        if (INSTANCE == null) {
            synchronized (RxBus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxBus();
                }
            }
        }
        return INSTANCE;
    }

    public void post(Object event) {
        mBus.onNext(event);
    }

    public Observable toObservable(final Class<?> eventType) {
        return mBus.filter(eventType::isInstance).cast(eventType);
    }
}
