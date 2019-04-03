package android.sa.com.hackernews;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.sa.com.hackernews.HttpHelper.ARTICLE_URL_TEMPLATE;

public class ArticleListAsyncTask extends AsyncTask<String,Void, List<Long>> {
    private MainActivity mainActivity;
    private int max = 50;

    public ArticleListAsyncTask(MainActivity mainActivity, int max) {
        this.mainActivity = mainActivity;
        if(max < 50){
            this.max = max;
        }
    }

    @Override
    protected List<Long> doInBackground(String... urls) {
        OkHttpClient okHttpClient = HttpHelper.getHttpClient();
        Request request = new Request.Builder().url(urls[0]).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            JsonReader jsonReader = new Gson().newJsonReader(response.body().charStream());
            jsonReader.beginArray();
            List<Long> articles = new ArrayList<>(50);
            while (jsonReader.hasNext()){
                articles.add( jsonReader.nextLong() );
            }
            jsonReader.close();
            return articles;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Long> articleIds) {
        if(articleIds!= null && articleIds.size() > 0) {
            List<String> urls = new ArrayList<>(max);
            for (int i = 0; i < max; i++) {
                Long id = articleIds.get(i);
                urls.add(String.format(ARTICLE_URL_TEMPLATE, id));
            }
            new ArticleAsyncTask(mainActivity).execute(urls);
        }
    }
}
