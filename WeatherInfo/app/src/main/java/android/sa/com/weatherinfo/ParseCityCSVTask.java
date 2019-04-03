package android.sa.com.weatherinfo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

public class ParseCityCSVTask extends AsyncTask<InputStream,Void, ArrayList<String>> {

    @Override
    protected void onPostExecute(ArrayList<String> cities) {
        super.onPostExecute(cities);
    }

    @Override
    protected ArrayList<String> doInBackground(InputStream... is) {
        ArrayList<String> cities = new ArrayList<>(10000);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is[0], "UTF-8"));
            String line = null;
            while ( (line = reader.readLine()) != null) {
                cities.add(line);
            }
        } catch (IOException io) {
            Log.e("cityList","unable to read city list.");
            return null;
        }
        finally {
            try {
                is[0].close();
            } catch (IOException e) {
            }
        }

        return cities;
    }
}
