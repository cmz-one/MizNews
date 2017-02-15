package com.cmz_one.miznews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmz_one.miznews.DAO.News;
import com.cmz_one.miznews.OnItemClickListener;
import com.cmz_one.miznews.R;
import com.cmz_one.miznews.bean.NewsItemBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmz_o on 2017/2/15.
 */

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder> {
    private Context context;
    private List<News> list;
    private OnItemClickListener listener;

    public FavoriteRecyclerAdapter(Context context,List<News> list) {
        this.context = context;
        this.list = list==null ? new ArrayList<News>() : list;
    }

    @Override
    public FavoriteRecyclerAdapter.FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_news,parent,false);
        return new FavoriteRecyclerAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteRecyclerAdapter.FavoriteViewHolder holder, final int position) {

        final News news = list.get(position);
        holder.title.setText(news.getTitle());
        Uri uri = Uri.parse(news.getThumbnail_pic_s());
        holder.draweeView.setImageURI(uri);
        holder.author.setText(news.getAuthor_name());
        holder.time.setText(news.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position, news);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }




    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView draweeView;
        private TextView title;
        private TextView author;
        private TextView time;


        public FavoriteViewHolder(View view) {
            super(view);
            draweeView = (SimpleDraweeView) view.findViewById(R.id.iv_news_image);
            title = (TextView) view.findViewById(R.id.tv_news_title);
            author = (TextView) view.findViewById(R.id.tv_news_author);
            time = (TextView) view.findViewById(R.id.tv_news_time);
        }
    }
}
