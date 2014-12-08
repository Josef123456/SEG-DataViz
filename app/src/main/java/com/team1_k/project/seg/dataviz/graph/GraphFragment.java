package com.team1_k.project.seg.dataviz.graph;

import android.app.Fragment;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.OnChartGestureListener;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.team1_k.project.seg.dataviz.model.DataPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexstoick on 12/8/14.
 */
abstract public class GraphFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {

    protected static final Map<String, String> metricToUnitBinding ;
    static {
        Map<String, String> map = new HashMap<String, String>();

        map.put("NY.GDP.MKTP.CD", "billion $");
        map.put("SL.UEM.TOTL.ZS", "% of total");
        map.put("IC.EXP.COST.CD", "$ per container");
        map.put("IC.IMP.COST.CD", "$ per container");
        map.put("NE.EXP.GNFS.KD.ZG", "annual % growth" );
        map.put("NE.IMP.GNFS.KD.ZG", "annual % growth" );
        map.put("FP.CPI.TOTL.ZG", "annual %");
        map.put("SL.TLF.TOTL.IN", "");
        map.put("FR.INR.RINR", "%" );
        metricToUnitBinding = Collections.unmodifiableMap(map);
    }

    protected static final int[] mColors = {
            Color.BLUE,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.GRAY,
    } ;

    abstract public void updateData(ArrayList<ArrayList<DataPoint>> dataPointsArray);

    @Override
    public void onChartLongPressed(MotionEvent motionEvent) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {

    }

    @Override
    public void onValueSelected(Entry entry, int i) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
