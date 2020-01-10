package cache.interfaces;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;


public interface ICache<T>  {

    void put(String key, T val);

    void put(String key, T val, long time, TimeUnit unit);

    void invalidate(String key);

    @Nullable
    T get(String key);

    void forEach(Consumer<T> consumer);

    Map<String, T> filter(Predicate<T> predicate);
}
