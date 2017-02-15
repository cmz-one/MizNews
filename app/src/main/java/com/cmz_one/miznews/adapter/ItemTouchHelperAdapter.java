package com.cmz_one.miznews.adapter;

/**
 * Created by cmz_o on 2016/12/22.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition,int toPosition);
    void onItemDismiss(int position);
}
