package com.cmz_one.miznews;

import android.app.Application;

import com.cmz_one.miznews.DAO.DBManager;
import com.cmz_one.miznews.utils.SPUtils;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by cmz_o on 2016/12/13.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        SPUtils.init(this);
        DBManager.getInstance(this);
    }
}
