package android.sa.com.hackernews;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static android.sa.com.hackernews.HttpHelper.TOP_STORIES_URL;

public class MainActivity extends AppCompatActivity {
    private List<Article> articles = new ArrayList<>(20);
    private NewsArticleRecyclerViewAdapter newsArticleRecyclerViewAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabReload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Loading new stories now..", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                loadData(25);
            }
        });

        recyclerView = findViewById(R.id.newListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsArticleRecyclerViewAdapter = new NewsArticleRecyclerViewAdapter(articles);
        recyclerView.setAdapter(newsArticleRecyclerViewAdapter);
        loadData(5);
    }



    private void loadData(int max) {
        new ArticleListAsyncTask(this,max).execute(TOP_STORIES_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reload) {
            loadData(50);
            return true;
        }
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateUI(List<Article> articles){
        this.articles.clear();
        this.articles.addAll(articles);
        newsArticleRecyclerViewAdapter.notifyDataSetChanged();
    }
}
