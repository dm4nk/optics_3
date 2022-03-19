package com.dm4nk.optics_3.model;

import com.dm4nk.optics_3.utility.Entity;
import com.dm4nk.optics_3.utility.FunctionUtilities;
import lombok.Getter;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.special.BesselJ;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.math3.util.FastMath.*;

public class Model {
    private static final double R = 5;
    private static final double m = -3;
    @Getter
    private static final int n = 127;
    private static final double alpha = 1;
    private static final double p = -5;
    private static final BesselJ besselJ = new BesselJ(abs(p));
    private static final double h = R / n;
    @Getter
    private final List<Double> x_k = new ArrayList<>();
    @Getter
    private final List<Entity<Double, Complex>> x_f = new ArrayList<>();
    private List<Complex> f_k;
    @Getter
    private List<List<Complex>> restoredBesselList;

    @Getter
    private List<List<Double>> restoredBesselPhase;
    @Getter
    private List<List<Double>> restoredBesselAmplitude;

    public Model() {
        init();
    }

    public Complex f(double r) {
        return Complex.I.multiply(p).exp().multiply(besselJ.value(alpha * r));
    }

    public List<List<Complex>> restoredFunction(List<Complex> list) {
        List<List<Complex>> restoredFunction = new ArrayList<>();

        int a;

        for (int i = 0; i < list.size()*2; ++i) {
            List<Complex> row = new ArrayList<>();
            for (int j = 0; j < list.size()*2; ++j) {
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
        restoredBesselPhase = FunctionUtilities.phase2(restoredBesselList);
        restoredBesselAmplitude = FunctionUtilities.amplitude2(restoredBesselList);
    }
}
