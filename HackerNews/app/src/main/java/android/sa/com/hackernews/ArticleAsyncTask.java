package android.sa.com.hackernews;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleAsyncTask extends android.os.AsyncTask<List<String>, Void, List<Article>>  {
    private MainActivity  mainActivity;
    public ArticleAsyncTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<Article> doInBackground(List<String>... urls) {
        List<Article> articles = new ArrayList<>(urls.length);
        OkHttpClient okHttpClient = HttpHelper.getHttpClient();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        ExecutorCompletionService<Article> executorCompletionService = new ExecutorCompletionService<Article>(pool);
        for (String url: urls[0]) {
            executorCompletionService.submit(new Callable<Article>() {
                @Override
                public Article call() throws Exception {
                    Request request = new Request.Builder().url(url).build();
                    try (Response response = okHttpClient.newCall(request).execute()) {
                        String resp = response.body().string();
                        Gson gson = new Gson();
                        Article article = gson.fromJson(resp,Article.class);
                    return article;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        }
        for(int i = 0; i < urls[0].size(); ++i) {
            try {
                final Future<Article> future = executorCompletionService.take();
                final Article content = future.get();
                if(content != null){
                    articles.add(content);
                }
            } catch (ExecutionException e) {
                Log.w("Error while downloading","skipping this article..");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        mainActivity.updateUI(articles);
    }
}
