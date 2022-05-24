package com.dm4nk.optics_3.model;

import com.dm4nk.optics_3.utility.Entity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.special.BesselJ;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.dm4nk.optics_3.utility.FunctionUtilities2D.*;
import static org.apache.commons.math3.util.FastMath.*;

@Slf4j
public class Model {
    private static final double R = 5;
    private static final double m = -3;
    private static final int LIST_SIZE = 2048;
    @Getter
    //max n for web = 10
    //max n for exel = 127
    private static final int n = 200;
    private static final double alpha = 1;
    private static final double p = -5;
    private static final BesselJ besselJ = new BesselJ(abs(p));
    private static final BesselJ besselJForHankel = new BesselJ(abs(m));
    private static final double h = R / n;
    @Getter
    private final List<Double> x_k = new ArrayList<>();
    @Getter
    private final List<Entity<Double, Complex>> x_f = new ArrayList<>();
    @Getter
    private final List<Entity<Double, Complex>> hankelTransformedListForView = new ArrayList<>();
    @Getter
    private long hankelTime;
    @Getter
    private long fftTime;
    private List<Complex> f_k;
    private List<List<Complex>> restoredBesselList;
    @Getter
    private List<List<Double>> restoredBesselPhase;
    @Getter
    private List<List<Double>> restoredBesselAmplitude;
    private List<Complex> hankelTransformedList;
    private List<List<Complex>> restoredHankelTransformedList;
    @Getter
    private List<List<Double>> restoredHankelTransformedPhase;
    @Getter
    private List<List<Double>> restoredHankelTransformedAmplitude;

    private List<List<Complex>> fftTransformedList;
    @Getter
    private List<List<Double>> fftTransformedPhase;
    @Getter
    private List<List<Double>> fftTransformedAmplitude;

    public Model() {
        init();
    }

    private List<Complex> hankelTransform(List<Complex> f, List<Double> rList) {
        hankelTime = System.nanoTime();

        List<Complex> result = new ArrayList<>();
        for (int i = 0; i < rList.size(); i++) {
            List<Double> besselValues = new ArrayList<>();

            for (Double r : rList)
                besselValues.add(besselJForHankel.value(2 * Math.PI * rList.get(i) * r));

            Complex ro = IntStream.range(0, besselValues.size())
                    .mapToObj(k -> f.get(k)
                            .multiply(besselValues.get(k))
                            .multiply(rList.get(k)))
                    .reduce(Complex::add)
                    .get();

            result.add(ro);
        }

        result = result.stream()
                .map(e -> e.multiply(Complex.valueOf(2).multiply(Math.PI).divide(Complex.I.pow(m))))
                .collect(Collectors.toList());

        hankelTime = (System.nanoTime() - hankelTime) / 1_000_000;
        log.debug("Hankel transformation time: " + hankelTime + " ms");
        return result;
    }

    private List<Complex> fastFourierTransform(List<Complex> toTransform) {
        return Arrays.asList(
                new FastFourierTransformer(DftNormalization.STANDARD)
                        .transform(toTransform.toArray(new Complex[0]), TransformType.FORWARD)
        );
    }

    private List<List<Complex>> fastFourierTransform2(List<List<Complex>> toTransform) {
        List<List<Complex>> result = new ArrayList<>();
        for (List<Complex> row : toTransform) {
            result.add(fastFourierTransform(row));
        }

        result = transpose(result);
        for (int i = 0; i < result.size(); i++) {
            result.set(i, fastFourierTransform(result.get(i)));
        }
        return transpose(result);
    }

    private Complex f(double r) {
        return Complex.I.multiply(p).exp().multiply(besselJ.value(alpha * r));
    }

    private List<List<Complex>> restoredFunction(List<Complex> list) {
        List<List<Complex>> restoredFunction = new ArrayList<>();

        int a;

        for (int i = 0; i < list.size() * 2 + 1; ++i) {
            List<Complex> row = new ArrayList<>();
            for (int j = 0; j < list.size() * 2 + 1; ++j) {
                a = (int) round(sqrt(pow(i - n, 2) + pow(j - n, 2)));

                if (a >= n)
                    row.add(Complex.ZERO);
                else
                    row.add(list.get(a)
                            .multiply(
                                    Complex.I.multiply(m * atan2(j - n, i - n))
                                            .exp()
                            ));
            }
            restoredFunction.add(row);
        }

        return restoredFunction;
    }

    private List<List<Complex>> FFT2D(List<List<Complex>> toTransform) {
        fftTime = System.nanoTime();

        List<List<Complex>> copyOfFunction = new ArrayList<>(toTransform);
        addZerosToListSize2(copyOfFunction, LIST_SIZE);
        copyOfFunction = swapList2(copyOfFunction);
        List<List<Complex>> transformedList = fastFourierTransform2(copyOfFunction);
        transformedList = multiply2(transformedList, h);
        transformedList = swapList2(transformedList);
        transformedList = getElementsFromCenter2(transformedList, n * 2 + 1);

        fftTime = (System.nanoTime() - fftTime) / 1_000_000;
        log.debug("FFT transformation time: " + fftTime + " ms");

        return transformedList;
    }

    private void init() {
        //initializing x
        for (int i = 0; i < n; ++i)
            x_k.add(i * h);

        //initializing given function
        f_k = x_k.stream()
                .map(this::f)
                .collect(Collectors.toList());

        //initializing function for view
        for (int i = 0; i < x_k.size(); ++i)
            x_f.add(new Entity<>(x_k.get(i), f_k.get(i)));

        //initializing restored function
        restoredBesselList = restoredFunction(f_k);
        restoredBesselPhase = phase2(restoredBesselList);
        restoredBesselAmplitude = amplitude2(restoredBesselList);

        //initializing hankel transformed function
        hankelTransformedList = hankelTransform(f_k, x_k);

        //initializing hankel transformed function for view
        for (int i = 0; i < x_k.size(); ++i)
            hankelTransformedListForView.add(new Entity<>(x_k.get(i), hankelTransformedList.get(i)));

        //initializing restored hankel transformed function
        restoredHankelTransformedList = restoredFunction(hankelTransformedList);
        restoredHankelTransformedPhase = phase2(restoredHankelTransformedList);
        restoredHankelTransformedAmplitude = amplitude2(restoredHankelTransformedList);

        //initializing FFT transformed function
        fftTransformedList = FFT2D(restoredBesselList);
        fftTransformedPhase = phase2(fftTransformedList);
        fftTransformedAmplitude = amplitude2(fftTransformedList);
    }
}
