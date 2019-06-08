package com.bodisoftware.fitnesslog.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.Cursor;
import android.util.Log;

import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dto.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvukman on 11/16/2016.
 * Category DAO.
 */

public class CategoryDataSource extends AbstractDataSource {

    private static final String TAG = CategoryDataSource.class.getSimpleName();
    private String[] allColumns = {FitnessDBHelper.COLUMN_ID, FitnessDBHelper.COL_NAME};


    public CategoryDataSource(final Context context) {
        super(context);
    }

    public Category createCategory(final String name) {
        ContentValues values = new ContentValues();
        values.put(FitnessDBHelper.COL_NAME, name);
        long insertId = database.insert(FitnessDBHelper.TABLE_NAME_CATEGORY, null, values);
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_CATEGORY, allColumns, FitnessDBHelper.COLUMN_ID + " =" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Category newCategory = cursorToCategory(cursor);
        cursor.close();
        return newCategory;
    }

    public void deleteCategory(final Category category) {
        long id = category.getId();
        Log.w(TAG, "Category with id: " + id + " deleted.");
        database.delete(FitnessDBHelper.TABLE_NAME_CATEGORY, FitnessDBHelper.COLUMN_ID + " = " + id, null);
    }

    public Category getCategory(final String name) {
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_CATEGORY, allColumns, FitnessDBHelper.COL_NAME + " ='" + name + "'", null, null, null, null);
        cursor.moveToFirst();
        Category newCategory = cursorToCategory(cursor);
        cursor.close();
        return newCategory;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_CATEGORY, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Category category = cursorToCategory(cursor);
            categories.add(category);
            cursor.moveToNext();
        }

        cursor.close();
        return categories;
    }

    public List<String> getAllCategoryNames() {
        List<String> categories = new ArrayList<String>();

        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_CATEGORY, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(FitnessDBHelper.COL_NAME));
            categories.add(name);
            cursor.moveToNext();
        }

        cursor.close();
        return categories;
    }

    public Cursor getAllCategoriesCursor() {
        Cursor cursor = database.query(FitnessDBHelper.TABLE_NAME_CATEGORY, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    private Category cursorToCategory(final Cursor cursor) {
        Category category = new Category();

        category.setId(cursor.getLong(0));
        category.setName(cursor.getString(1));

        return category;
    }
}
