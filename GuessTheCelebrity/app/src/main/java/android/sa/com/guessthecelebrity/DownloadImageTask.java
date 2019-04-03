package android.sa.com.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;
        try{
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch ( MalformedURLException e){
            Log.e("Error","invalid url",e);
        }
        catch ( IOException e){
            Log.e("Error","IO error",e);
        }
        catch ( Exception e){
            Log.e("Error","unknown error",e);
        }

        return bitmap;
    }
}
