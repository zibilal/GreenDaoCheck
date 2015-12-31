package com.zibilal.data.worker;

import android.database.sqlite.SQLiteDatabase;

import com.zibilal.dao.DaoMaster;
import com.zibilal.dao.DaoSession;

/**
 * Created by Bilal on 12/31/2015.
 */
public class GreenDaoSessionManager {
    private static GreenDaoSessionManager _instance;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SQLiteDatabase mDatabase;

    private GreenDaoSessionManager(SQLiteDatabase database) {
        mDatabase = database;
        mDaoMaster = new DaoMaster(mDatabase);
        mDaoSession = mDaoMaster.newSession();
    }

    public static void initSessionManager(SQLiteDatabase database) {
        _instance = new GreenDaoSessionManager(database);
    }

    public static GreenDaoSessionManager getInstance() {
        if (_instance == null)
            throw new IllegalStateException("Please call initSessionManager first");
        return _instance;
    }
}
