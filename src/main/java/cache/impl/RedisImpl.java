package cache.impl;

import cache.interfaces.ICache;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RedisImpl<T> implements ICache<T> {


    private String namespace;
    private Jedis jedis;

    RedisImpl(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void put(String key, T val) {

    }

    @Override
    public void put(String key, T val, long time, TimeUnit unit) {

    }

    @Override
    public void invalidate(String key) {

    }

    @Nullable
    @Override
    public T get(String key) {
        System.out.println("GET CALLED with " + key);
        return null;
    }

    @Override
    public void forEach(Consumer<T> consumer) {

    }

    @Override
    public Map<String, T> filter(Predicate<T> predicate) {
        return null;
    }


    public Jedis getJedis() {
        return jedis;
    }
}
