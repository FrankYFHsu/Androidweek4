package tw.edu.ncu.ce.networkprogramming.dbexample;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tw.edu.ncu.ce.networkprogramming.dbexample.data.AQXContract;
import tw.edu.ncu.ce.networkprogramming.dbexample.data.AQXDbHelper;
import tw.edu.ncu.ce.networkprogramming.dbexample.model.AQXSiteData;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getName();

    private Button mShowQueryButton;
    private ListView mListView;
    private  AQXDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowQueryButton = (Button)findViewById(R.id.button2);
        mShowQueryButton.setEnabled(false);
        mListView = (ListView)findViewById(R.id.listView);
         mDbHelper = new AQXDbHelper(MainActivity.this);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void downloadAQXSiteAndUpDb(View view ){
        new DbInsertTask().execute();
    }

    public void queryAQXSite(View view){
        new DbQueryTask().execute();

    }


    private class DbInsertTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {


            String apiurl = "http://opendata.epa.gov.tw/ws/Data/AQXSite/?&format=json";

            try {

                URL url = new URL(apiurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);


                if (conn.getResponseCode() == 200) {
                    MainActivity.this.deleteDatabase(AQXDbHelper.DATABASE_NAME);//測試



                    //AQXDbHelper mDbHelper = new AQXDbHelper(MainActivity.this);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    Log.d(TAG, "connect successful");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    reader.close();

                    String jsonString = sb.toString();

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

                        long newRowId;
                        newRowId = db.insert(
                                AQXContract.AQXSiteEntry.TABLE_NAME,
                                null,
                                values);

                    }


                }

                conn.disconnect();


            } catch (Exception e) {
                Log.e(TAG, "Exception for url:" + apiurl);
                Log.e(TAG, "Exception :" + e.getMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            mShowQueryButton.setEnabled(true);

        }


    }

    private class DbQueryTask extends AsyncTask<Void, Void, List<String>> {


        @Override
        protected List<String> doInBackground(Void... voids) {

            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            List<String> result = new ArrayList<>();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    AQXContract.AQXSiteEntry._ID,
                    AQXContract.AQXSiteEntry.COLUMN_SITE_NAME,
                    AQXContract.AQXSiteEntry.COLUMN_TWD97LAT,
                    AQXContract.AQXSiteEntry.COLUMN_TWD97LON,
            };


            Cursor c = db.query(
                    AQXContract.AQXSiteEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if (c.moveToFirst()) {

                do {

                    long itemID = c.getLong(
                            c.getColumnIndexOrThrow(AQXContract.AQXSiteEntry._ID));
                    Log.d(TAG,"get itme:"+itemID);

                    String siteName = c.getString(c.getColumnIndexOrThrow(AQXContract.AQXSiteEntry.COLUMN_SITE_NAME));
                    float twd97Lat = c.getFloat(c.getColumnIndexOrThrow(AQXContract.AQXSiteEntry.COLUMN_TWD97LAT));
                    float twd97Lon = c.getFloat(c.getColumnIndexOrThrow(AQXContract.AQXSiteEntry.COLUMN_TWD97LON));

                    result.add(itemID+", "+siteName+", "+twd97Lat+", "+twd97Lon);
                }while (c.moveToNext());


            }


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
