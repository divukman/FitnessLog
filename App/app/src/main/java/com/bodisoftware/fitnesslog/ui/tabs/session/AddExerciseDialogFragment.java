package com.bodisoftware.fitnesslog.ui.tabs.session;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dao.CategoryDataSource;
import com.bodisoftware.fitnesslog.database.dao.ExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutDataSource;
import com.bodisoftware.fitnesslog.ui.tabs.routine.NewWorkoutDialogFragment;
import com.bodisoftware.fitnesslog.ui.tabs.routine.WorkoutData;

/**
 * Created by dvukman on 10/11/2017.
 *
 * Used in the new session activity to add a new exercise to the existing session.
 */

public class AddExerciseDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(WorkoutData.ExerciseData exerciseData);
    }

    View mRootView = null;
    // Use this instance of the interface to deliver action events
    AddExerciseDialogFragment.NoticeDialogListener mListener = null;
    private Spinner mSpnCategory = null;
    private Spinner mSpnExercise = null;
    private com.shawnlin.numberpicker.NumberPicker mNumberPickerSets = null;

    private Cursor mCategoriesCursor = null;
    private CategoryDataSource mCategoryDAO = null;

    private Cursor mExercisesCursor = null;
    private ExerciseDataSource mExercisesDAO = null;


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Activity activity;

        if (context instanceof Activity){
            activity =(Activity) context;
        } else {
            activity = getActivity();
        }

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddExerciseDialogFragment.NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialogTesting);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mRootView = inflater.inflate(R.layout.dialog_session_add_exercise, null);

        mSpnCategory = (Spinner) mRootView.findViewById(R.id.spnSessionCategory);
        mSpnExercise = (Spinner) mRootView.findViewById(R.id.spnSessionExercise);
        mNumberPickerSets = (com.shawnlin.numberpicker.NumberPicker) mRootView.findViewById(R.id.numberPickerSessionSets);

        mCategoryDAO = new CategoryDataSource(getActivity().getApplicationContext());
        mCategoryDAO.open();
        mExercisesDAO = new ExerciseDataSource(getActivity().getApplicationContext());
        mExercisesDAO.open();

        createSpinnerAdaptersAndListeners();

        builder.setView(mRootView)
                // Add action buttons
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                final Button btnOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Cursor exerciseCursor = (Cursor) mSpnExercise.getSelectedItem();

                        if (exerciseCursor != null) {
                            WorkoutData.ExerciseData exerciseData = new WorkoutData.ExerciseData();
                            final String strExerciseName = (String) exerciseCursor.getString(exerciseCursor.getColumnIndex(FitnessDBHelper.COL_NAME));
                            final int noSets = mNumberPickerSets.getValue();

                            exerciseData.name = strExerciseName;
                            exerciseData.sets = noSets;
                            exerciseData.exerciseId = (long) exerciseCursor.getLong(exerciseCursor.getColumnIndex(FitnessDBHelper.COLUMN_ID));

                            mListener.onDialogPositiveClick(exerciseData);
                        } else {
                            // Something very wrong happened!
                        }


                        dialog.dismiss();
                    }
                });

                final Button btnCancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });


        return dialog;
    }

    private void createSpinnerAdaptersAndListeners() {

        final String [] from = new String[] {FitnessDBHelper.COL_NAME};
        final int[] to = new int[] {android.R.id.text1};

        mCategoriesCursor = mCategoryDAO.getAllCategoriesCursor();
        if (mCategoriesCursor.getCount() > 0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, mCategoriesCursor, from, to, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnCategory.setAdapter(adapter);
        }

        mSpnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mExercisesCursor = mExercisesDAO.getAllExercisesCursor(id);
                SimpleCursorAdapter exercisesAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, mExercisesCursor, from, to, 0);
                exercisesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpnExercise.setAdapter(exercisesAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
