package com.bodisoftware.fitnesslog.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dto.WorkoutExercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvukman on 11/28/2016.
 */

public class WorkoutExerciseDataSource extends AbstractDataSource {

    private static final String TAG = ExerciseHistoryDataSource.class.getSimpleName();
    private String[] allColumns = {
            FitnessDBHelper.COLUMN_ID,
            FitnessDBHelper.COL_FKEY_WORKOUT_ID,
            FitnessDBHelper.COL_FKEY_EXERCISE_ID,
            FitnessDBHelper.COL_SETS,
            FitnessDBHelper.COL_REPS,
            FitnessDBHelper.COL_REST};

    public WorkoutExerciseDataSource(Context context) {
        super(context);
    }

    public WorkoutExercise createWorkoutExercise(final long workoutId, final long exerciseId, final int sets, final int reps, final int rest) {
        ContentValues values = new ContentValues();
        values.put(FitnessDBHelper.COL_FKEY_WORKOUT_ID, workoutId);
        values.put(FitnessDBHelper.COL_FKEY_EXERCISE_ID, exerciseId);
        values.put(FitnessDBHelper.COL_SETS, sets);
        values.put(FitnessDBHelper.COL_REPS, reps);
        values.put(FitnessDBHelper.COL_REST, rest);

        long insertId = database.insert(FitnessDBHelper.TABLE_NAME_WORKOUT_EXERCISE, null, values);
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT_EXERCISE, allColumns, FitnessDBHelper.COLUMN_ID + " =" + insertId, null, null, null, null);
        cursor.moveToFirst();
        WorkoutExercise workoutExercise = cursorToWorkoutExercise(cursor);
        cursor.close();
        return workoutExercise;
    }

    public void deleteWorkoutExercise(final WorkoutExercise workoutExercise) {
        final long id = workoutExercise.getId();
        Log.w(TAG, "WorkoutExercise with id: " + id + " deleted.");
        database.delete(FitnessDBHelper.TABLE_NAME_WORKOUT_EXERCISE, FitnessDBHelper.COLUMN_ID + " = " + id, null);
    }

    public List<WorkoutExercise> getAllWorkoutExercises(final long workoutId) {
        List<WorkoutExercise> workoutExercises = new ArrayList<WorkoutExercise>();

        final String where = FitnessDBHelper.COL_FKEY_WORKOUT_ID+"=? ";
        final String [] args = {String.valueOf(workoutId)};

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT_EXERCISE, allColumns, where, args, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            WorkoutExercise workoutExercise = cursorToWorkoutExercise(cursor);
            workoutExercises.add(workoutExercise);
            cursor.moveToNext();
        }

        cursor.close();
        return workoutExercises;
    }


    private WorkoutExercise cursorToWorkoutExercise(final Cursor cursor) {
        WorkoutExercise workoutExercise = new WorkoutExercise();

        workoutExercise.setId(cursor.getLong(0));
        workoutExercise.setWorkoutId(cursor.getLong(1));
        workoutExercise.setExerciseId(cursor.getLong(2));
        workoutExercise.setSets(cursor.getInt(3));
        workoutExercise.setReps(cursor.getInt(4));
        workoutExercise.setRest(cursor.getInt(5));

        return workoutExercise;
    }

}
