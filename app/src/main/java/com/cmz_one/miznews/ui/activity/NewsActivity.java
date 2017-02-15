package com.cmz_one.miznews.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cmz_one.miznews.DAO.DBManager;
import com.cmz_one.miznews.DAO.News;
import com.cmz_one.miznews.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * Created by cmz_o on 2016/12/14.
 */

public class NewsActivity extends AppCompatActivity {

    private WebView webView;
    private String url;
    private String imgUrl;
    private String title;
    private String uniquekey;
    private String date;
    private String img;
    private String author;
    private String category;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private SimpleDraweeView mSimpleDraweeView;
    private boolean isFavourite=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_news);
        url = getIntent().getExtras().getString("url");
        imgUrl = getIntent().getExtras().getString("imgUrl");
        title = getIntent().getExtras().getString("title");
        uniquekey = getIntent().getExtras().getString("uniquekey");
        date = getIntent().getExtras().getString("date");
        img = getIntent().getExtras().getString("img");
        author = getIntent().getExtras().getString("author");
        category = getIntent().getExtras().getString("category");
        ShareSDK.initSDK(this, "1a3a4686ecf60");
        initToolbar();
        initView();
        getFavorite();
    }

    private void initView() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.simpleDraweeView);
        if (imgUrl != null) {//API接口有时没有返回图片地址，在有返回时加载图片
            Uri uri = Uri.parse(imgUrl);
            mSimpleDraweeView.setImageURI(uri);
        }

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);//app内加载网页
                return true;
            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_news);
        mToolbar.setTitle("Miz新闻");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_news_share:
                        showShare();//展开分享界面
                        break;
                    case R.id.menu_news_star:
                        onFavorite();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isFavourite){
            menu.findItem(R.id.menu_news_star).setIcon(R.drawable.ic_star_24px);
        }else {
            menu.findItem(R.id.menu_news_star).setIcon(R.drawable.ic_star_outline_24px);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
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

    /**
     * 进入页面检测该则新闻有无被收藏
     * */
    private void getFavorite(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<News> list = DBManager.getInstance(NewsActivity.this).queryNewsList(uniquekey);
                Log.d("【getFavorite】",""+list.size());
                if (list.size()!=0){
                    isFavourite = true;
                    invalidateOptionsMenu();//刷新收藏按钮
                }
            }
        }).run();
    }

    /**
     * 收藏按钮点击事件
     * */
    private void onFavorite(){
        if (isFavourite){
            //取消收藏
            isFavourite = false;
            DBManager.getInstance(NewsActivity.this).deleteNews(uniquekey);
            Snackbar.make(mToolbar,"取消成功",Snackbar.LENGTH_SHORT).show();
        }else {
            //收藏
            isFavourite = true;
            News news = new News(uniquekey,title,date,category,author,url,img,imgUrl);
            DBManager.getInstance(NewsActivity.this).insertNews(news);
            Snackbar.make(mToolbar,"收藏成功",Snackbar.LENGTH_SHORT).show();
        }
        invalidateOptionsMenu();//刷新收藏按钮
    }

    /**
     * 分享界面
     * */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
// text是分享文本，所有平台都需要这个字段
        oks.setText(title);
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        oks.setImageUrl(imgUrl);
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

// 启动分享GUI
        oks.show(this);
    }
}
