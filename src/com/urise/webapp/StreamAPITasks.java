package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamAPITasks {

    public static void main(String[] args) {

        //Test task №1
        System.out.println("Test task №1");
        int[] array1 = {1, 2, 3, 3, 2, 3};
        System.out.println("Array1: " + Arrays.toString(array1));
        System.out.println("minValue return: " + minValue(array1));

        int[] array2 = {9, 8};
        System.out.println("Array2: " + Arrays.toString(array2));
        System.out.println("minValue return: " + minValue(array2));

        //Test task №2
        System.out.println();
        System.out.println("Test task №2");
        List<Integer> oddSumIntegerList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 13}));
        System.out.println("Odd sum list: " + oddSumIntegerList);
        System.out.println("Result list: " + oddOrEven(oddSumIntegerList));

        List<Integer> evenSumIntegerList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 17, 13}));
        System.out.println("Even sum list: " + evenSumIntegerList);
        System.out.println("Result list: " + oddOrEven(evenSumIntegerList));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (a,b) -> 10 * a + b);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream()
                .filter(num -> (integers.stream().reduce(Integer::sum).get() % 2 == 0) == (num % 2 != 0))
                .collect(Collectors.toList());
    }
}
