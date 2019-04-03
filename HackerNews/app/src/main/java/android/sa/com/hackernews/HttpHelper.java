package android.sa.com.hackernews;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class HttpHelper {

    public static final String ARTICLE_URL_TEMPLATE = "https://hacker-news.firebaseio.com/v0/item/%d.json";
    public static final String TOP_STORIES_URL = "https://hacker-news.firebaseio.com/v0/topstories.json";

    private static OkHttpClient okHttpClient;
    static {
        okHttpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(100,TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool( 50, 2, TimeUnit.MINUTES))
                .build();
    }
    public static OkHttpClient getHttpClient(){
        return okHttpClient;
    }
}
