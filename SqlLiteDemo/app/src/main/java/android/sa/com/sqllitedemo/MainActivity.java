package android.sa.com.sqllitedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase database = this.openOrCreateDatabase("sqllitedemo",MODE_PRIVATE, null );
        database.execSQL("CREATE TABLE IF NOT EXISTS user (name VARCHAR,age int(3))");
        boolean insert = true;
        Cursor cursor = database.rawQuery("select count(1) from user", null);
        if(cursor!=null && cursor.moveToFirst()) {
            if((cursor.getInt(0) > 0)){
                insert = false;
            }
        }
        cursor.close();
        if(insert) {
            database.execSQL("INSERT into user VALUES('sa',32)");
        }
        cursor = database.rawQuery("select name from user", null);
        if(cursor!=null && cursor.moveToFirst()){
            System.out.println(cursor.getString(0)+" <--------------");
        }
        cursor.close();
    }
}
