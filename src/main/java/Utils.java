import java.lang.reflect.Constructor;

public class Utils<R> {
    private Listener<R> l;
    public static<T> Utils<T> createFor(Class<T> clazz) {
        return new Utils<T>();
    }
    public void subscribe(Listener<R> l) {
//        Constructor<R> c = Class.forName()
//        l.proccess(((R) new Person("avsdf", 123123)));
        this.l = l;
    }

    public void unsub() {
        System.out.println("REMOVING LISTNER");
        l = null;
    }

    public interface Listener<T> {
        public void proccess(T val);
    }
}

