package com.cmz_one.miznews.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.cmz_one.miznews.adapter.SettingRecyclerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by cmz_o on 2016/12/20.
 */

public class ItemDragHelperCallback extends ItemTouchHelper.Callback {

    private SettingRecyclerAdapter mAdapter;

    public ItemDragHelperCallback(SettingRecyclerAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //拖拽
        int dragFlags = ItemTouchHelper.UP   | ItemTouchHelper.DOWN |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        //滑动
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position

        if(fromPosition==0){
            return false;
        }
        mAdapter.onItemMove(fromPosition,toPosition);
        //返回true表示执行拖动
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.onItemDismiss(position);
    }
}
