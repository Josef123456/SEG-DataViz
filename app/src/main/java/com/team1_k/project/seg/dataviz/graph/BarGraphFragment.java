package com.team1_k.project.seg.dataviz.graph;

/**
 * Created by alexstoick on 12/8/14.
 */

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.model.DataPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dbrisingr on 08/12/14.
 */
public class BarGraphFragment extends GraphFragment {

    private BarChart mChart;
    private View rootView;
    private final static String LOG_TAG = "ui.fragment.bar_graph";
    private ArrayList<ArrayList<DataPoint>> mDataPointsArray;
    ArrayList<String> mXVals = new ArrayList<String>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frament_bar_chart, container, false);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription("");

        // disable the drawing of values
        mChart.setDrawYValues(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setValueFormatter(new LargeValueFormatter());

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawHorizontalGrid(false);

        XLabels xl = mChart.getXLabels();
        xl.setCenterXLabelText(true);

        YLabels yl = mChart.getYLabels();
        yl.setFormatter(new LargeValueFormatter());

        return rootView;
    }

    public void setData(){

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();

        ArrayList<DataPoint> currentLine = mDataPointsArray.get(0) ;

        Log.w ( LOG_TAG , currentLine.get(0).getMetric().getApiId());
        mChart.setUnit(metricToUnitBinding.get(currentLine.get(0).getMetric().getApiId()));
        getActivity().setTitle(currentLine.get(0).getMetric().getName());

        Set<Integer> yearValues = new HashSet<Integer>();

        for (int i = 0; i < mDataPointsArray.size(); ++i) {
            ArrayList<DataPoint> currentArray = mDataPointsArray.get(i);
            for ( int j = 0 ; j < currentArray.size(); ++ j )
                yearValues.add(currentArray.get(j).getYear());
        }
        List sortedYears =  new ArrayList(yearValues);
        Collections.sort(sortedYears);
        mXVals = new ArrayList<String>();
        for ( int i = 0 ; i < sortedYears.size(); ++ i ) {
            mXVals.add(String.valueOf(sortedYears.get(i)));
        }

        Log.w ( LOG_TAG, sortedYears.toArray().toString() ) ;

        for ( int lineNumber = 0 ; lineNumber < mDataPointsArray.size() ; ++ lineNumber) {
            currentLine = mDataPointsArray.get(lineNumber) ;

            ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
            for (int i = 0; i < currentLine.size(); ++i) {
                yearValues.add(currentLine.get(i).getYear());
                float displayValue = (float)currentLine.get(i).getValue() ;
                Log.d ( LOG_TAG , String.valueOf(displayValue) );
                if ( displayValue > 2147000000 )
                    displayValue = (float)currentLine.get(i).getValue() / 1000000000 ;
                Log.d ( LOG_TAG, String.valueOf(displayValue));
                yVals.add(
                        new BarEntry(
                                displayValue,
                                sortedYears.indexOf(currentLine.get(i).getYear())
                        )
                );
            }
            String name ;
            if ( currentLine.get(0).getCountry() == null ) {
                name = currentLine.get(0).getMetric().getName();
            } else {
                name = currentLine.get(0).getCountry().getName();
            }
            BarDataSet dataSet = new BarDataSet(
                    yVals,
                    name
            );

            dataSet.setColor(mColors[lineNumber]);
            dataSets.add(dataSet); // add the datasets
        }
        BarData data = new BarData(mXVals, dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        Log.i( LOG_TAG , "Selected: " + e.toString() + ", dataSet: " + dataSetIndex);
    }

    public void updateData(ArrayList<ArrayList<DataPoint>> dataPointsArray) {
        mDataPointsArray = dataPointsArray;
        Log.d ( LOG_TAG, String.valueOf(mDataPointsArray.size()) ) ;
        setData();
    }
}
