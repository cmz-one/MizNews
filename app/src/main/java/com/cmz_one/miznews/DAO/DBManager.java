package com.cmz_one.miznews.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anye.greendao.gen.DaoMaster;
import com.anye.greendao.gen.DaoSession;
import com.anye.greendao.gen.NewsDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by cmz_o on 2017/2/14.
 */

public class DBManager {
    private final static String DBNAME = "test_db";//数据库名
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    private DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, DBNAME, null);
    }

    /**
     * 获取单例
     * */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     * */
    private SQLiteDatabase getReadableDatabase() {
        return openHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     * */
    private SQLiteDatabase getWritableDatabase() {
        return openHelper.getWritableDatabase();
    }

    /**
     * 插入数据
     * */
    public void insertNews(News news){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsDao newsDao = daoSession.getNewsDao();
        newsDao.insert(news);
    }

    /**
     * 插入数据集合
     * */
    public void insertNewsList(List<News> news){
        if(news == null || news.isEmpty()){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsDao newsDao = daoSession.getNewsDao();
        newsDao.insertInTx(news);
    }

    /**
     * 删除指定主键数据
     * */
    public void deleteNews(String  id){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsDao newsDao = daoSession.getNewsDao();
        newsDao.deleteByKey(id);
    }

    /**
     * 查询数据集
     * */
    public List<News> queryNewsList(int offset) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsDao newsDao = daoSession.getNewsDao();
        QueryBuilder<News> qb = newsDao.queryBuilder();
        qb.orderDesc(NewsDao.Properties.Date).offset(offset*10).limit(10);
        List<News> list = qb.list();
        return list;
    }

    public List<News> queryNewsList(String id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsDao newsDao = daoSession.getNewsDao();
        QueryBuilder<News> qb = newsDao.queryBuilder();
        qb.where(NewsDao.Properties.Id.eq(id));
        List<News> list = qb.list();
        return list;
    }




}
