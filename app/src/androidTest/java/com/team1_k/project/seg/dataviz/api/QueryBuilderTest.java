package com.team1_k.project.seg.dataviz.api;

import android.test.AndroidTestCase;

/**
 * Created by alexstoick on 11/20/14.
 */
public class QueryBuilderTest extends AndroidTestCase {

    public void testGetMetrics() throws Exception {

        QueryBuilder queryBuilder = new QueryBuilder(getContext());
        queryBuilder.getMetrics();
        queryBuilder.getMetrics();
    }

}
