
package tw.edu.ncu.ce.networkprogramming.dbexample.data;


import android.provider.BaseColumns;

/**
 * Defines table and column names for the aqx database.
 */
public class AQXContract {

    /*
        Inner class that defines the table contents of the AQXSite table
     */
    public static final class AQXSiteEntry implements BaseColumns {

        public static final String TABLE_NAME = "aqxsite";
        public static final String COLUMN_SITE_NAME = "site_name";
        public static final String COLUMN_SITE_ENG_NAME = "site_engname";
        public static final String COLUMN_AREA_NAME = "area_name";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_TOWNSHIP = "township";
        public static final String COLUMN_SITE_ADDRESS = "site_address";
        public static final String COLUMN_TWD97LON = "TWD97_Lon";
        public static final String COLUMN_TWD97LAT = "TWD97_Lat";
        public static final String COLUMN_SITE_TYPE = "site_type";
    }

    /* Inner class that defines the table contents of the AQX table */
    public static final class AQXEntry implements BaseColumns {

        public static final String TABLE_NAME = "aqx";
        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_SITE_NAME = "site_name";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_PSI = "psi";
        public static final String COLUMN_MAJORPOLLUTANT = "major_pullutant";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_SO2= "so2";
        public static final String COLUMN_CO = "co";
        public static final String COLUMN_O3 = "o3";
        public static final String COLUMN_PM10= "pm10";
        public static final String COLUMN_PM25= "pm25";
        public static final String COLUMN_NO2= "no2";
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        public static final String COLUMN_WIND_DIREC= "wind_direc";
        public static final String COLUMN_FPMI= "fpmi";

        //(e.g, 2015-06-09 09:00)
        public static final String COLUMN_PUBLISH_TIME = "publishtime";
    }
}
