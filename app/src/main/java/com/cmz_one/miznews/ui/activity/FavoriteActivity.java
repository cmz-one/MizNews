package com.cmz_one.miznews.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.cmz_one.miznews.DAO.DBManager;
import com.cmz_one.miznews.DAO.News;
import com.cmz_one.miznews.OnItemClickListener;
import com.cmz_one.miznews.R;
import com.cmz_one.miznews.adapter.FavoriteRecyclerAdapter;
import com.cmz_one.miznews.adapter.NewsRecyclerAdapter;
import com.cmz_one.miznews.bean.NewsItemBean;
import com.cmz_one.miznews.helper.EndlessRecyclerOnScrollListener;

import java.util.List;

/**
 * Created by cmz_o on 2017/2/14.
 */

public class FavoriteActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView mRecyclerView;
    private FavoriteRecyclerAdapter mAdapter;
    private List<News> list;
    private ProgressBar progressBar;
    private Toolbar mToolbar;
    private int offset=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_favorite);
        initView();
        getNews();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("收藏夹");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px);

        progressBar = (ProgressBar) findViewById(R.id.favorite_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.favorite_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                ++offset;
                List<News> li = DBManager.getInstance(FavoriteActivity.this).queryNewsList(offset);
                list.addAll(li);
                mAdapter.notifyDataSetChanged();
                if (li.size()!=0){
                    Snackbar.make(mRecyclerView, "加载成功...", Snackbar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(mRecyclerView, "无更多内容", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = DBManager.getInstance(FavoriteActivity.this).queryNewsList(offset);
                mAdapter = new FavoriteRecyclerAdapter(FavoriteActivity.this, list);
                mAdapter.setOnItemClickListener(FavoriteActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);
            }
        }).run();

    }

    @Override
    public void onItemClick(int position, Object object) {
        Intent intent = new Intent(this, NewsActivity.class);
        Bundle bundle = new Bundle();
        News dataBean = (News) object;
        bundle.putString("uniquekey", dataBean.getId());
        bundle.putString("date", dataBean.getDate());
        bundle.putString("url", dataBean.getUrl());
        bundle.putString("img", dataBean.getThumbnail_pic_s());
        bundle.putString("imgUrl", dataBean.getThumbnail_pic_s02());
        bundle.putString("title", dataBean.getTitle());
        bundle.putString("author", dataBean.getAuthor_name());
        bundle.putString("category", dataBean.getCategory());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
