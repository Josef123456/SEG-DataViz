package com.team1_k.project.seg.dataviz.data_exchange_rate;

/**
 * Created by dbrisingr on 01/12/14.
 */
public class ExchangeItem {

    public String getLabel() {
        return label;
    }

    public String getTitle() {
        return title;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getDifference() {
        return difference;
    }

    private final String label;
    private final String title;
    private String difference;

    public ExchangeItem(String label, String title, String difference) {
        this.label = label;
        this.title = title;
        this.difference = difference;
    }


}