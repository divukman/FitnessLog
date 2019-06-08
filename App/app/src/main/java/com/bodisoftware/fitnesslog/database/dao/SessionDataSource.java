package com.bodisoftware.fitnesslog.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dto.Exercise;
import com.bodisoftware.fitnesslog.database.dto.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvukman on 11/28/2016.
 *
 * Session is one time visit to the gym.
 */

public class SessionDataSource extends AbstractDataSource {

    private static final String TAG = SessionDataSource.class.getSimpleName();
    private String[] allColumns = {FitnessDBHelper.COLUMN_ID,
            FitnessDBHelper.COL_DATE,
            FitnessDBHelper.COL_NOTES,
            FitnessDBHelper.COL_FKEY_WORKOUT_ID};

    public SessionDataSource(Context context) {
        super(context);
    }

    public Session createSession(final long workoutId, final String notes, final long date) {
        ContentValues values = new ContentValues();
        values.put(FitnessDBHelper.COL_FKEY_WORKOUT_ID, workoutId);
        values.put(FitnessDBHelper.COL_NOTES, notes);
        values.put(FitnessDBHelper.COL_DATE, date);
        long insertId = database.insert(FitnessDBHelper.TABLE_NAME_SESSION, null, values);
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_SESSION, allColumns, FitnessDBHelper.COLUMN_ID + " =" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Session newSession = cursorToSession(cursor);
        cursor.close();
        return newSession;
    }

    public Session getSession(final long sessionId) {
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_SESSION, allColumns, FitnessDBHelper.COLUMN_ID + " =" + sessionId, null, null, null, null);
        Session session = cursorToSession(cursor);
        cursor.close();
        return session;
    }

    public void deleteSession(final Session session) {
        final long id = session.getId();
        Log.w(TAG, "Session with id: " + id + " deleted.");
        database.delete(FitnessDBHelper.TABLE_NAME_SESSION, FitnessDBHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<Session>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_SESSION, allColumns, null, null, null, null, FitnessDBHelper.COL_DATE+" DESC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Session session = cursorToSession(cursor);
            sessions.add(session);
            cursor.moveToNext();
        }

        cursor.close();
        return sessions;
    }

    public List<Session> getAllSessions(final long routineId) {
        List<Session> sessions = new ArrayList<Session>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_SESSION, allColumns, FitnessDBHelper.COL_FKEY_ROUTINE_ID + " =" + routineId, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Session session = cursorToSession(cursor);
            sessions.add(session);
            cursor.moveToNext();
        }

        cursor.close();
        return sessions;
    }

    public Cursor getAllSessionsCursor(final long routineId) {
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_SESSION, allColumns, FitnessDBHelper.COL_FKEY_ROUTINE_ID + " =" + routineId, null, null, null, null);
        cursor.moveToFirst();

        return cursor;
    }

    public Cursor getAllSessionsCursor() {
        Cursor cursor = database.query(
                FitnessDBHelper.TABLE_NAME_SESSION,
                allColumns,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        return cursor;
    }

    /**
     * Returns the last recorded session. (newest one)
     * @return session
     */
    public Session getLastSession() {
        Session result = null;

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_SESSION, allColumns, null, null, null, null, FitnessDBHelper.COLUMN_ID + " DESC", "1");
        if (cursor.moveToFirst()) {
            result = cursorToSession(cursor);
        }

        return result;
    }

    /**
     * Returns the last recorded session for specified workout.
     * @param workoutID - the database table id of the workout
     * @return session
     */
    public Session getLastSessionForWorkout(final long workoutID) {
        Session result = null;
        final String where = FitnessDBHelper.COL_FKEY_WORKOUT_ID+"=? ";
        final String [] args = {String.valueOf(workoutID)};

        Cursor cursor = database.query(
                FitnessDBHelper.TABLE_NAME_SESSION,
                allColumns,
                where,
                args,
                null,
                null,
                FitnessDBHelper.COLUMN_ID + " DESC",
                "1");
        if (cursor.moveToFirst()) {
            result = cursorToSession(cursor);
        }

        return result;
    }

    public static Session cursorToSession(final Cursor cursor) {
        Session session = new Session();

        session.setId(cursor.getLong(0));
        session.setDate(cursor.getLong(1));
        session.setNotes(cursor.getString(2));
        session.setWorkout_id(cursor.getLong(3));

        return session;
    }

}
