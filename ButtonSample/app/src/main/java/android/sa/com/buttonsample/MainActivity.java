package android.sa.com.buttonsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public void loginAction(View view){
        Log.i("login", "Login pressed");
        String username = ((EditText)findViewById(R.id.usernameEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        Log.i("login",username);
        if(username.equals("sa") && password.equals("ss")) {
            Intent myIntent = new Intent(this, HomeActivity.class);
            myIntent.putExtra("Name", username);
            startActivity(myIntent);
            finish();
        }
//        Log.i("login",password);
//        Toast.makeText(view.getContext(), String.format("hello %s", username), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
