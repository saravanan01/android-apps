package android.sa.com.hackernews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

public class NewsArticleRecyclerViewAdapter extends RecyclerView.Adapter<NewsArticleRecyclerViewAdapter.NewsArticleViewHolder> {
    private List<Article> articles;

    public NewsArticleRecyclerViewAdapter (@NonNull List<Article> articles){
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =  layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
        return new NewsArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsArticleViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(articles.get(position).toString());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class NewsArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public NewsArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = Integer.valueOf(v.getTag().toString());
            Intent intent = new Intent(v.getContext(),WebActivity.class);
            intent.putExtra("newsUrl",articles.get(id).getUrl());
            v.getContext().startActivity(intent);
        }
    }
}
