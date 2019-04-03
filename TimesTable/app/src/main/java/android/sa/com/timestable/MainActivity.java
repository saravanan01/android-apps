package android.sa.com.timestable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int SEEK_MAX = 20;
    private final int SEEK_MIN = 1;
    private List<String> ttList= new ArrayList<String>(10);;
    private int selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.ttListView);
        final ArrayAdapter listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());

        listView.setAdapter(listAdapter);
        SeekBar seekBar = (SeekBar) findViewById(R.id.ttSeekBar);
        seekBar.setMax(SEEK_MAX);
        selected = SEEK_MAX /2;
        seekBar.setProgress(selected);
        listAdapter.addAll(buildData(selected));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < SEEK_MIN) {
                    progress = SEEK_MIN;
                    selected = progress;
                    seekBar.setProgress(progress);
                }
//                Log.i("progress", String.valueOf(progress));
                listAdapter.clear();
                listAdapter.addAll(buildData(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = ((AppCompatTextView) view).getText().toString();
                showToast(value);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    private List<String> buildData(int val) {
        final int LIST_UNTIL = 10;
        ttList.clear();
        for(int i = 1; i <= LIST_UNTIL ; i++) {
            String msg = String.format("%d * %d = %s", selected,i,val);
            ttList.add(msg);
        }
        return ttList;
    }

}
