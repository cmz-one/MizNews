package com.cmz_one.miznews.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.Toast;

import com.cmz_one.miznews.OnItemClickListener;
import com.cmz_one.miznews.R;
import com.cmz_one.miznews.utils.SPUtils;
import com.cmz_one.miznews.adapter.SettingBottomRecyclerAdapter;
import com.cmz_one.miznews.adapter.SettingRecyclerAdapter;
import com.cmz_one.miznews.helper.ItemDragHelperCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cmz_o on 2016/12/19.
 */

public class SettingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView mBottomRecyclerView;
    private SettingRecyclerAdapter mAdapter;
    private SettingBottomRecyclerAdapter mBottomAdapter;
    private Toolbar mToolbar;
    private List<String> mTopList;
    private List<String> mBottomList;
    private List<String> mOldTopList;
    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_setting);
        initData();
        initView();
        listenView();
    }



    private void initData() {
        mTopList = new ArrayList<>();
        mBottomList = new ArrayList<>();
        String topTab = (String) SPUtils.get("thisTab", "");
        List<String> thisTab = Arrays.asList(topTab.split(","));
        thisTab = new ArrayList<>(thisTab);
        mTopList.addAll(thisTab);
        mOldTopList = new ArrayList<>(thisTab);
        String botomTab = (String) SPUtils.get("otherTab", "");
        if (!botomTab.equals("")){
            List<String> otherTab = Arrays.asList(botomTab.split(","));
            otherTab = new ArrayList<>(otherTab);
            mBottomList.addAll(otherTab);
        }


    }

    private void initView(){
        //初始化Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("MizNews");
        mToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //初始化RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.setting_RecyclerView);
        mBottomRecyclerView = (RecyclerView) findViewById(R.id.setting_bottom_RecyclerView);

        mAdapter = new SettingRecyclerAdapter(this,mTopList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragHelperCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mBottomAdapter = new SettingBottomRecyclerAdapter(this,mBottomList);
        mBottomRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mBottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBottomRecyclerView.setAdapter(mBottomAdapter);
    }

    private void listenView() {
        //顶部RecyclerView的Item点击事件
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (position != 0){
                    String str = mTopList.get(position);
                    mBottomList.add(str);
                    mTopList.remove(position);
                    mAdapter.setmList(mTopList);
                    mBottomAdapter.setmList(mBottomList);
                    mAdapter.notifyDataSetChanged();
                    mBottomAdapter.notifyDataSetChanged();
                }
            }
        });

        mBottomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                String str = mBottomList.get(position);
                mTopList.add(str);
                mBottomList.remove(position);
                mAdapter.setmList(mTopList);
                mBottomAdapter.setmList(mBottomList);
                mAdapter.notifyDataSetChanged();
                mBottomAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     *判断Tab栏是否有改变
     */
    private boolean isSaveTabData(){
        if (mOldTopList.size()==mTopList.size()){
            for (int i=0;i<mOldTopList.size();i++){
                if (!mOldTopList.get(i).equals(mTopList.get(i))){
                    return true;
                }
            }
            return false;
        }else {
            return true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isSaveTabData()){//如果tab栏目信息需要更新
            //生成字符串 储存新Tab栏信息
            String topStr="";
            for (int i=0;i<mTopList.size();i++){
                topStr += mTopList.get(i);
                if (i!=mTopList.size()-1){
                    topStr +=",";
                }
            }
            String bottomStr="";
            for (int i=0;i<mBottomList.size();i++){
                bottomStr += mBottomList.get(i);
                if (i!=mBottomList.size()-1){
                    bottomStr +=",";
                }
            }
            SPUtils.put("thisTab",topStr);
            SPUtils.put("otherTab",bottomStr);
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
            Intent intent = new Intent("com.cmz_one.miznews.SETTING_CHANGE");
            localBroadcastManager.sendBroadcast(intent);
            Toast.makeText(this,"设置成功",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //监听返回键
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
