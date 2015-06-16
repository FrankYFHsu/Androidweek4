package tw.edu.ncu.ce.networkprogramming.dbexample2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tw.edu.ncu.ce.networkprogramming.dbexample2.data.AQXContract;
import tw.edu.ncu.ce.networkprogramming.dbexample2.data.AQXDbHelper;
import tw.edu.ncu.ce.networkprogramming.dbexample2.model.AQXData;
import tw.edu.ncu.ce.networkprogramming.dbexample2.model.AQXSiteData;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getName();

    private Button mLoadAQXButton;
    private Button mShowQueryButton;
    private ListView mListView;
    private AQXDbHelper mDbHelper;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowQueryButton = (Button) findViewById(R.id.button3);
        mShowQueryButton.setEnabled(false);
        mLoadAQXButton = (Button) findViewById(R.id.button2);
        mLoadAQXButton.setEnabled(false);
        mListView = (ListView) findViewById(R.id.listView);
        mDbHelper = new AQXDbHelper(MainActivity.this);
        mEditText = (EditText) findViewById(R.id.editText);

    }

    public void temp() {
        final String AQX_BASE_URL =
                "http://opendata.epa.gov.tw/ws/Data/AQX/?";
        final String FILTER_PARAM = "$filter";
        final String FORMAT_PARAM = "format";
        Uri builtUri = Uri.parse(AQX_BASE_URL).buildUpon()
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(FILTER_PARAM, "SiteName eq '中壢'")
                .build();


    }


    public static String getJsonStringFromConnection(HttpURLConnection conn) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();

        return sb.toString();

    }


    public void downloadAQXSiteAndUpDb(View view) {
        new DownloadAQXSiteDbTask().execute();
    }

    public void downloadAQXAndUpDb(View view) {
        new DownloadAQXDbTask().execute();
    }

    public void queryAQXSite(View view) {
        new DbQueryTask().execute();

    }

    private class DownloadAQXDbTask extends AsyncTask<Void, Void, Void> {

        SQLiteDatabase db;

        public DownloadAQXDbTask() {
            db = mDbHelper.getWritableDatabase();
        }

        private long getLocationKey(String siteName) {


            String[] projection = {
                    AQXContract.AQXSiteEntry._ID,
                    AQXContract.AQXSiteEntry.COLUMN_SITE_NAME,
            };
            String[] selectionArgs = {siteName};

            Cursor c = db.query(
                    AQXContract.AQXSiteEntry.TABLE_NAME,            // The table to query
                    projection,                                      // The columns to return
                    AQXContract.AQXSiteEntry.COLUMN_SITE_NAME + "=?", // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            long itemID;

            if (c.moveToFirst()) {
                itemID = c.getLong(
                        c.getColumnIndexOrThrow(AQXContract.AQXSiteEntry._ID));
            } else {
                itemID = -1;
                Log.e(TAG, "itemID should not be -1!!");
            }

            return itemID;

        }

        @Override
        protected Void doInBackground(Void... voids) {

            String apiurl = "http://opendata.epa.gov.tw/ws/Data/AQX/?format=json";

            try {

                URL url = new URL(apiurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);


                if (conn.getResponseCode() == 200) {


                    String jsonString = getJsonStringFromConnection(conn);

                    Gson gson = new Gson();

                    AQXData[] data = gson.fromJson(jsonString, AQXData[].class);

                    for (int i = 0; i < data.length; i++) {

                        AQXData aqxSiteData = data[i];

                        String siteName = aqxSiteData.getSiteName();

                        Long itemID = getLocationKey(siteName);

                        ContentValues values = new ContentValues();
                        values.put(AQXContract.AQXEntry.COLUMN_LOC_KEY, itemID);
                        values.put(AQXContract.AQXEntry.COLUMN_CO, aqxSiteData.getCO());
                        values.put(AQXContract.AQXEntry.COLUMN_COUNTRY, aqxSiteData.getCounty());
                        values.put(AQXContract.AQXEntry.COLUMN_FPMI, aqxSiteData.getFPMI());
                        values.put(AQXContract.AQXEntry.COLUMN_MAJORPOLLUTANT, aqxSiteData.getMajorPollutant());
                        values.put(AQXContract.AQXEntry.COLUMN_NO2, aqxSiteData.getNO2());
                        values.put(AQXContract.AQXEntry.COLUMN_O3, aqxSiteData.getO3());
                        values.put(AQXContract.AQXEntry.COLUMN_PM10, aqxSiteData.getPM10());
                        values.put(AQXContract.AQXEntry.COLUMN_PM25, aqxSiteData.getPM2_5());
                        values.put(AQXContract.AQXEntry.COLUMN_PSI, aqxSiteData.getPSI());
                        values.put(AQXContract.AQXEntry.COLUMN_PUBLISH_TIME, aqxSiteData.getPublishTime());
                        values.put(AQXContract.AQXEntry.COLUMN_SITE_NAME, aqxSiteData.getSiteName());
                        values.put(AQXContract.AQXEntry.COLUMN_STATUS, aqxSiteData.getStatus());
                        values.put(AQXContract.AQXEntry.COLUMN_SO2, aqxSiteData.getSO2());
                        values.put(AQXContract.AQXEntry.COLUMN_WIND_DIREC, aqxSiteData.getWindDirec());
                        values.put(AQXContract.AQXEntry.COLUMN_WIND_SPEED, aqxSiteData.getWindSpeed());

                        long newRowId = db.insert(
                                AQXContract.AQXEntry.TABLE_NAME,
                                null,
                                values);

                    }


                }

                conn.disconnect();


            } catch (Exception e) {
                Log.e(TAG, "Exception for url:" + apiurl);
                Log.e(TAG, "Exception :" + e.getMessage());
            }
            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mShowQueryButton.setEnabled(true);

        }


    }


    private class DownloadAQXSiteDbTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {


            String apiurl = "http://opendata.epa.gov.tw/ws/Data/AQXSite/?format=json";

            try {

                URL url = new URL(apiurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);


                if (conn.getResponseCode() == 200) {
                    MainActivity.this.deleteDatabase(AQXDbHelper.DATABASE_NAME);
                    //為了測試，所以每次都會新的

                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    Log.d(TAG, "connect successful");
                    String jsonString = getJsonStringFromConnection(conn);

                    Gson gson = new Gson();

                    AQXSiteData[] data = gson.fromJson(jsonString, AQXSiteData[].class);

                    for (int i = 0; i < data.length; i++) {

                        AQXSiteData aqxSiteData = data[i];

                        ContentValues values = new ContentValues();
                        values.put(AQXContract.AQXSiteEntry.COLUMN_SITE_NAME, aqxSiteData.getSiteName());
                        values.put(AQXContract.AQXSiteEntry.COLUMN_SITE_ENG_NAME, aqxSiteData.getSiteEngName());
                        values.put(AQXContract.AQXSiteEntry.COLUMN_AREA_NAME, aqxSiteData.getAreaName());
                        values.put(AQXContract.AQXSiteEntry.COLUMN_COUNTRY, aqxSiteData.getCounty());
                        values.put(AQXContract.AQXSiteEntry.COLUMN_TOWNSHIP, aqxSiteData.getTownship());
                        values.put(AQXContract.AQXSiteEntry.COLUMN_SITE_ADDRESS, aqxSiteData.getSiteAddress());
                        values.put(AQXContract.AQXSiteEntry.COLUMN_TWD97LON, new Float(aqxSiteData.getTWD97Lon()));
                        values.put(AQXContract.AQXSiteEntry.COLUMN_TWD97LAT, new Float(aqxSiteData.getTWD97Lat()));
                        values.put(AQXContract.AQXSiteEntry.COLUMN_SITE_TYPE, aqxSiteData.getSiteType());


                        long newRowId = db.insert(
                                AQXContract.AQXSiteEntry.TABLE_NAME,
                                null,
                                values);

                    }


                }

                conn.disconnect();


            } catch (Exception e) {
                Log.e(TAG, "Exception :" + e.getMessage());
            }
            mDbHelper.close();
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            mLoadAQXButton.setEnabled(true);

        }


    }

    private class DbQueryTask extends AsyncTask<Void, Void, List<String>> {


        @Override
        protected List<String> doInBackground(Void... voids) {

            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            List<String> result = new ArrayList<>();
            String psiVale = mEditText.getText().toString();

            String[] selectionArgs = {psiVale};

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    AQXContract.AQXEntry._ID,
                    AQXContract.AQXEntry.COLUMN_SITE_NAME,
                    AQXContract.AQXEntry.COLUMN_PSI
            };

            String sortOrder =
                    AQXContract.AQXEntry.COLUMN_PSI + " DESC";


            Cursor c = db.query(
                    AQXContract.AQXEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    AQXContract.AQXEntry.COLUMN_PSI + " < ?",                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            if (c.moveToFirst()) {

                do {

                    long itemID = c.getLong(
                            c.getColumnIndexOrThrow(AQXContract.AQXEntry._ID));

                    String siteName = c.getString(c.getColumnIndexOrThrow(AQXContract.AQXEntry.COLUMN_SITE_NAME));
                    int psi = c.getInt(c.getColumnIndexOrThrow(AQXContract.AQXEntry.COLUMN_PSI));


                    result.add(itemID + ", " + siteName + ", " + psi);
                } while (c.moveToNext());


            }

            db.close();
            return result;
        }


        @Override
        protected void onPostExecute(List<String> result) {


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, result);

            mListView.setAdapter(adapter);
        }
    }
}
