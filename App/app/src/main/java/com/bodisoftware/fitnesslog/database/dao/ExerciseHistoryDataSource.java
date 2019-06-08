package com.bodisoftware.fitnesslog.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dto.ExerciseHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvukman on 11/28/2016.
 */

public class ExerciseHistoryDataSource extends AbstractDataSource {

    private static final String TAG = ExerciseHistoryDataSource.class.getSimpleName();
    private String[] allColumns = {
            FitnessDBHelper.COLUMN_ID,
            FitnessDBHelper.COL_FKEY_SESSION_ID,
            FitnessDBHelper.COL_FKEY_EXERCISE_ID,
            FitnessDBHelper.COL_SET,
            FitnessDBHelper.COL_REPS,
            FitnessDBHelper.COL_REST,
            FitnessDBHelper.COL_WEIGHT};

    public ExerciseHistoryDataSource(Context context) {
        super(context);
    }

    public ExerciseHistory createExerciseHistory(final long sessionId,
                                                 final long exerciseId,
                                                 final int set,
                                                 final int reps,
                                                 final int rest,
                                                 final float weight) {
        ContentValues values = new ContentValues();
        values.put(FitnessDBHelper.COL_FKEY_SESSION_ID, sessionId);
        values.put(FitnessDBHelper.COL_FKEY_EXERCISE_ID, exerciseId);
        values.put(FitnessDBHelper.COL_SET, set);
        values.put(FitnessDBHelper.COL_REPS, reps);
        values.put(FitnessDBHelper.COL_REST, rest);
        values.put(FitnessDBHelper.COL_WEIGHT, weight);

        long insertId = database.insert(FitnessDBHelper.TABLE_NAME_EXERCISE_HISTORY, null, values);
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_EXERCISE_HISTORY, allColumns, FitnessDBHelper.COLUMN_ID + " =" + insertId, null, null, null, null);
        cursor.moveToFirst();
        ExerciseHistory exerciseHistory = cursorToExerciseHistory(cursor);
        cursor.close();
        return exerciseHistory;
    }

    public void deleteExerciseHistory(final ExerciseHistory exerciseHistory) {
        final long id = exerciseHistory.getId();
        Log.w(TAG, "ExerciseHistory with id: " + id + " deleted.");
        database.delete(FitnessDBHelper.TABLE_NAME_EXERCISE_HISTORY, FitnessDBHelper.COLUMN_ID + " = " + id, null);
    }

    public List<ExerciseHistory> getAllExercisesHistory() {
        List<ExerciseHistory> exercisesHistory = new ArrayList<ExerciseHistory>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_EXERCISE_HISTORY, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ExerciseHistory exerciseHistory = cursorToExerciseHistory(cursor);
            exercisesHistory.add(exerciseHistory);
            cursor.moveToNext();
        }

        cursor.close();
        return exercisesHistory;
    }

    /**
     * Returns the exercise history Cursor for specified sessionId
     * @param sessionId
     * @return cursor containing list of
     */
    public List<ExerciseHistory> getExerciseHistory(final long sessionId) {
        List<ExerciseHistory> exercisesHistory = new ArrayList<ExerciseHistory>();
        final String where = FitnessDBHelper.COL_FKEY_SESSION_ID+"=? ";
        final String [] args = {String.valueOf(sessionId)};

        final Cursor cursor =  database.query(FitnessDBHelper.TABLE_NAME_EXERCISE_HISTORY, allColumns, where, args, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ExerciseHistory exerciseHistory = cursorToExerciseHistory(cursor);
            exercisesHistory.add(exerciseHistory);
            cursor.moveToNext();
        }

        cursor.close();
        return exercisesHistory;
    }


    private ExerciseHistory cursorToExerciseHistory(final Cursor cursor) {
        ExerciseHistory exerciseHistory = new ExerciseHistory();

        exerciseHistory.setId(cursor.getLong(0));
        exerciseHistory.setSessionId(cursor.getLong(1));
        exerciseHistory.setExerciseId(cursor.getLong(2));
        exerciseHistory.setSet(cursor.getInt(3));
        exerciseHistory.setReps(cursor.getInt(4));
        exerciseHistory.setRest(cursor.getInt(5));
        exerciseHistory.setWeight(cursor.getFloat(6));

        return exerciseHistory;
    }
}
