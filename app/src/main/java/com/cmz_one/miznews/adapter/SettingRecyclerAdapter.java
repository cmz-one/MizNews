package com.cmz_one.miznews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmz_one.miznews.OnItemClickListener;
import com.cmz_one.miznews.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by cmz_o on 2016/12/20.
 */

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private OnItemClickListener listener;
    private List<String> mList;


    public SettingRecyclerAdapter(Context context,List<String> list) {
        this.context = context;
        this.mList = list;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public SettingRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_setting,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingRecyclerAdapter.MyViewHolder holder, final int position) {
        holder.textView.setText(mList.get(position));
        if (position==0){
            holder.imageView.setVisibility(View.GONE);
        }
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
        return mList==null ? 0 : mList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        /**
         * 拖拽后，切换位置，数据排序
         */
        Collections.swap(mList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        /**
         * 移除之前的数据
         */
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_setting_item);
            imageView = (ImageView) itemView.findViewById(R.id.image_setting_item);
        }
    }

}
