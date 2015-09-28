package com.i2r.alan.rate_this_place.database;

import android.provider.BaseColumns;

/**
 * Created by Alan on 23/9/2015.
 */
public final class DBContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.



    public DBContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "popuplocations";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_LOCATION_NAME = "locationname";
        public static final String COLUMN_LOCATION_LATITUDE = "latitude";
        public static final String COLUMN_LOCATION_LONGITUDE = "longitude";
        public static final String COLUMN_RATING_STATUS = "rating_status";
    }
}
