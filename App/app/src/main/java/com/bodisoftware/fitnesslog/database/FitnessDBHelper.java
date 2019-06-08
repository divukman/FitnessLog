package com.bodisoftware.fitnesslog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dvukman on 11/16/2016.
 */

public class FitnessDBHelper extends SQLiteOpenHelper {

    //Thread safe singleton
    private static FitnessDBHelper sInstance = null;

    //database name and version
    private static final String DB_NAME = "fitnesslog.db";
    private static final int DB_VERSION = 1;

    //table and column constants
    public static final String COLUMN_ID = "_id";

    //table names
    public static final String TABLE_NAME_CATEGORY = "category";
    public static final String TABLE_NAME_EXERCISE = "exercise";
    public static final String TABLE_NAME_ROUTINE = "routine";
    public static final String TABLE_NAME_WORKOUT = "workout";
    public static final String TABLE_NAME_SESSION = "session";
    public static final String TABLE_NAME_EXERCISE_HISTORY = "exercisehistory";
    public static final String TABLE_NAME_WORKOUT_EXERCISE = "workoutexercise";

    //column names
    public static final String COL_NAME = "name";
    public static final String COL_DATE = "date";
    public static final String COL_NOTES = "notes";
    public static final String COL_SETS = "sets";
    public static final String COL_SET = "setNo";
    public static final String COL_REPS = "reps";
    public static final String COL_REST = "rest";
    public static final String COL_WEIGHT = "weight";

    //foreign key column names
    public static final String COL_FKEY_CATEGORY_ID = "category_id";
    public static final String COL_FKEY_ROUTINE_ID = "routine_id";
    public static final String COL_FKEY_WORKOUT_ID = "workout_id";
    public static final String COL_FKEY_SESSION_ID = "session_id";
    public static final String COL_FKEY_EXERCISE_ID = "exercise_id";

    //sql creation constants
    private static final String STRING_CREATE_CATEGORY = "CREATE TABLE " + TABLE_NAME_CATEGORY
            + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME + " TEXT not null);";

    private static final String STRING_CREATE_EXERCISE = "CREATE TABLE " + TABLE_NAME_EXERCISE
            + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_FKEY_CATEGORY_ID + " INTEGER, "
            + COL_NAME + " TEXT, "
            + " FOREIGN KEY(" + COL_FKEY_CATEGORY_ID + ") REFERENCES " + TABLE_NAME_CATEGORY + "(" + COLUMN_ID + ")"
            + ");";

    private static final String STRING_CREATE_ROUTINE = "CREATE TABLE " + TABLE_NAME_ROUTINE
            + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME + " TEXT);";

    private static final String STRING_CREATE_WORKOUT = "CREATE TABLE " + TABLE_NAME_WORKOUT
            + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_FKEY_ROUTINE_ID + " INTEGER, "
            + COL_NAME + " TEXT, "
            + " FOREIGN KEY(" + COL_FKEY_ROUTINE_ID + ") REFERENCES " + TABLE_NAME_ROUTINE + "(" + COLUMN_ID + ")"
            + ");";

    private static final String STRING_CREATE_SESSION = "CREATE TABLE " + TABLE_NAME_SESSION
            + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_FKEY_WORKOUT_ID + " INTEGER, "
            + COL_DATE + " INTEGER, "
            + COL_NOTES + " TEXT, "
            + " FOREIGN KEY(" + COL_FKEY_WORKOUT_ID + ") REFERENCES " + TABLE_NAME_WORKOUT + "(" + COLUMN_ID + ")"
            + ");";

    private static final String STRING_CREATE_EXERCISE_HISTORY = "CREATE TABLE " + TABLE_NAME_EXERCISE_HISTORY
            + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_FKEY_SESSION_ID + " INTEGER, "
            + COL_FKEY_EXERCISE_ID + " INTEGER, "
            + COL_SET + " INTEGER, "
            + COL_REPS + " INTEGER, "
            + COL_REST + " INTEGER, "
            + COL_WEIGHT + " REAL, "
            + " FOREIGN KEY(" + COL_FKEY_SESSION_ID + ") REFERENCES " + TABLE_NAME_SESSION + "(" + COLUMN_ID + ")"
            + " FOREIGN KEY(" + COL_FKEY_EXERCISE_ID + ") REFERENCES " + TABLE_NAME_EXERCISE + "(" + COLUMN_ID + ")"
            + ");";

    private static final String STRING_CREATE_WORKOUT_EXERCISE = "CREATE TABLE " + TABLE_NAME_WORKOUT_EXERCISE
            + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_FKEY_WORKOUT_ID + " INTEGER, "
            + COL_FKEY_EXERCISE_ID + " INTEGER, "
            + COL_SETS + " INTEGER, "
            + COL_REPS + " INTEGER, "
            + COL_REST + " INTEGER, "
            + " FOREIGN KEY(" + COL_FKEY_WORKOUT_ID + ") REFERENCES " + TABLE_NAME_WORKOUT + "(" + COLUMN_ID + ")"
            + " FOREIGN KEY(" + COL_FKEY_EXERCISE_ID + ") REFERENCES " + TABLE_NAME_EXERCISE + "(" + COLUMN_ID + ")"
            + ");";


    /***
     * Singleton idea from http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
     * @param context
     * @return instance of FitnessDBHelper
     */
    public static synchronized FitnessDBHelper getInstance(final Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new FitnessDBHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private FitnessDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create database tables
        sqLiteDatabase.execSQL(STRING_CREATE_CATEGORY);
        sqLiteDatabase.execSQL(STRING_CREATE_EXERCISE);
        sqLiteDatabase.execSQL(STRING_CREATE_ROUTINE);
        sqLiteDatabase.execSQL(STRING_CREATE_WORKOUT);
        sqLiteDatabase.execSQL(STRING_CREATE_SESSION);
        sqLiteDatabase.execSQL(STRING_CREATE_EXERCISE_HISTORY);
        sqLiteDatabase.execSQL(STRING_CREATE_WORKOUT_EXERCISE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Temp: @todo: check version
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STRING_CREATE_WORKOUT_EXERCISE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STRING_CREATE_EXERCISE_HISTORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EXERCISE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WORKOUT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROUTINE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SESSION);
        onCreate(sqLiteDatabase);
    }
}
