package com.bodisoftware.fitnesslog.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dto.Exercise;
import com.bodisoftware.fitnesslog.database.dto.Workout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dvukman on 11/28/2016.
 *
 * Routine has one or more workouts. Workout is a list of exercises, sets and reps.
 */

public class WorkoutDataSource extends AbstractDataSource {

    private static final String TAG = ExerciseDataSource.class.getSimpleName();
    private String[] allColumns = {FitnessDBHelper.COLUMN_ID, FitnessDBHelper.COL_FKEY_ROUTINE_ID, FitnessDBHelper.COL_NAME};

    public WorkoutDataSource(Context context) {
        super(context);
    }

    public Workout createWorkout(final long routineId, final String name) {
        ContentValues values = new ContentValues();
        values.put(FitnessDBHelper.COL_FKEY_ROUTINE_ID, routineId);
        values.put(FitnessDBHelper.COL_NAME, name);
        long insertId = database.insert(FitnessDBHelper.TABLE_NAME_WORKOUT, null, values);
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT, allColumns, FitnessDBHelper.COLUMN_ID + " =" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Workout newWorkout = cursorToWorkout(cursor);
        cursor.close();
        return newWorkout;
    }

    public void deleteWorkout(final Workout workout) {
        long id = workout.getId();
        Log.w(TAG, "Workout with id: " + id + " deleted.");
        database.delete(FitnessDBHelper.TABLE_NAME_WORKOUT, FitnessDBHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Workout workout = cursorToWorkout(cursor);
            workouts.add(workout);
            cursor.moveToNext();
        }

        cursor.close();
        return workouts;
    }

    public List<Workout> getAllWorkouts(final long routineId) {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT, allColumns, FitnessDBHelper.COL_FKEY_ROUTINE_ID + " =" + routineId, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Workout workout = cursorToWorkout(cursor);
            workouts.add(workout);
            cursor.moveToNext();
        }

        cursor.close();
        return workouts;
    }

    public Cursor getAllWorkoutsCursor(final long routineId) {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT, allColumns, FitnessDBHelper.COL_FKEY_ROUTINE_ID + " =" + routineId, null, null, null, null);
        cursor.moveToFirst();

        return cursor;
    }

    public static Workout cursorToWorkout(final Cursor cursor) {
        Workout workout = new Workout();

        workout.setId(cursor.getLong(0));
        workout.setRoutineId(cursor.getLong(1));
        workout.setName(cursor.getString(2));

        return workout;
    }

    public long getWorkoutId(final long routineId, final String workoutName) {
        long result = -1;

        final String where = FitnessDBHelper.COL_FKEY_ROUTINE_ID+"=? AND " + FitnessDBHelper.COL_NAME+"=?";
        final String [] args = {String.valueOf(routineId), workoutName};

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT, allColumns, where, args, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Workout workout = cursorToWorkout(cursor);
            cursor.close();
            result = workout.getId();
        }

        return result;
    }

    public Workout getWorkout(final long workoutId) {
        Workout result = null;

        final String where = FitnessDBHelper.COLUMN_ID+"=?";
        final String [] args = {String.valueOf(workoutId)};

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT, allColumns, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            result = cursorToWorkout(cursor);
            cursor.close();
        }

        return result;
    }

    public Workout getNextWorkout(final long routineId, final long currentWorkoutId) {
        Workout result = null;

        List<Workout> lstAllWorkouts = getAllWorkouts(routineId);
        Iterator<Workout> iterator =  lstAllWorkouts.iterator();
        while (iterator.hasNext()) {
            Workout workout = iterator.next();
            if (workout.getId() == currentWorkoutId) {
                if (iterator.hasNext()) {
                    result = iterator.next();
                    break;
                } else {
                    result = lstAllWorkouts.get(0);
                    break;
                }
            }
        }

        return result;
    }

    public Cursor getWorkoutsCursor(final long routineId) {
        final String where = FitnessDBHelper.COL_FKEY_ROUTINE_ID+"=?";
        final String [] args = {String.valueOf(routineId)};

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_WORKOUT, allColumns, where, args, null, null, null);
        return cursor;
    }

}
