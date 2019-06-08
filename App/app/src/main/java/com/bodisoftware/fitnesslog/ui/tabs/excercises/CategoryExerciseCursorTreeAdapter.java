package com.bodisoftware.fitnesslog.ui.tabs.excercises;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.SimpleCursorTreeAdapter;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;

import java.util.HashMap;

/**
 * Created by dvukman on 11/30/2016.
 */

public class CategoryExerciseCursorTreeAdapter extends SimpleCursorTreeAdapter {

    private static final String TAG = CategoryExerciseCursorTreeAdapter.class.getSimpleName();
    private Context mContext = null;
    protected HashMap<Integer, Integer> mGroupMap= null;
    private LoaderManager mLoaderManager = null;
    private Fragment mFragment = null;

    public CategoryExerciseCursorTreeAdapter(Context context,
                                             int collapsedGroupLayout,
                                             int expandedGroupLayout,
                                             String[] groupFrom,
                                             int[] groupTo,
                                             int childLayout,
                                             int lastChildLayout,
                                             String[] childFrom,
                                             int[] childTo,
                                             Fragment fragment) {
        super(context, null, collapsedGroupLayout, expandedGroupLayout, groupFrom, groupTo, childLayout, lastChildLayout, childFrom, childTo);
        mContext = context;
        mGroupMap = new HashMap<Integer, Integer>();
        mFragment = fragment;
        mLoaderManager = fragment.getLoaderManager();
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        // Logic to get the child cursor on the basis of selected group.
        int groupPos = groupCursor.getPosition();
        int groupId = groupCursor.getInt(groupCursor.getColumnIndex(FitnessDBHelper.COLUMN_ID));

        Log.d(TAG, "getChildrenCursor() for groupPos " + groupPos);
        Log.d(TAG, "getChildrenCursor() for groupId " + groupId);

        mGroupMap.put(groupId, groupPos);
        Loader<Cursor> loader = mLoaderManager.getLoader(groupId);
        if (loader != null && !loader.isReset()) {
            mLoaderManager.restartLoader(groupId, null, (LoaderManager.LoaderCallbacks<? extends Object>) mFragment);
        } else {
            mLoaderManager.initLoader(groupId, null, (LoaderManager.LoaderCallbacks<? extends Object>) mFragment);
        }

        return null;
    }

    public HashMap<Integer, Integer> getGroupMap() {
        return mGroupMap;
    }
}
