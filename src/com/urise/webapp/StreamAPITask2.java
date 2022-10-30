package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamAPITask2 {
    public static void main(String[] args) {
        List<Integer> oddSumIntegerList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 13}));
        System.out.println("Odd sum list: " + oddSumIntegerList);
        System.out.println("Result list: " + oddOrEven(oddSumIntegerList));

        List<Integer> evenSumIntegerList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 17, 13}));
        System.out.println("Even sum list: " + evenSumIntegerList);
        System.out.println("Result list: " + oddOrEven(evenSumIntegerList));
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream()
                .filter(num -> (integers.stream().reduce(Integer::sum).get() % 2 == 0) == (num % 2 != 0))
                .collect(Collectors.toList());
    }
}
