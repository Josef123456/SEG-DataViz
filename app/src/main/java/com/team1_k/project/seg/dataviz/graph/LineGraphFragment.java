package com.team1_k.project.seg.dataviz.graph;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartGestureListener;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.LimitLine;
import com.team1_k.project.seg.dataviz.R;
import com.team1_k.project.seg.dataviz.model.DataPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexstoick on 12/6/14.
 */
public class LineGraphFragment extends Fragment implements OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {

    private static final String LOG_TAG = "ui.fragment.line_graph" ;

    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private View rootView;
    ArrayList<ArrayList<DataPoint>> mDataPointsArray;

    private static final Map<String, String> metricToUnitBinding ;
    static {
        Map<String, String> map = new HashMap<String, String>();

        map.put("NY.GDP.MKTP.CD", "billion $");
        map.put("IC.EXP.COST.CD", "$ per container");
        map.put("IC.IMP.COST.CD", "$ per container");
        map.put("NE.EXP.GNFS.KD.ZG", "annual % growth" );
        map.put("NE.IMP.GNFS.KD.ZG", "annual % growth" );
        map.put("FP.CPI.TOTL.ZG", "annual %");
        map.put("SL.TLF.TOTL.IN", "");
        map.put("FR.INR.RINR", "%" );
        metricToUnitBinding = Collections.unmodifiableMap(map);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);


        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mChart = (LineChart) rootView.findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setUnit(" billion $");
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

        return rootView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void setData(int count, float range) {

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        ArrayList<DataPoint> currentLine = mDataPointsArray.get(0) ;

        Log.w ( LOG_TAG , currentLine.get(0).getMetric().getApiId());
        mChart.setUnit(metricToUnitBinding.get(currentLine.get(0).getMetric().getApiId()));
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < currentLine.size(); ++i) {
            xVals.add( String.valueOf(currentLine.get(i).getYear()) );
        }

        for ( int lineNumber = 0 ; lineNumber < mDataPointsArray.size() ; ++ lineNumber) {
            currentLine = mDataPointsArray.get(lineNumber) ;

            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < currentLine.size(); ++i) {
                float displayValue = (float)currentLine.get(i).getValue() ;
                Log.d ( LOG_TAG , String.valueOf(displayValue) );
                if ( displayValue > 2147000000 )
                    displayValue = (float)currentLine.get(i).getValue() / 1000000000 ;
                Log.d ( LOG_TAG, String.valueOf(displayValue));
                yVals.add(new Entry(displayValue, i));
            }
            LineDataSet dataSet = new LineDataSet(yVals, "DataSet " + lineNumber);

            dataSet.enableDashedLine(10f, 5f, 0f);
            dataSet.setColor(Color.BLACK);
            dataSet.setCircleColor(Color.BLACK);
            dataSet.setLineWidth(1f);
            dataSet.setCircleSize(4f);
            dataSet.setFillAlpha(65);
            dataSet.setFillColor(Color.BLACK);
            dataSets.add(dataSet); // add the datasets
        }

        LineData data = new LineData(xVals, dataSets);

        mChart.setData(data);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    public void updateData(ArrayList<ArrayList<DataPoint>> dataPointsArray) {
        mDataPointsArray = dataPointsArray;
        Log.d ( LOG_TAG, String.valueOf(mDataPointsArray.size()) ) ;
        setData(50, 400);
    }

}