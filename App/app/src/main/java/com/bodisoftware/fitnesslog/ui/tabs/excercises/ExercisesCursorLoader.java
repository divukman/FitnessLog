package com.bodisoftware.fitnesslog.ui.tabs.excercises;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.bodisoftware.fitnesslog.database.dao.ExerciseDataSource;

/**
 * Created by dvukman on 12/5/2016.
 */

public class ExercisesCursorLoader extends CursorLoader {

    private static final String TAG = ExercisesCursorLoader.class.getSimpleName();
    private Context mContext = null;
    private long mId = -1;

    public ExercisesCursorLoader(final Context context, final long id) {
        super(context);
        mContext = context;
        mId = id;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor result = null;

        ExerciseDataSource exercisesDAO = new ExerciseDataSource(mContext);
        exercisesDAO.open();
        result = exercisesDAO.getAllExercisesCursor(mId);
        result.moveToFirst();

        return result;
    }
}
