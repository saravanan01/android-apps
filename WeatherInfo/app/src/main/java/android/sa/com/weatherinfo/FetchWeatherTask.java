package android.sa.com.weatherinfo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchWeatherTask extends AsyncTask<String,Void,OpenWeather> {
    @Override
    protected OpenWeather doInBackground(String... ids) {

        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;

        try{
            URL url = new URL( "https://samples.openweathermap.org/data/2.5/weather?appid=b6907d289e10d714a6e88b30761fae22&zip="+ ids[0]);
            connection = (HttpURLConnection)url.openConnection();
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuffer stringBuffer = new StringBuffer(500);
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line).append('\n');
            }
            JSONObject weather = new JSONObject(stringBuffer.toString());
            OpenWeather openWeather = new OpenWeather();
            openWeather.weatherType = weather.getJSONArray("weather").getJSONObject(0).getString("main");
            openWeather.weatherDescription = weather.getJSONArray("weather").getJSONObject(0).getString("description");
            openWeather.temp = weather.getJSONObject("main").getString("temp");
            openWeather.humidity = weather.getJSONObject("main").getString("humidity");
            openWeather.tempMin = weather.getJSONObject("main").getString("temp_min");
            openWeather.tempMax = weather.getJSONObject("main").getString("temp_max");
            openWeather.windSpeed = weather.getJSONObject("wind").getString("speed");
            openWeather.windGust = weather.getJSONObject("wind").getString("gust");

            return openWeather;

        }catch ( MalformedURLException e){
            Log.e("Error","invalid url",e);
        }
        catch ( IOException e){
            Log.e("Error","IO error",e);
        }
        catch ( Exception e){
            Log.e("Error","unknown error",e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                Log.e("Error","IO error closing bufferedReader",e);
            }catch (NullPointerException npe){ }

            try {
                inputStreamReader.close();
            } catch (IOException e) {
                Log.e("Error","IO error closing inputStreamReader",e);
            }catch (NullPointerException npe){ }
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("Error","IO error closing inputStream",e);
            }

            catch (NullPointerException npe){ }
            try{
                connection.disconnect();
            } catch (NullPointerException npe){ }
        }
        return null;
    }
}
