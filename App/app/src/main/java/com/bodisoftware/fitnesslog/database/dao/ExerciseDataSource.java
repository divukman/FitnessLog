package com.bodisoftware.fitnesslog.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dto.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvukman on 11/16/2016.
 * Exercise DAO.
 */

public class ExerciseDataSource extends AbstractDataSource {

    private static final String TAG = ExerciseDataSource.class.getSimpleName();
    private String[] allColumns = {FitnessDBHelper.COLUMN_ID, FitnessDBHelper.COL_FKEY_CATEGORY_ID, FitnessDBHelper.COL_NAME};


    public ExerciseDataSource(final Context context) {
        super(context);
    }

    public Exercise createExercise(final long categoryId, final String name) {
        ContentValues values = new ContentValues();
        values.put(FitnessDBHelper.COL_FKEY_CATEGORY_ID, categoryId);
        values.put(FitnessDBHelper.COL_NAME, name);
        long insertId = database.insert(FitnessDBHelper.TABLE_NAME_EXERCISE, null, values);
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_EXERCISE, allColumns, FitnessDBHelper.COLUMN_ID + " =" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Exercise newExercise = cursorToExercise(cursor);
        cursor.close();
        return newExercise;
    }

    public void deleteExercise(final Exercise exercise) {
        long id = exercise.getId();
        Log.w(TAG, "Exercise with id: " + id + " deleted.");
        database.delete(FitnessDBHelper.TABLE_NAME_EXERCISE, FitnessDBHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<Exercise>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_EXERCISE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            exercises.add(exercise);
            cursor.moveToNext();
        }

        cursor.close();
        return exercises;
    }

    public List<Exercise> getAllExercises(final long categoryId) {
        List<Exercise> exercises = new ArrayList<Exercise>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_EXERCISE, allColumns, FitnessDBHelper.COL_FKEY_CATEGORY_ID + " =" + categoryId, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            exercises.add(exercise);
            cursor.moveToNext();
        }

        cursor.close();
        return exercises;
    }

    public Cursor getAllExercisesCursor(final long categoryId) {
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_EXERCISE, allColumns, FitnessDBHelper.COL_FKEY_CATEGORY_ID + " =" + categoryId, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    private Exercise cursorToExercise(final Cursor cursor) {
        Exercise exercise = new Exercise();

        exercise.setId(cursor.getLong(0));
        exercise.setCategoryId(cursor.getLong(1));
        exercise.setName(cursor.getString(2));

        return exercise;
    }

    public long getExerciseId(final String name) {
        long result = -1;

        final String where = FitnessDBHelper.COL_NAME+"=? ";
        final String [] args = {name};

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_EXERCISE, allColumns, where, args, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Exercise exercise = cursorToExercise(cursor);
            cursor.close();
            result = exercise.getId();
        }

        return result;
    }

    public Exercise getExercise(final long id) {
        Exercise result = null;

        final String where = FitnessDBHelper.COLUMN_ID+"=? ";
        final String [] args = {String.valueOf(id)};

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_EXERCISE, allColumns, where, args, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursorToExercise(cursor);
            cursor.close();
        }

        return result;
    }
}
