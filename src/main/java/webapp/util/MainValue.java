package webapp.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainValue {
    public static void main(String[] args) {
        int[] values = {0, 11, 145, 2, 3, 4, 5};
        List<Integer> list = Arrays.asList(2,2,3);
        System.out.println(minValue(values));
        System.out.println(oddOrEven(list));
        System.out.println(odd(list));
    }


    static int minValue(int[] values) {
        return Arrays.stream(values).filter(value -> value > 0 && value < 10).distinct().reduce(0, (number, x) -> number * 10 + x);
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        Predicate<Integer> predicate = n -> n % 2 != 0;
        var summarizer = Collectors.summingInt(Integer::intValue);
        var partitioningBy = Collectors.partitioningBy(predicate, summarizer);
        var result = integers.stream().collect(partitioningBy);
        return List.of(result.get(true), result.get(false));
    }
    static List<Integer> odd (List<Integer> integers) {
        var summarizer = Collectors.summingInt(Integer::intValue);
        int totalSum = integers.stream().collect(summarizer);
        Predicate<Integer> predicate = totalSum % 2 == 0 ? n -> n % 2 != 0 : n -> n % 2 == 0;
        return integers.stream().filter(predicate).collect(Collectors.toList());
    }
}