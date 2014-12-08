package com.team1_k.project.seg.dataviz.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.team1_k.project.seg.dataviz.model.DataPoint;

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


    /**
     * Class that represents the Country model in the database. This holds all of the logic we
     * use when querying the database, from the column names to the indexes at which the columns
     * are to be found when we query.
     */
    public static final class CountryEntry implements BaseColumns {

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;

        public static final String TABLE_NAME = "country" ;
        public static final String COLUMN_NAME = "name" ;
        public static final String COLUMN_API_ID = "api_id" ;
        public static final String COLUMN_LONGITUDE = "longitude" ;
        public static final String COLUMN_LATITUDE = "latitude" ;
        public static final String COLUMN_CAPITAL_CITY = "capital_city" ;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_COUNTRY).build();

        /**
         * Builds an URI for the country entity with the given ID
         * @param _ID the id for which we want to build the URI
         * @return Built {@link java.net.URI}
         */
        public static Uri buildCountryUri(long _ID){
            return ContentUris.withAppendedId(CONTENT_URI, _ID) ;
        }

        /**
         * Builds an URI for countries & metrics query
         * @return Built {@link java.net.URI}
         */
        public static Uri buildCountriesWithMetricsUri(){
            return CONTENT_URI.buildUpon().
                    appendEncodedPath("metrics").build();
        }

        /**
         * Builds an URI for a specified country and all of its metrics
         * @param _ID the ID of the country we want to fetch metrics for
         * @return Built {@link java.net.URI}
         */
        public static Uri buildCountryWithMetricsUriWithId(long _ID){
            return CONTENT_URI.buildUpon().
                    appendEncodedPath("metrics").
                    appendEncodedPath(String.valueOf(_ID)).build();
        }

        /**
         * Builds an URI for a specified country and a specified metric
         * @param country_id ID of the {@link com.team1_k.project.seg.dataviz.model.Country}
         * @param metric_id ID of the {@link com.team1_k.project.seg.dataviz.model.Metric}
         * @return Built {@link java.net.URI}
         */
        public static Uri buildCountryWithMetricId(long country_id, long metric_id){
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(country_id))
                    .appendEncodedPath("metrics").appendEncodedPath(String.valueOf(metric_id))
                    .build();
        }

        /**
         * Used to notify a country of an update in its metrics.
         * @return Built {@link java.net.URI}
         */
        public static Uri buildCountryWithMetricUri() {
            return CONTENT_URI.buildUpon().
                    appendEncodedPath("metrics").build();
        }

        /**
         * The list of columns of the entity.
         */
        public static final String[] COLUMNS = {
                _ID,
                COLUMN_NAME,
                COLUMN_API_ID,
                COLUMN_CAPITAL_CITY,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE
        };

        public static final int INDEX_COLUMN_ID = 0 ;
        public static final int INDEX_COLUMN_NAME = 1 ;
        public static final int INDEX_COLUMN_API_ID = 2 ;
        public static final int INDEX_COLUMN_CAPITAL_CITY = 3;
        public static final int INDEX_COLUMN_LATITUDE = 4 ;
        public static final int INDEX_COLUMN_LONGITUDE = 5 ;
    }

    /**
     * The metric entity which holds all of the database logic for
     * {@link com.team1_k.project.seg.dataviz.model.Metric}.
     */
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

        public static final int INDEX_COLUMN_ID = 0 ;
        public static final int INDEX_COLUMN_NAME = 1 ;
        public static final int INDEX_COLUMN_DESCRIPTION = 2 ;
        public static final int INDEX_COLUMN_API_ID = 3 ;


        public static final String[] COLUMNS_FOR_METRIC_QUERY = {
                CountryEntry.TABLE_NAME+ "." + CountryEntry.COLUMN_NAME,
                MetricEntry.TABLE_NAME + "." + _ID,
                MetricEntry.TABLE_NAME + "." + COLUMN_NAME,
                MetricEntry.TABLE_NAME + "." + COLUMN_DESCRIPTION,
                MetricEntry.TABLE_NAME + "." + COLUMN_API_ID,
                DataPointEntry.TABLE_NAME + "." + DataPointEntry.COLUMN_YEAR,
                DataPointEntry.TABLE_NAME + "." + DataPointEntry.COLUMN_VALUE,
                CountryEntry.TABLE_NAME + "." + CountryEntry.COLUMN_API_ID,
                CountryEntry.TABLE_NAME + "." + CountryEntry._ID
        } ;

        public static final int INDEX_METRIC_QUERY_COLUMN_COUNTRY_NAME = 0 ;
        public static final int INDEX_METRIC_QUERY_COLUMN_METRIC_ID = 1 ;
        public static final int INDEX_METRIC_QUERY_COLUMN_NAME = 2 ;
        public static final int INDEX_METRIC_QUERY_COLUMN_DESCRIPTION = 3 ;
        public static final int INDEX_METRIC_QUERY_COLUMN_API_ID = 4 ;
        public static final int INDEX_METRIC_QUERY_COLUMN_YEAR = 5;
        public static final int INDEX_METRIC_QUERY_COLUMN_VALUE = 6 ;
        public static final int INDEX_METRIC_QUERY_COLUMN_COUNTRY_API_ID = 7;
        public static final int INDEX_METRIC_QUERY_COLUMN_COUNTRY_ID = 8 ;
    }

    /**
     * Holds all of the database logic for {@link com.team1_k.project.seg.dataviz.model.DataPoint}.
     */
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
