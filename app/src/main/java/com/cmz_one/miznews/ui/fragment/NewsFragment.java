package com.cmz_one.miznews.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cmz_one.miznews.OnItemClickListener;
import com.cmz_one.miznews.R;
import com.cmz_one.miznews.adapter.NewsRecyclerAdapter;
import com.cmz_one.miznews.bean.NewsItemBean;
import com.cmz_one.miznews.http.RetrofitFactory;
import com.cmz_one.miznews.ui.activity.NewsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cmz_o on 2016/12/11.
 */

public class NewsFragment extends Fragment implements OnItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mView;
    private NewsItemBean bean;
    private NewsRecyclerAdapter mAdapter;
    private String type;
    private List<NewsItemBean.ResultBean.DataBean> list;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_news,null);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        initView();
        getNews();
        return mView;
    }

    private void initView(){
        progressBar = (ProgressBar) mView.findViewById(R.id.news_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.news_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NewsRecyclerAdapter(getContext(),list);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);


        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                getNews();
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(swipeRefreshLayout,"刷新成功",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getNews(){
        Call<NewsItemBean> call = RetrofitFactory.getInstance().getService().getNews("116e365ceb2c07d27e326c75ee29c237",type);
        call.enqueue(new Callback<NewsItemBean>() {
            @Override
            public void onResponse(Call<NewsItemBean> call, Response<NewsItemBean> response) {
                bean = response.body();
                list = bean.getResult().getData();
                mAdapter = new NewsRecyclerAdapter(getContext(),list);
                mAdapter.setOnItemClickListener(NewsFragment.this);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsItemBean> call, Throwable t) {


            }
        });
    }


    @Override
    public void onItemClick(int position, Object object) {
        Intent intent = new Intent(getContext(),NewsActivity.class);
        Bundle bundle = new Bundle();
        NewsItemBean.ResultBean.DataBean dataBean = (NewsItemBean.ResultBean.DataBean) object;
        bundle.putString("uniquekey",dataBean.getUniquekey());
        bundle.putString("date",dataBean.getDate());
        bundle.putString("url",dataBean.getUrl());
        bundle.putString("img",dataBean.getThumbnail_pic_s());
        bundle.putString("imgUrl",dataBean.getThumbnail_pic_s02());
        bundle.putString("title",dataBean.getTitle());
        bundle.putString("author",dataBean.getAuthor_name());
        bundle.putString("category",dataBean.getCategory());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
