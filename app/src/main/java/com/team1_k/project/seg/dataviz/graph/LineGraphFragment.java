package com.team1_k.project.seg.dataviz.graph;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.model.DataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexstoick on 12/6/14.
 */
public class LineGraphFragment extends Fragment {
    public void updateData(ArrayList<ArrayList<DataPoint>> dataPointsArray) {}
/*
    private static final String LOG_TAG = "ui.graph.line_graph.fragment";

    LineChartView chart;
    LineChartData data;
    int numberOfLines ;
    int numberOfPoints ;

    float[][] randomNumbersTab;

    boolean hasAxes = true;
    boolean hasAxesNames = true;
    boolean hasLines = true;
    boolean hasPoints = true;
    ValueShape shape = ValueShape.CIRCLE;
    boolean isFilled = false;
    boolean hasLabels = true;
    boolean isCubic = false;
    boolean hasLabelForSelected = false;

    public LineGraphFragment() {
    }

    public void updateData(ArrayList<ArrayList<DataPoint>> dataPointsArray) {
        numberOfLines = dataPointsArray.size() ;
        numberOfPoints = 60 ;
        randomNumbersTab = new float[numberOfLines][numberOfPoints];
        for ( int i = 0 ; i < numberOfLines ; ++ i ) {
            ArrayList<DataPoint> dataPoints = dataPointsArray.get(i);
            numberOfPoints = dataPoints.size();
            for (int j = 0; j < numberOfPoints; ++j) {
                DataPoint currentDataPoint = dataPoints.get(j);
                randomNumbersTab[i][j] =
                        (float) currentDataPoint.getValue();
            }
        }
        generateData();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

        chart = (LineChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        chart.setInteractive(true);
        return rootView;
    }

    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
                Log.i(LOG_TAG, String.valueOf(randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(Utils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Year");
                axisY.setName("GDP");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
    }


    private class ValueTouchListener implements LineChartView.LineChartOnValueTouchListener {

        @Override
        public void onValueTouched(int selectedLine, int selectedValue, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingTouched() {
        }
    }*/
}