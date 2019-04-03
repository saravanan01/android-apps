package android.sa.com.guessthecelebrity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadHtmlTask extends AsyncTask<String,Void, ArrayList<Celebrity>> {
    @Override
    protected ArrayList<Celebrity> doInBackground(String... strings) {
        ArrayList<Celebrity> celebrities = new ArrayList<>(100);
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;

        try{
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection)url.openConnection();
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuffer stringBuffer = new StringBuffer(500);
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line).append('\n');
            }
            String[] lines = stringBuffer.toString().split("\n");
//						<img src="http://cdn.posh24.se/images/:profile/c/50012" alt="Jessica Simpson"/>
            for (String s: lines) {
                if (s.trim().contains("<img src=\"http://cdn.posh24.se/images/:profile")
                  && s.trim().contains("alt=") ) {
                    celebrities.add(buildCelebrity(s.trim()));
                }
            }

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


        return celebrities;
    }
    final Pattern PATTERN_IMG = Pattern.compile("src=\"(.*)\" ");
    final Pattern PATTERN_NAME = Pattern.compile("alt=\"(.*)\"");
    private Celebrity buildCelebrity(String imgStr) {
        Celebrity celebrity = new Celebrity();
//						<img src="http://cdn.posh24.se/images/:profile/c/50012" alt="Jessica Simpson"/>
        Matcher matcher = PATTERN_IMG.matcher(imgStr);
        while (matcher.find()) {
            celebrity.setImgUrl(matcher.group(1));
        }
        matcher = PATTERN_NAME.matcher(imgStr);
        while (matcher.find()) {
            celebrity.setName(matcher.group(1));
        }
        return celebrity;
    }
}
