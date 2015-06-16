
package tw.edu.ncu.ce.networkprogramming.dbexample.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tw.edu.ncu.ce.networkprogramming.dbexample.data.AQXContract.AQXEntry;
import tw.edu.ncu.ce.networkprogramming.dbexample.data.AQXContract.AQXSiteEntry;


/**
 * Manages a local database for weather data.
 */
public class AQXDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "aqx.db";

    public AQXDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_AQXSITE_TABLE = "CREATE TABLE " + AQXSiteEntry.TABLE_NAME + " (" +
                AQXSiteEntry._ID + " INTEGER PRIMARY KEY," +
                AQXSiteEntry.COLUMN_SITE_NAME + " TEXT NOT NULL, " +
                AQXSiteEntry.COLUMN_SITE_ENG_NAME + " TEXT NOT NULL, " +
                AQXSiteEntry.COLUMN_AREA_NAME + " TEXT NOT NULL, " +
                AQXSiteEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +
                AQXSiteEntry.COLUMN_TOWNSHIP + " TEXT NOT NULL, " +
                AQXSiteEntry.COLUMN_SITE_ADDRESS + " TEXT UNIQUE NOT NULL, " +
                AQXSiteEntry.COLUMN_TWD97LON + " REAL NOT NULL, " +
                AQXSiteEntry.COLUMN_TWD97LAT + " REAL NOT NULL, " +
                AQXSiteEntry.COLUMN_SITE_TYPE + " TEXT NOT NULL, " +
                " UNIQUE (" + AQXSiteEntry.COLUMN_SITE_NAME + ") ON CONFLICT REPLACE);";


        final String SQL_CREATE_AQX_TABLE = "CREATE TABLE " + AQXEntry.TABLE_NAME + " (" +

                AQXEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AQXEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                AQXEntry.COLUMN_SITE_NAME + " TEXT NOT NULL, " +
                AQXEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +
                AQXEntry.COLUMN_PSI + " INTEGER NOT NULL," +
                AQXEntry.COLUMN_MAJORPOLLUTANT + " TEXT, " +
                AQXEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                AQXEntry.COLUMN_SO2 + " REAL, " +
                AQXEntry.COLUMN_CO + " REAL, " +
                AQXEntry.COLUMN_O3 + " REAL, " +
                AQXEntry.COLUMN_PM10 + " INTEGER NOT NULL, " +
                AQXEntry.COLUMN_PM25 + " INTEGER NOT NULL, " +
                AQXEntry.COLUMN_NO2 + " REAL, " +
                AQXEntry.COLUMN_WIND_SPEED + " REAL , " +
                AQXEntry.COLUMN_WIND_DIREC + " INTEGER, " +
                AQXEntry.COLUMN_FPMI + " INTEGER NOT NULL, " +
                AQXEntry.COLUMN_PUBLISH_TIME + " TEXT NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + AQXEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                AQXSiteEntry.TABLE_NAME + " (" + AQXSiteEntry._ID + "), " +

                // To assure the application have just one AQX entry per location
                //  it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + AQXEntry.COLUMN_SITE_NAME + ", " +
                AQXEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_AQXSITE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_AQX_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AQXSiteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AQXEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
