package com.team1_k.project.seg.dataviz.model;

import org.json.JSONObject;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Country {

    String name ;
    Metric[] metrics ;

    public String getName() {
        return name;
    }

    public Country(JSONObject country) {

    }
}
