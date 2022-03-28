package com.dm4nk.optics_3.view;

import com.dm4nk.optics_3.model.Model;
import com.dm4nk.optics_3.utility.Entity;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.commons.math3.complex.Complex;

import java.io.IOException;
import java.util.List;

@Route("")
public class View extends VerticalLayout {

    public View() throws IOException {
        Model model = new Model();

        addGraph(model.getX_f(), "Bessel modes");

//        add(new H2("Restored Bessel"), new FormLayout(
//                addScatter(model.getRestoredBesselPhase(), model.getX_k(), "Restored Bessel Phase"),
//                addScatter(model.getRestoredBesselAmplitude(), model.getX_k(), "Restored Bessel Amplitude")
//        ));

        addGraph(model.getHankelTransformedListForView(), "Function after Hankel transformation");

        add(new H3("Hankel transformation time : " + model.getHankelTime()));
        add(new H3("FFT transformation time : " + model.getFftTime()));
    }

    private void addGraph(List<Entity<Double, Complex>> list, String title) {
        Chart chart = new Chart();
        Configuration real = chart.getConfiguration();
        Configuration imaginary = chart.getConfiguration();

        DataSeries phase = new DataSeries();
        DataSeries amplitude = new DataSeries();
        list.forEach(complex -> {
            phase.add(new DataSeriesItem(complex.getT(), complex.getE().getArgument()));
            amplitude.add(new DataSeriesItem(complex.getT(), complex.getE().abs()));
        });
        phase.setName("Phase");
        amplitude.setName("Amplitude");
        real.addSeries(phase);
        imaginary.addSeries(amplitude);
        add(new H2(title), chart);
    }

    private Chart addScatter(List<List<Double>> list, List<Double> x_k, String title) {
        Chart chart = new Chart(ChartType.SCATTER);

        Configuration conf = chart.getConfiguration();

        Marker marker = new Marker();
        marker.setRadius(5);

        PlotOptionsScatter options = new PlotOptionsScatter();
        options.setMarker(marker);
        conf.setPlotOptions(options);

        DataSeries series = new DataSeries();

        Double maxPhase = Double.MIN_VALUE;
        Double minPhase = Double.MAX_VALUE;

        for (List<Double> l : list)
            for (Double c : l) {
                maxPhase = Math.max(maxPhase, c);
                minPhase = Math.min(minPhase, c);
            }

        for (int i = 0; i < x_k.size() * 2; ++i) {
            for (int j = 0; j < x_k.size() * 2; ++j) {
                double normalizedPhase = (list.get(i).get(j) - minPhase) / (maxPhase - minPhase);
                DataSeriesItem point = new DataSeriesItem(i, j, new SolidColor((int) (255 * normalizedPhase), 127, 127));
                series.add(point);
            }
        }

        series.setName(title);
        conf.addSeries(series);
        return chart;
    }
}