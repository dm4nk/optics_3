package com.dm4nk.optics_3.view;

import com.dm4nk.optics_3.model.Model;
import com.dm4nk.optics_3.utility.Entity;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.Color;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.commons.math3.complex.Complex;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.math3.util.FastMath.abs;

@Route("")
public class View extends VerticalLayout {

    public View() throws IOException {
        Model model = new Model();

        addGraph(model.getX_f(), "Bessel modes");
        //addScatter(model.getRestoredList(), model.getX_k(), "Restored Bessel");
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

    private void addScatter(List<List<Complex>>list, List<Double> x_k,  String title) {
        Chart chart = new Chart(ChartType.SCATTER);

        Configuration conf = chart.getConfiguration();

        Marker marker = new Marker();
        marker.setLineColor(SolidColor.BLACK);
        marker.setRadius(5);

        PlotOptionsScatter options = new PlotOptionsScatter();
        options.setMarker(marker);
        conf.setPlotOptions(options);

        DataSeries series = new DataSeries();

        Double maxPhase = Double.MIN_VALUE;
        Double maxAmplitude = Double.MIN_VALUE;
        Double minPhase = Double.MAX_VALUE;
        Double minAmplitude = Double.MAX_VALUE;

        for(List<Complex> l : list)
            for (Complex c : l){
                maxPhase = Math.max(maxPhase, c.getArgument());
                maxAmplitude = Math.max(maxAmplitude, c.abs());
                minPhase = Math.min(minPhase, c.getArgument());
                minAmplitude = Math.min(minAmplitude, c.abs());
            }


        for (int i = 0; i < x_k.size()*2; i++) {
            for (int j = 0; j < x_k.size()*2; j++) {
                DataSeriesItem point = new DataSeriesItem(i, j);
                Marker markerp = new Marker();
                double normalizedPhase = (list.get(i).get(j).getArgument() - minPhase)/ (maxPhase - minPhase);
                markerp.setFillColor(new SolidColor((int) (255 * normalizedPhase), 127, 127));
                point.setMarker(markerp);

                series.add(point);
            }
        }
        conf.addSeries(series);
        add(new H2(title), chart);
    }
}