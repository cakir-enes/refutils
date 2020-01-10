import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class App {
    private static int iii = 0;

    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {

        var ex = Executors.newSingleThreadExecutor();

        final Consumer<Integer> write = (Integer i) -> {
            ex.execute(() -> {
                iii++;
                for (int j = 0; j < 1000; j++) {
                    new Random().nextFloat();
                }
            });
        };


        for (int i = 0; i < 3; i++) {
            Thread w1 = new Thread(() -> {
                System.out.println("RUNNING ON: " + Thread.currentThread().getName());
                for (int j = 0; j < 500; j++) {
                write.accept(j);
//                    iii++;
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("FINISHED: " + Thread.currentThread().getName());
            }, "worker-" +i);
            w1.start();
        }

        TimeUnit.SECONDS.sleep(6);
        System.out.println("SIZE: " + iii);
        System.exit(0);

//        Class<?> aClass = Class.forName(Person.class.getName());
//        Utils utilz = Utils.createFor(aClass);
//
//        Utils utilzHey = Utils.createFor(Class.forName(Heyvan.class.getName()));
//
//        Utils.Listener<?> listener = new Utils.Listener<>() {
//            @Override
//            public void proccess(Object val) {
//                System.out.println(val);
//            }
//        };
//
//        utilz.subscribe(listener);
//
//        utilzHey.subscribe(listener);
//        utilzHey.unsub();
    }

    static class Heyvan {
        private long ll = new Random().nextInt();
    }

}
