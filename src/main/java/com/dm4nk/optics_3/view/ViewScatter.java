package com.dm4nk.optics_3.view;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/scatter")
public class ViewScatter extends VerticalLayout {

    public ViewScatter() {
        initDemo();
    }

    public void initDemo() {
        Chart chart = new Chart(ChartType.SCATTER);

        Configuration conf = chart.getConfiguration();

        PlotOptionsScatter options = new PlotOptionsScatter();
        conf.setPlotOptions(options);

        DataSeries series = new DataSeries();
        for (int i=0; i<300; i++) {
            double lng = Math.random() * 2 * Math.PI;
            double lat = Math.random() * Math.PI - Math.PI/2;
            double x   = Math.cos(lat) * Math.sin(lng);
            double y   = Math.sin(lat);

            DataSeriesItem point = new DataSeriesItem(x,y);
            series.add(point);
        }
        conf.addSeries(series);
        add(chart);
    }
}