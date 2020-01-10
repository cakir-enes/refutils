public class AppWrap {
    public static void main(String[] args) {

    }

    public static <T> void doSth(Wrapper<Pet<T>> w) {
        System.out.println(w);
    }
}
