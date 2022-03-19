package com.dm4nk.optics_3.utility;

import org.apache.commons.math3.complex.Complex;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionUtilities {
    public static List<Double> phase(List<Complex> source) {
        return source.stream().map(Complex::getArgument).collect(Collectors.toList());
    }

    public static List<Double> amplitude(List<Complex> source) {
        return source.stream().map(Complex::abs).collect(Collectors.toList());
    }

    public static List<List<Double>> phase2(List<List<Complex>> source) {
        return source.stream().map(FunctionUtilities::phase).collect(Collectors.toList());
    }

    public static List<List<Double>> amplitude2(List<List<Complex>> source) {
        return source.stream().map(FunctionUtilities::amplitude).collect(Collectors.toList());
    }
}
