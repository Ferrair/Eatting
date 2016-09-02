package dawizards.eatting.manager;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by WQH on 2016/2/29.
 *
 * Publisher : RxBus.getDefault().post(Object.class,tag);
 * Subject : RxBus.getDefault().toObservable(Food.class,tag).subscribe(Subscription subscription ); (Destroy it in onDestroy)
 *
 * Tag : distinguish EventType.
 */
public class RxBus {
    private static RxBus INSTANCE;
    private final Subject<Object, Object> mBus;

    public static final String EVENT_ADD = "add"; // default
    public static final String EVENT_UPDATE = "update";

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

    public void post(Object event, String tag) {
        mBus.onNext(RxBusObject.newInstance(event, tag));
    }

    public Observable<?> toObservable(final Class<?> eventType, final String tag) {
        return mBus.filter(aObject -> {
            if (!(aObject instanceof RxBusObject))
                return false;
            RxBusObject rxBusObject = (RxBusObject) aObject;
            return eventType.isInstance(rxBusObject.getObj()) && tag != null && tag.equals(rxBusObject.getTag());
        }).map(aObject -> {
            RxBusObject ro = (RxBusObject) aObject;
            return ro.getObj();
        });
    }

    private static class RxBusObject {
        private String tag;
        private Object obj;

        private RxBusObject(Object obj, String tag) {
            this.tag = tag;
            this.obj = obj;
        }

        public String getTag() {
            return tag;
        }

        public Object getObj() {
            return obj;
        }

        public static RxBusObject newInstance(Object obj, String tag) {
            return new RxBusObject(obj, tag);
        }
    }
}
