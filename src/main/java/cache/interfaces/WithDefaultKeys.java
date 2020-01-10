package cache.interfaces;

import java.util.concurrent.TimeUnit;

public interface WithDefaultKeys<T> extends ICache<T> {

    void put(T val);

    void put(T val, long time, TimeUnit unit);
}
