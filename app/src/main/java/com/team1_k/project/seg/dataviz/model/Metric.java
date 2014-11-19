package com.team1_k.project.seg.dataviz.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Metric {

    /**
     * String id - the id with which we grab the metric from the API, eg: AG.CON.FERT.ZS
     */
    private String id;

    /**
     * The human readable version of the id, eg: Fertilizer consumption (kilograms per
     * hectare of arable land)
     */
    private String name;

    /**
     * The description of this metric, eg: "Fertilizer consumption measures the quantity of ...
     */
    private String description;

    /**
     * dataPoints - first integer represents year, second the value for that year for this metric
     */
    private HashMap<Integer,Integer> dataPoints;


    public Metric(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
