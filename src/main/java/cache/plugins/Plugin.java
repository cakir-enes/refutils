package cache.plugins;

import cache.interfaces.*;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Plugin {

    public static <T> WithDefaultKeys<T> withDefaultKeys(ICache<T> backend, IKeyGenerator<T> IKeyGenerator) {

        return new WithDefaultKeys<T>() {

            @Override
            public void put(T val) {
                String key = IKeyGenerator.generateKeyFor(val);
                put(key, val);
            }

            @Override
            public void put(T val, long time, TimeUnit unit) {
                String key = IKeyGenerator.generateKeyFor(val);
                put(key, val, time, unit);
            }

            @Override
            public void put(String key, T val) {
                backend.put(key, val);
            }

            @Override
            public void put(String key, T val, long time, TimeUnit unit) {
                backend.put(key, val, time, unit);
            }

            @Override
            public void invalidate(String key) {
                backend.invalidate(key);
            }

            @Nullable
            @Override
            public T get(String key) {
                return backend.get(key);
            }

            @Override
            public void forEach(Consumer<T> consumer) {
                backend.forEach(consumer);
            }

            @Override
            public Map<String, T> filter(Predicate<T> predicate) {
                return backend.filter(predicate);
            }
        };
    }
}
