package com.bodisoftware.fitnesslog.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;

/**
 * Created by dvukman on 11/16/2016.
 */

public abstract class AbstractDataSource {

    protected SQLiteDatabase database = null;
    protected FitnessDBHelper dbHelper = null;

    public AbstractDataSource(final Context context) {
        dbHelper = FitnessDBHelper.getInstance(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase(); // @todo: open writable, open readable
    }

    public void close() {
        dbHelper.close();
    }
}
