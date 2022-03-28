package com.dm4nk.optics_3.utility;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionUtilities {
    public static List<Double> phase(List<Complex> source) {
        return source.stream().map(Complex::getArgument).collect(Collectors.toList());
    }

    public static List<Double> amplitude(List<Complex> source) {
        return source.stream().map(Complex::abs).collect(Collectors.toList());
    }

    public static List<Complex> addZerosToListToSize(List<Complex> list, int size) {
        int zeroCount = size - list.size();

        for (int i = 0; i < zeroCount; i += 2) {
            list.add(Complex.ZERO);
            list.add(0, Complex.ZERO);
        }

        if (zeroCount % 2 != 0) {
            list.remove(0);
        }

        return list;
    }

    public static <T> List<T> swapList(List<T> list) {
        int listMiddle = list.size() / 2;
        List<T> resultList = new ArrayList<>(list.subList(listMiddle, list.size()));
        resultList.addAll(list.subList(0, listMiddle));
        return resultList;
    }

    public static List<Complex> multiply(List<Complex> source, double multiplier) {
        return source.stream().map(e -> e.multiply(multiplier)).collect(Collectors.toList());
    }

    public static <T> List<T> getElementsFromCenter(List<T> list, int size) {
        int center = list.size() / 2;
        return list.subList(center - (size / 2), center + (size / 2));
    }
}
