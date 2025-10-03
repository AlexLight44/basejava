package webapp.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainValue {
    public static void main(String[] args) {
        int[] values = {0, 11, 145, 2, 3, 4, 5};
        List<Integer> list = Arrays.asList(2, 51, 41, 8, 213, 3, 20);
        System.out.println(minValue(values));
        System.out.println(oddOrEven(list));
    }


    static int minValue(int[] values) {
        return Arrays.stream(values).filter(value -> value > 0 && value < 10).distinct().reduce(0, (number, x) -> number * 10 + x);
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        boolean isSumEven = integers.stream().mapToInt(Integer::intValue).sum() % 2 == 0;
        return integers.stream().filter(n -> (n % 2 == 0) != isSumEven).collect(Collectors.toList());
    }
}
