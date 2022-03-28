package com.dm4nk.optics_3.utility;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.MatrixUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dm4nk.optics_3.utility.FunctionUtilities.*;

public class FunctionUtilities2D {
    public static List<List<Double>> phase2(List<List<Complex>> source) {
        return source.stream().map(FunctionUtilities::phase).collect(Collectors.toList());
    }

    public static List<List<Double>> amplitude2(List<List<Complex>> source) {
        return source.stream().map(FunctionUtilities::amplitude).collect(Collectors.toList());
    }

    public static List<List<Complex>> swapList2(List<List<Complex>> list) {
        List<List<Complex>> result = new ArrayList<>();
        for (List<Complex> row : swapList(list)) {
            result.add(swapList(row));
        }
        return result;
    }

    public static List<List<Complex>> multiply2(List<List<Complex>> source, double multiplier) {
        var result = new ArrayList<List<Complex>>();
        for (List<Complex> row : source) {
            result.add(multiply(row, multiplier));
        }
        return result;
    }

    public static <T> List<List<T>> getElementsFromCenter2(List<List<T>> list, int size) {
        var elementsFromCenter = getElementsFromCenter(list, size);
        var result = new ArrayList<List<T>>();
        for (List<T> row : elementsFromCenter) {
            result.add(getElementsFromCenter(row, size));
        }
        return result;
    }

    public static void addZerosToListSize2(List<List<Complex>> list, int needSize) {
        int zerosSize = needSize - list.size();
        for (List<Complex> row : list) {
            addZerosToListToSize(row, needSize);
        }

        List<Complex> zeros = Collections.nCopies(list.size() + zerosSize, Complex.ZERO);
        for (int i = 0; i < zerosSize; i += 2) {
            list.add(new ArrayList<>(zeros));
            list.add(0, new ArrayList<>(zeros));
        }

        if (zerosSize % 2 != 0) {
            list.remove(0);
        }
    }

    public static List<List<Complex>> transpose(List<List<Complex>> matrix) {
        Complex[][] array = matrix.stream().map(e -> e.toArray(new Complex[0])).toArray(Complex[][]::new);
        Complex[][] transposedArray = MatrixUtils.createFieldMatrix(array).transpose().getData();
        return Arrays.stream(transposedArray)
                .map(Arrays::asList)
                .collect(Collectors.toList());
    }
}
