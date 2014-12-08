package com.team1_k.project.seg.dataviz.graph;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartGestureListener;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
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
 * Created by alexstoick on 12/6/14.
 */
public class LineGraphFragment extends GraphFragment {

    private static final String LOG_TAG = "ui.fragment.line_graph" ;

    private LineChart mChart;
    private View rootView;
    ArrayList<ArrayList<DataPoint>> mDataPointsArray;
    ArrayList<String> mXVals = new ArrayList<String>();

    public static LineGraphFragment newInstance(ArrayList<ArrayList<DataPoint>> mDataPointsArray) {
        LineGraphFragment fragment = new LineGraphFragment();
        Bundle bundle = new Bundle();
        int i = 0 ;
        for( ArrayList<DataPoint> p : mDataPointsArray ) {
            bundle.putParcelableArrayList( "data" + i , p);
        }
        bundle.putInt("dataLength", mDataPointsArray.size());
        fragment.setArguments(bundle);
        return fragment ;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mChart = (LineChart) rootView.findViewById(R.id.chartLine);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawUnitsInChart(true);

        mChart.setStartAtZero(false);

        mChart.setDrawYValues(false);

        mChart.setDrawBorder(true);
        mChart.setBorderPositions(new BorderPosition[] {
                BorderPosition.BOTTOM
        });
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        mChart.setDrawGridBackground(true);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setValueTextColor(Color.RED);
        mChart.setHighlightEnabled(true);

        mChart.setTouchEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setPinchZoom(true);

        mChart.setHighlightIndicatorEnabled(false);

        mChart.animateX(2500);

        if ( getArguments() != null ) {
            int length = getArguments().getInt("dataLength");
            mDataPointsArray = new ArrayList<ArrayList<DataPoint>>();
            for ( int i = 0 ; i < length ; ++ i ) {
                ArrayList<DataPoint> currentArray = getArguments().getParcelableArrayList("data"+i);
                mDataPointsArray.add(currentArray);
            }
            setData();
        }

        return rootView;
    }

    private void setData() {

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

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

            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < currentLine.size(); ++i) {
                yearValues.add(currentLine.get(i).getYear());
                float displayValue = (float)currentLine.get(i).getValue() ;
                Log.d ( LOG_TAG , String.valueOf(displayValue) );
                if ( displayValue > 2147000000 )
                    displayValue = (float)currentLine.get(i).getValue() / 1000000000 ;
                Log.d ( LOG_TAG, String.valueOf(displayValue));
                yVals.add(
                        new Entry(
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
            LineDataSet dataSet = new LineDataSet(
                    yVals,
                    name
            );

            dataSet.setColor(mColors[lineNumber]);
            dataSet.setCircleColor(mColors[lineNumber]);
            dataSet.setLineWidth(6f);
            dataSet.setCircleSize(5f);
            dataSet.setFillAlpha(100);
            dataSet.setFillColor(mColors[lineNumber]);
            dataSets.add(dataSet); // add the datasets
        }

        LineData data = new LineData(mXVals, dataSets);

        mChart.setData(data);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        Log.i("Entry selected", e.toString());
        String message = "" ;
        Log.w ( LOG_TAG, String.valueOf(mDataPointsArray.size()) );
        if ( mDataPointsArray.size() <= 1 ) {
            message = "Entry for year: " + mXVals.get(e.getXIndex())
                    + " with value: " + String.valueOf(e.getVal()) + " " + mChart.getUnit() ;

        } else {
            message = "Entry for year: " + mXVals.get(e.getXIndex())
                    + " with value: " + String.valueOf(e.getVal()) + " " + mChart.getUnit()
                    + " for country "
                    + mDataPointsArray.get(dataSetIndex).get(0).getCountry().getName()
            ;
        }
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void updateData(ArrayList<ArrayList<DataPoint>> dataPointsArray) {
        mDataPointsArray = dataPointsArray;
        Log.d ( LOG_TAG, String.valueOf(mDataPointsArray.size()) ) ;
        setData();
    }

}