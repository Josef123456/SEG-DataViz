package com.team1_k.project.seg.dataviz.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alexstoick on 11/19/14.
 */
public class DataVizContract {

    public static final String CONTENT_AUTHORITY = "com.team1_k.project.seg.dataviz." +
            "contentprovider" ;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_COUNTRY = "country" ;
    public static final String PATH_METRIC = "metric" ;
    public static final String PATH_DATA_POINT = "data_point" ;


    public static final class CountryEntry implements BaseColumns {

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;

        public static final String TABLE_NAME = "country" ;
        public static final String COLUMN_NAME = "name" ;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_COUNTRY).build();

        public static Uri buildCountryUri(long _ID){
            return ContentUris.withAppendedId(CONTENT_URI, _ID) ;
        }

        public static Uri buildCountryWithMetricUri(long _ID){
            return CONTENT_URI.buildUpon().
                    appendEncodedPath("metrics").
                    appendEncodedPath(String.valueOf(_ID)).build();
        }

        public static final String[] COLUMNS = {
                _ID,
                COLUMN_NAME
        };
    }

    public static final class MetricEntry implements BaseColumns {

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_METRIC;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_METRIC;


        public static final String TABLE_NAME = "metric" ;
        public static final String COLUMN_NAME = "name" ;
        public static final String COLUMN_DESCRIPTION = "description" ;
        public static final String COLUMN_API_ID = "api_id" ;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_METRIC).build();

        public static Uri buildMetricUri(long _ID) {
            return ContentUris.withAppendedId(CONTENT_URI, _ID);
        }

        public static final String[] COLUMNS = {
                _ID,
                COLUMN_NAME,
                COLUMN_DESCRIPTION,
                COLUMN_API_ID
        } ;

        public static final String[] COLUMNS_FOR_METRIC_QUERY = {
                CountryEntry.TABLE_NAME+ "." + CountryEntry.COLUMN_NAME,
                MetricEntry.TABLE_NAME + "." + _ID,
                MetricEntry.TABLE_NAME + "." + COLUMN_NAME,
                MetricEntry.TABLE_NAME + "." + COLUMN_DESCRIPTION,
                MetricEntry.TABLE_NAME + "." + COLUMN_API_ID
        } ;

    }

    public static final class DataPointEntry implements BaseColumns {


        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DATA_POINT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_DATA_POINT;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_DATA_POINT).build() ;

        public static Uri buildDataPointUri(long _ID) {
            return ContentUris.withAppendedId(CONTENT_URI, _ID);
        }


        public static final String TABLE_NAME = "data_point" ;
        public static final String COLUMN_COUNTRY_ID = "country_id" ;
        public static final String COLUMN_METRIC_ID = "metric_id" ;
        public static final String COLUMN_YEAR = "year" ;
        public static final String COLUMN_VALUE = "value" ;
    }


}
