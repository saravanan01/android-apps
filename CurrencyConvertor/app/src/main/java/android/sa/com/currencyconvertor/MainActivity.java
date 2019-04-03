package android.sa.com.currencyconvertor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {


    public void convertToInr(View view) {
        Log.i("currency", "convert usd");
        EditText usdEditText = (EditText) findViewById(R.id.currencyUsdEditText);
        String usdStr = usdEditText.getText().toString();
        BigDecimal usd = new BigDecimal(usdStr);
        String inr = usd.multiply(BigDecimal.valueOf(69.77)).toPlainString();
        Toast.makeText(this, String.format("$%s is Rs.%.2f", usdStr, Double.valueOf(inr)), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
