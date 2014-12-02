package com.team1_k.project.seg.dataviz.data_exchange_rate;

/**
 * Created by dbrisingr on 01/12/14.
 */
public class ExchangeItem {

    private final String label;
    private final String title;

    public ExchangeItem(String label, String title) {
        this.label = label;
        this.title = title;
    }

    public String getLabel() {
        return label;
    }
    public String getTitle() {
        return title;
    }
}
