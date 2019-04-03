package android.sa.com.buttonsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        ((TextView)findViewById(R.id.homeTextView)).setText(String.format("Hello %s", name));
    }
}
