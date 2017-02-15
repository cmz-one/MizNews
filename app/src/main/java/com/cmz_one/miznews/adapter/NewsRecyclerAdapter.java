package com.cmz_one.miznews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmz_one.miznews.OnItemClickListener;
import com.cmz_one.miznews.bean.NewsItemBean;
import com.cmz_one.miznews.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * Created by cmz_o on 2016/12/13.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<NewsItemBean.ResultBean.DataBean> list;
    private OnItemClickListener listener;

    public NewsRecyclerAdapter(Context context,List<NewsItemBean.ResultBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_news,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final NewsItemBean.ResultBean.DataBean dataBean = list.get(position);
        holder.title.setText(dataBean.getTitle());
        Uri uri = Uri.parse(dataBean.getThumbnail_pic_s());
        holder.draweeView.setImageURI(uri);
        holder.author.setText(dataBean.getAuthor_name());
        holder.time.setText(dataBean.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position, dataBean);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView draweeView;
        private TextView title;
        private TextView author;
        private TextView time;


        public MyViewHolder(View view) {
            super(view);
            draweeView = (SimpleDraweeView) view.findViewById(R.id.iv_news_image);
            title = (TextView) view.findViewById(R.id.tv_news_title);
            author = (TextView) view.findViewById(R.id.tv_news_author);
            time = (TextView) view.findViewById(R.id.tv_news_time);
        }
    }
}
