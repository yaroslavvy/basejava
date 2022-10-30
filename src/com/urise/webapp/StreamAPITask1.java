package com.urise.webapp;

import java.util.Arrays;
import java.util.Comparator;

public class StreamAPITask1 {
    public static int order = 0;

    public static void main(String[] args) {
        int[] array1 = {1, 2, 3, 3, 2, 3};
        System.out.println("Array1: " + Arrays.toString(array1));
        System.out.println("minValue return: " + minValue(array1));

        int[] array2 = {9, 8};
        System.out.println("Array2: " + Arrays.toString(array2));
        System.out.println("minValue return: " + minValue(array2));
    }

    private static int minValue(int[] values) {
        order = 0;
        return Arrays.stream(values)
                .distinct()
                .boxed()
                .sorted(Comparator.reverseOrder())
                .reduce(0, (accumulator, value) -> accumulator + value * (int) Math.pow(10, order++));
    }
}
