import cache.impl.CacheProvider;
import cache.impl.RedisImpl;
import cache.interfaces.ExpirationPolicy;
import cache.interfaces.ICache;
import cache.interfaces.WithDefaultKeys;
import cache.plugins.Plugin;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.*;
public class AppCache {

    public static void main(String[] args) throws Exception {

      Kryo kryo = new Kryo();
      kryo.register(Pet.class);
      kryo.register(Person.class);
//      Pet<Person> obj = new Pet<>(new Person("asd", 123));

      Output out = new Output(new FileOutputStream("file.bin"));
//      kryo.writeObject(out, obj);
      out.close();

      Input in = new Input(new FileInputStream("file.bin"));
      Pet<Person> p2 = kryo.readObject(in, Pet.class);
        System.out.println();

    }



}
