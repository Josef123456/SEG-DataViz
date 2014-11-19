package com.team1_k.project.seg.dataviz.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alexstoick on 11/19/14.
 */
public class DataVizContract {

    public static final String CONTRACT_AUTHORITY = "com.team1_k.project.seg.dataviz." +
            "contentprovider" ;

    public static final String PATH_COUNTRY = "country" ;
    public static final String PATH_METRIC = "metric" ;
    public static final String PATH_DATA_POINT = "data_point" ;


    public static final class CountryEntry implements BaseColumns {

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTRACT_AUTHORITY + "/" + PATH_COUNTRY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTRACT_AUTHORITY + "/" + PATH_COUNTRY;


        public static final String TABLE_NAME = "country" ;
        public static final String COLUMN_NAME = "name" ;

        public static final String[] COLUMNS = {
                _ID,
                COLUMN_NAME
        };
    }

    public static final class MetricEntry implements BaseColumns {

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTRACT_AUTHORITY + "/" + PATH_METRIC;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTRACT_AUTHORITY + "/" + PATH_METRIC;


        public static final String TABLE_NAME = "metric" ;
        public static final String COLUMN_NAME = "name" ;
        public static final String COLUMN_DESCRIPTION = "description" ;
        public static final String COLUMN_API_ID = "api_id" ;
    }

    public static final class DataPointEntry implements BaseColumns {


        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTRACT_AUTHORITY + "/" + PATH_DATA_POINT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTRACT_AUTHORITY + "/" + PATH_DATA_POINT;


        public static final String TABLE_NAME = "data_point" ;
        public static final String COLUMN_COUNTRY_ID = "country_id" ;
        public static final String COLUMN_METRIC_ID = "metric_id" ;
        public static final String COLUMN_YEAR = "year" ;
        public static final String COLUMN_VALUE = "value" ;
    }


}
