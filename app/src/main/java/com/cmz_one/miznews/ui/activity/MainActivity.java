package com.cmz_one.miznews.ui.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


import com.cmz_one.miznews.R;
import com.cmz_one.miznews.utils.SPUtils;
import com.cmz_one.miznews.adapter.MyPagerAdapter;
import com.cmz_one.miznews.ui.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MyPagerAdapter mAdapter;
    private List<String> mTitle;
    private List<Fragment> mViews;
    private LocalBroadcastManager localBroadcastManager;
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        firstInit();
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mTitle, mViews);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cmz_one.miznews.SETTING_CHANGE");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(receiver,intentFilter);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Miz新闻");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        imageButton = (ImageButton) findViewById(R.id.btn_setting_add);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //DrawerLayout打开监听
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //DrawerLayout关闭监听
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_home:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.navigation_item_setting://设置按钮
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_favorite://设置按钮
                        Intent i = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初次启动app时创建保存版本号和tab栏信息的sp文件
     */
    private void firstInit() {
        int nowVersion = 1;//当前版本号
        try {
            nowVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("nowVersionCode：", nowVersion + "");

        int spVersionCode = (int) SPUtils.get("spVersionCode", 0);
        Log.d("spVersionCode：", spVersionCode + "");

        if (nowVersion > spVersionCode) {
            //此时为app首次启动,写入配置信息
            SPUtils.put("spVersionCode", nowVersion);
            String[] title = getResources().getStringArray(R.array.tab);
            String[] type = getResources().getStringArray(R.array.tabType);
            String tab = "";//使用","做分隔符，使得单个sp key可以储存多个tab栏信息
            for (int i = 0; i < title.length; i++) {
                if (i == 0) {
                    tab += title[i];
                } else {
                    tab += "," + title[i];
                }
                SPUtils.put(title[i], type[i]);//存入对应Tab栏对应的请求type
            }
            SPUtils.put(title[0], type[0]);
            SPUtils.put("thisTab", tab);

            mTitle = new ArrayList<>();
            mViews = new ArrayList<>();
            //首次启动直接使用resources创建tab栏
            for (int i = 0; i < 6; i++) {
                mTitle.add(title[i]);
                NewsFragment newsFragment = new NewsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", type[i]);
                newsFragment.setArguments(bundle);
                mViews.add(newsFragment);
            }

        } else {
            //从sp配置文件获取tab栏并配置
            initTab();
        }
    }

    /**
     * 从sp配置文件获取tab栏并配置
     */
    private void initTab() {
         mTitle = new ArrayList<>();
         mViews = new ArrayList<>();

        //获取到可操作的tab频道对应的title
        String tab = (String) SPUtils.get("thisTab", "");
        List<String> thisTab = Arrays.asList(tab.split(","));
        thisTab = new ArrayList<>(thisTab);
        if (thisTab.size() != 0) {
            mTitle.addAll(thisTab);
            for (int i = 0; i < thisTab.size(); i++) {
                String type = (String) SPUtils.get(mTitle.get(i), "");
                NewsFragment newsFragments = new NewsFragment();
                Bundle bundles = new Bundle();
                bundles.putString("type", type);
                newsFragments.setArguments(bundles);
                mViews.add(newsFragments);
            }
        }


    }

    //使用本地广播方式接收，如设置发生改变则刷新界面
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initTab();
            mAdapter.setAdapterTitle(mTitle);
            mAdapter.setAdapterViews(mViews);
            mAdapter.notifyDataSetChanged();
            Snackbar.make(imageButton, "修改设置成功", Snackbar.LENGTH_SHORT)
                    .setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //这里的单击事件代表点击消除Action后的响应事件
                        }
                    }).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
    }
}
