package android.sa.com.memorableplaces;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimingLogger;

import com.google.android.gms.common.util.Base64Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TIME_TAG = "Time_tag" ;
    private static List<Place> places = new ArrayList<>();
    static private PlaceAdapter placeAdapter;
    static private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(places.size() < 1) {
            places.add(new Place(Constants.ADD_AN_ENTRY));

            sharedPreferences = getSharedPreferences("android.sa.com.memorableplaces",MODE_PRIVATE);
            if( sharedPreferences.contains("data_places") ) {
                decodePlaces(sharedPreferences.getString("data_places", null));
            }
        }

        placeAdapter = new PlaceAdapter(places);
        recyclerView.setAdapter(placeAdapter);
    }

    private static String encodePlaces() {

        int count = 0;
        TimingLogger timings = new TimingLogger(TIME_TAG, "encodePlaces");
        StringBuilder sb = new StringBuilder(500);
        for (Place place: places) {
            if( ! place.getAddress().equals(Constants.ADD_AN_ENTRY)) {
                timings.addSplit("work "+ count++);
                sb.append(
                        Base64Utils.encode(place.getAddress().getBytes()))
                        .append(",")
                        .append(place.getLatitude())
                        .append(',')
                        .append(place.getLongitude())
                        .append("~~~");
            }
        }
        timings.dumpToLog();
        return sb.toString();
    }

    private void decodePlaces(String str) {
        if(str!=null) {
            TimingLogger timings = new TimingLogger(TIME_TAG, "decodePlaces");
            String[] splits = str.split("~~~");
            for (int i=0;i<splits.length;i++) {
                String[] it = splits[i].split(",");
                places.add(
                 new Place(
                         new String(Base64Utils.decode(it[0])),
                         Double.valueOf(it[1]),
                         Double.valueOf(it[2]) )
                );
                timings.addSplit("work "+ i);
            }
            timings.dumpToLog();
        }
    }

    static void addPlace(Place p) {
        places.add(p);
        placeAdapter.notifyItemInserted(places.size());
        sharedPreferences.edit().
                putString("data_places",encodePlaces()).apply();

    }

    static void removePlace(int pos) {
        places.remove(pos);
        placeAdapter.notifyItemRemoved(pos);
        sharedPreferences.edit().
                putString("data_places",encodePlaces()).apply();

    }

    static List<Place> getAllPlace() {
        return places;
    }

}
