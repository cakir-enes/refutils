package cache.interfaces;

public interface IKeyGenerator<T> {

    String generateKeyFor(T val);
}
