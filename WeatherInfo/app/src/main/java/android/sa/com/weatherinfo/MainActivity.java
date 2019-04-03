package android.sa.com.weatherinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> cityList;
    String selectedCity ="78717,TX";
    public void fetchWeather(View view){
        try {
            OpenWeather openWeather = new FetchWeatherTask().execute(selectedCity).get();
            TextView weatherTextView = findViewById(R.id.weatherTextView);
            weatherTextView.setText(openWeather.toString());
        }catch (Exception e) {
            Log.e("Error",e.getMessage(),e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            cityList = new ParseCityCSVTask().
                    execute(getResources().openRawResource(R.raw.zipcity))
                    .get();
        }catch (Exception e){
            Log.e("Error",e.getMessage(),e);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,cityList);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.cityAutoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(5);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                selectedCity = (String) arg0.getAdapter().getItem(arg2);

            }
        });
        Log.i("test","test");
    }


}
