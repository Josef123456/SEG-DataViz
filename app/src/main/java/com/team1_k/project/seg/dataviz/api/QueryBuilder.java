package com.team1_k.project.seg.dataviz.api;

import com.team1_k.project.seg.dataviz.model.Country;
import com.team1_k.project.seg.dataviz.model.Metric;
import com.team1_k.project.seg.dataviz.model.MetricList;

/**
 * Created by alexstoick on 11/17/14.
 */
public class QueryBuilder {

    private static final String API_BASE_URL = "http://api.worldbank.org/" ;

    /**
     * Grabs specific metric for a country. API Call will look like this:
     * countries/country.iso/indicators/metric.id?per_page=10&format=json
     * @param country
     * @param metric
     * @return JSON String
     */
    public static String forParams ( Country country, Metric metric) {

        return "" ;

    }

    /**
     * Grabs specific metric for a country bound by years. API call will look like this
     * /countries/country.iso/indicators/metric.id?per_page=10&date=startYear:endYear&format=json
     * @param country
     * @param metric
     * @param startYear
     * @param finalYear
     * @return JSON String
     */
    public static String forParams ( Country country, Metric metric, int startYear, int finalYear) {

        return "" ;

    }

    /**
     * Gets all the available metrics. API Call:
     * /source/2/indicators?format=json
     * @return JSON String
     */
    public static String getMetrics () {

        return "" ;
    }

}
