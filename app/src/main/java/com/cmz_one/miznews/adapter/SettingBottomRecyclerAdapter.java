package com.cmz_one.miznews.adapter;

import android.content.Context;
import android.support.transition.Visibility;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmz_one.miznews.OnItemClickListener;
import com.cmz_one.miznews.R;

import java.util.List;

/**
 * Created by cmz_o on 2016/12/24.
 */

public class SettingBottomRecyclerAdapter extends RecyclerView.Adapter<SettingBottomRecyclerAdapter.MyBottomViewHolder> {

    private Context context;
    private List<String> mList;
    private OnItemClickListener listener;

    public SettingBottomRecyclerAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public MyBottomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_setting,parent,false);
        return new MyBottomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyBottomViewHolder holder, final int position) {
        holder.imageView.setVisibility(View.GONE);
        holder.textView.setText(mList.get(position));
        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position,view);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyBottomViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public MyBottomViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_setting_item);
            imageView = (ImageView) itemView.findViewById(R.id.image_setting_item);
        }
    }
}
