package com.bodisoftware.fitnesslog.ui.tabs.routine;

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

/**
 * Created by dvukman on 7/21/2017.
 */

public class NewWorkoutDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(WorkoutData workoutData);
        public void onDialogNegativeClick(WorkoutData workoutData);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener = null;

    View mRootView = null;
    private Spinner mSpnGroup = null;
    private Spinner mSpnItem = null;
    private LinearLayout mLayoutExercises = null;
    private FloatingActionButton mBtnAdd = null;

    private Cursor mCategoriesCursor = null;
    private CategoryDataSource mCategoryDAO = null;

    private Cursor mExercisesCursor = null;
    private ExerciseDataSource mExercisesDAO = null;

    private com.shawnlin.numberpicker.NumberPicker mNumberPickerSets = null;
    private com.shawnlin.numberpicker.NumberPicker mNumberPickerReps = null;
    private EditText mEditTxtWorkoutName = null;

    private WorkoutData mWorkoutData = new WorkoutData();

    private long mRoutineId = 0;
    private WorkoutDataSource mWorkoutDAO = null;


    /**
     * Sets the routine id.
     * @param routineId
     */
    public void setRoutineId(final long routineId) {
        mRoutineId = routineId;
    }

    /**
     * Sets the workout DAO from the parent activity.
     * @param workoutDAO
     */
    public void setWorkoutDAO(final WorkoutDataSource workoutDAO) {
        mWorkoutDAO = workoutDAO;
    }

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
            mListener = (NoticeDialogListener) activity;
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
        mRootView = inflater.inflate(R.layout.dialog_new_workout, null);

        mSpnGroup = (Spinner) mRootView.findViewById(R.id.spinnerGroup);
        mSpnItem = (Spinner) mRootView.findViewById(R.id.spinnerItem);
        mLayoutExercises = (LinearLayout) mRootView.findViewById(R.id.layoutExercises);
        mBtnAdd = (FloatingActionButton) mRootView.findViewById(R.id.floatingActionButtonAdd);
        mNumberPickerSets = (com.shawnlin.numberpicker.NumberPicker) mRootView.findViewById(R.id.numberPickerSets);
        mNumberPickerReps = (com.shawnlin.numberpicker.NumberPicker) mRootView.findViewById(R.id.numberPickerReps);
        mEditTxtWorkoutName = (EditText) mRootView.findViewById(R.id.editTextWorkoutName);


        mCategoryDAO = new CategoryDataSource(getActivity().getApplicationContext());
        mCategoryDAO.open();
        mExercisesDAO = new ExerciseDataSource(getActivity().getApplicationContext());
        mExercisesDAO.open();

        createSpinnerAdaptersAndListeners();
        createListeners();

        builder.setView(mRootView)
                // Add action buttons
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(mWorkoutData);
                    }
                });


        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                final Button btnOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Get the workout name and the list of exercises, pass it to the parent (listener)
                        final String workoutName = mEditTxtWorkoutName.getText().toString().trim();
                        final boolean workoutExists = mWorkoutDAO.getWorkoutId(mRoutineId, workoutName) != -1;

                        if (workoutExists || !(workoutName.length() > 0 && mWorkoutData.lstExercises.size() > 0)) {
                            final String msg = workoutExists ? getResources().getString(R.string.workout_exists) :
                                    getResources().getString(R.string.enter_exercise_data);
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        } else {
                            mWorkoutData.workoutName = workoutName;
                            mListener.onDialogPositiveClick(mWorkoutData);
                            dialog.dismiss();
                        }
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
            mSpnGroup.setAdapter(adapter);
        }

        mSpnGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mExercisesCursor = mExercisesDAO.getAllExercisesCursor(id);
                SimpleCursorAdapter exercisesAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, mExercisesCursor, from, to, 0);
                exercisesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpnItem.setAdapter(exercisesAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void createListeners() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ConstraintLayout item = (ConstraintLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_new_workout_row, null);
                TextView exercise = (TextView) item.findViewById(R.id.txtExercise);
                TextView sets = (TextView) item.findViewById(R.id.txtSets);
                TextView reps = (TextView) item.findViewById(R.id.txtReps);
                FloatingActionButton fabRemoveRow = (FloatingActionButton) item.findViewById(R.id.fabRemoveRow);
                final WorkoutData.ExerciseData exerciseData = new WorkoutData.ExerciseData();


                Cursor groupCursor = (Cursor) mSpnGroup.getSelectedItem();
                Cursor exerciseCursor = (Cursor) mSpnItem.getSelectedItem();

                if (groupCursor != null && exerciseCursor != null) {
                    final String strGroup = (String) groupCursor.getString(groupCursor.getColumnIndex(FitnessDBHelper.COL_NAME));
                    final String strExerciseName = (String) exerciseCursor.getString(exerciseCursor.getColumnIndex(FitnessDBHelper.COL_NAME));

                    fabRemoveRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mLayoutExercises.removeView(item);
                            mWorkoutData.lstExercises.remove(exerciseData);
                        }
                    });

                    exercise.setText(strGroup + ": " + strExerciseName);
                    final int noSets = mNumberPickerSets.getValue();
                    final int noReps = mNumberPickerReps.getValue();

                    sets.setText(String.valueOf(noSets));
                    reps.setText(String.valueOf(noReps));

                    //Add a new workout data list item

                    exerciseData.name = strExerciseName;
                    exerciseData.sets = noSets;
                    exerciseData.reps = noReps;
                    mWorkoutData.lstExercises.add(exerciseData);

                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    item.setLayoutParams(lparams);
                    mLayoutExercises.addView(item);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_exercise), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mExercisesDAO = null;
        mCategoryDAO = null;
    }
}
