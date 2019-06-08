package com.bodisoftware.fitnesslog.ui.tabs;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dao.CategoryDataSource;
import com.bodisoftware.fitnesslog.database.dao.ExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dto.Category;
import com.bodisoftware.fitnesslog.database.dto.Exercise;
import com.bodisoftware.fitnesslog.ui.tabs.excercises.CategoryCursorLoader;
import com.bodisoftware.fitnesslog.ui.tabs.excercises.CategoryExerciseCursorTreeAdapter;
import com.bodisoftware.fitnesslog.ui.tabs.excercises.ExercisesCursorLoader;
import com.bodisoftware.fitnesslog.ui.tabs.excercises.Group;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TabFragmentExercises extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = TabFragmentExercises.class.getSimpleName();

    // more efficient than HashMap for mapping integers to objects
    ArrayList<Group> groups = new ArrayList<Group>();
    private AddFloatingActionButton mBtnAddCategory = null;
    private AddFloatingActionButton mBtnAddExercise = null;
    private ExpandableListView mLstView = null;

    private CategoryExerciseCursorTreeAdapter mTreeAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_excercises, container, false);

        //createData();
        mLstView = (ExpandableListView) view.findViewById(R.id.listView);
        mTreeAdapter = new CategoryExerciseCursorTreeAdapter(
                getContext(),
                R.layout.listrow_group_excercises,
                R.layout.listrow_group_excercises_collapsed,
                new String [] { FitnessDBHelper.COL_NAME },
                new int[] {R.id.txtCategory},
                R.layout.listrow_details_excercises,
                R.layout.listrow_details_excercises,
                new String[] { FitnessDBHelper.COL_NAME },
                new int[] {R.id.txtExercise},
                this
        );
        mLstView.setAdapter(mTreeAdapter);


        mBtnAddCategory = (AddFloatingActionButton) view.findViewById(R.id.btnAddCategory);
        mBtnAddExercise = (AddFloatingActionButton) view.findViewById(R.id.btnAddExercise);

        addListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Loader<Cursor> loader = getLoaderManager().getLoader(-1);
        if (loader != null && !loader.isReset()) {
            getLoaderManager().restartLoader(-1, null, this);
        } else {
            getLoaderManager().initLoader(-1, null, this);
        }
    }

    private void addListeners() {
        mBtnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Category name")
                        .content("Please enter category name (example: Chest, Biceps, Cardio...)")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Name", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                final Category category = addCategory(input.toString());
                                Toast.makeText(TabFragmentExercises.this.getContext(), "Category: " + category.getName() + " added.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        mBtnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wrapInScrollView = true;
                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title("Exercise name")
                        .customView(R.layout.dialog_excercise, wrapInScrollView)
                        .positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                EditText editTxtName = (EditText) dialog.findViewById(R.id.exerciseName);
                                Spinner spinnerCategory = (Spinner) dialog.findViewById(R.id.spinnerCategory);
                                if (spinnerCategory != null && editTxtName != null && editTxtName.getText().toString().trim().length() > 0) {
                                    final String exerciseName = editTxtName.getText().toString().trim();
                                    final String category = spinnerCategory.getSelectedItem().toString();
                                    final Exercise exercise = addExercise(category, exerciseName);
                                    Toast.makeText(TabFragmentExercises.this.getContext(), "Category: " + category + ", exercise: " + exerciseName, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).build();

                Spinner spinner = (Spinner) dialog.findViewById(R.id.spinnerCategory);
                // Creating adapter for spinner
                CategoryDataSource categoryDAO = new CategoryDataSource(getContext());
                categoryDAO.open();
                List<String> lst = categoryDAO.getAllCategoryNames();
                categoryDAO.close();

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lst);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                dialog.show();
            }
        });
    }

    private Category addCategory(final String name) {
        Category result = null;

        CategoryDataSource categoryDAO = new CategoryDataSource(TabFragmentExercises.this.getContext());
        categoryDAO.open();
        result =  categoryDAO.createCategory(name);

        final Cursor categoryCursor  = categoryDAO.getAllCategoriesCursor();
        categoryCursor.moveToFirst();
        mTreeAdapter.changeCursor(categoryCursor);
        categoryDAO.close();

        return result;
    }

    private Exercise addExercise(final String categoryName, final String exerciseName) {
        CategoryDataSource categoryDAO = new CategoryDataSource(getContext());
        categoryDAO.open();
        Category category =  categoryDAO.getCategory(categoryName);
        categoryDAO.close();

        ExerciseDataSource execiseDAO = new ExerciseDataSource(getContext());
        execiseDAO.open();
        Exercise exercise = execiseDAO.createExercise(category.getId(), exerciseName);
        execiseDAO.close();

        mTreeAdapter.notifyDataSetChanged();

        return exercise;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader for loader_id " + id);

        CursorLoader result;
        if (id != -1) {
            // child cursor
            result = new ExercisesCursorLoader(getContext(), (long)id);
        } else {
            // group cursor
            result = new CategoryCursorLoader(getContext());
        }

        return result;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Setting the new cursor onLoadFinished. (Old cursor would be closed
        // automatically)
        final int id = loader.getId();
        Log.d(TAG, "onLoadFinished() for loader_id " + id);
        if (id != -1) {
            // child cursor
            if (!data.isClosed()) {
                Log.d(TAG, "data.getCount() " + data.getCount());

                HashMap<Integer, Integer> groupMap = mTreeAdapter.getGroupMap();
                try {
                    int groupPos = groupMap.get(id);
                    Log.d(TAG, "onLoadFinished() for groupPos " + groupPos);
                    mTreeAdapter.setChildrenCursor(groupPos, data);
                } catch (NullPointerException e) {
                    Log.w(TAG,
                            "Adapter expired, try again on the next query: "
                                    + e.getMessage());
                }
            }
        } else {
            mTreeAdapter.setGroupCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Called just before the cursor is about to be closed.
        int id = loader.getId();
        Log.d(TAG, "onLoaderReset() for loader_id " + id);
        if (id != -1) {
            // child cursor
            HashMap<Integer, Integer> groupMap = mTreeAdapter.getGroupMap();
            try {
                int groupPos = groupMap.get(id);
                mTreeAdapter.setChildrenCursor(groupPos, null);
            } catch (NullPointerException e) {
                Log.w(TAG, "Adapter expired, try again on the next query: "
                        + e.getMessage());
            }
        } else {
            mTreeAdapter.setGroupCursor(null);
        }
    }
}