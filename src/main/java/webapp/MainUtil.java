package webapp;


import java.util.function.Supplier;

public class MainUtil {
    public static void main(String[] args) {
        Integer x = 1243;
        Supplier<String> sup = x::toString;
        System.out.println(sup.get());
        String s = "abcde";
        String b = "222";
        Interf inf = (a, z) -> a.length() + z.length();
        System.out.println(inf.meth(s, b));

    }

    @FunctionalInterface
    interface Interf {
        int meth(String a, String b);
    }
}
