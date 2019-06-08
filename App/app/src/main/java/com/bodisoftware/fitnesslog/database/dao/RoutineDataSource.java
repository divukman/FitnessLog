package com.bodisoftware.fitnesslog.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dto.Category;
import com.bodisoftware.fitnesslog.database.dto.Routine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvukman on 11/25/2016.
 */

public class RoutineDataSource extends AbstractDataSource {

    private static final String TAG = RoutineDataSource.class.getSimpleName();
    private String[] allColumns = {FitnessDBHelper.COLUMN_ID, FitnessDBHelper.COL_NAME};

    public RoutineDataSource(Context context) {
        super(context);
    }

    public Routine createRoutine(final String name) {
        ContentValues values = new ContentValues();
        values.put(FitnessDBHelper.COL_NAME, name);
        long insertId = database.insert(FitnessDBHelper.TABLE_NAME_ROUTINE, null, values);
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_ROUTINE, allColumns, FitnessDBHelper.COLUMN_ID + " =" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Routine newRoutine = cursorToRoutine(cursor);
        cursor.close();
        return newRoutine;
    }

    public void deleteRoutine(final Routine routine) {
        final long id = routine.getId();
        Log.w(TAG, "Routine with id: " + id + " deleted.");
        database.delete(FitnessDBHelper.TABLE_NAME_ROUTINE, FitnessDBHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Routine> getAllRoutines() {
        List<Routine> routines = new ArrayList<Routine>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_ROUTINE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Routine routine = cursorToRoutine(cursor);
            routines.add(routine);
            cursor.moveToNext();
        }

        cursor.close();
        return routines;
    }

    public Cursor getAllRoutinesCursor() {
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_ROUTINE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    private Routine cursorToRoutine(final Cursor cursor) {
        Routine routine = new Routine();

        routine.setId(cursor.getLong(0));
        routine.setName(cursor.getString(1));

        return routine;
    }

    public long getRoutineId(final String routineName) {
        List<Routine> routines = new ArrayList<Routine>();
        long result = -1;

        final String where = FitnessDBHelper.COL_NAME+"=?";
        final String [] args = {routineName};

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_ROUTINE, allColumns, where, args, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Routine routine = cursorToRoutine(cursor);
            cursor.close();
            result = routine.getId();
        }

        return result;
    }

    public static long getRoutineId(final Context context, final String routineName) {
        RoutineDataSource routineDAO = new RoutineDataSource(context);
        routineDAO.open();
        final long result = routineDAO.getRoutineId(routineName);
        routineDAO.close();
        return result;
    }

    public Routine getRoutine(final long routineId) {
        Routine result = null;

        final String where = FitnessDBHelper.COLUMN_ID+"=?";
        final String [] args = {String.valueOf(routineId)};

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_ROUTINE, allColumns, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            result = cursorToRoutine(cursor);
            cursor.close();
        }

        return result;
    }

    public static long addRoutine(final Context context, final String routineName) {
        RoutineDataSource routineDAO = new RoutineDataSource(context);
        routineDAO.open();
        final Routine newRoutine = routineDAO.createRoutine(routineName);
        final long result = newRoutine.getId();
        routineDAO.close();
        return result;
    }
}
