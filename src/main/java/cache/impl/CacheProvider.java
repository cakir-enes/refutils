package cache.impl;

import cache.interfaces.ICache;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CacheProvider {

    public static <T> ICache<T> withNamespace(String namespace) {
        return new RedisImpl<>(namespace);
    }

    public static <T> ICache<T> global() {
        return new RedisImpl<T>("##");
    }

    public static <ObjectType, CacheType extends ICache<ObjectType>> CacheType WithNamespace(Class<CacheType> cacheTypeClass, String namespace) throws NoSuchMethodException {
        Constructor<CacheType> constructor = cacheTypeClass.getConstructor(String.class);
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(namespace);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }
}
