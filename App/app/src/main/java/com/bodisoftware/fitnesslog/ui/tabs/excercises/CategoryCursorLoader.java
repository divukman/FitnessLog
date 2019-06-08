package com.bodisoftware.fitnesslog.ui.tabs.excercises;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.bodisoftware.fitnesslog.database.dao.CategoryDataSource;

/**
 * Created by dvukman on 12/5/2016.
 */

public class CategoryCursorLoader extends CursorLoader {

    private static final String TAG = CategoryCursorLoader.class.getSimpleName();
    private Context mContext = null;

    public CategoryCursorLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor result = null;

        CategoryDataSource categoryDAO = new CategoryDataSource(mContext);
        categoryDAO.open();
        result = categoryDAO.getAllCategoriesCursor();
        result.moveToFirst();
        categoryDAO.close();

        return result;
    }
}
