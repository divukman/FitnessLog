package com.bodisoftware.fitnesslog.ui.tabs.routine;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bodisoftware.fitnesslog.Constants;
import com.bodisoftware.fitnesslog.MainActivity;
import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.dao.ExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dao.RoutineDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dto.Workout;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by dvukman on 1/17/2017.
 */

public class DisplayWorkoutsActivity extends AppCompatActivity implements  NewWorkoutDialogFragment.NoticeDialogListener {

    private int tabIndexToReturnTo = -1;
    private String mRoutineName = null;
    private long mRoutineId = -1;
    private AddFloatingActionButton mBtnAddSession = null;
    private DisplayWorkoutsRecyclerAdapter mRoutinesAdapter = null;

    private WorkoutDataSource mWorkoutDAO = null;
    private RoutineDataSource mRoutineDAO = null;
    private WorkoutExerciseDataSource mWorkoutExerciseDAO = null;
    private ExerciseDataSource mExerciseDAO = null;
    private List<Workout> mLstWorkouts = null;

    private RecyclerView mRecyclerView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_routine);

        mBtnAddSession = (AddFloatingActionButton) findViewById(R.id.btnAddSession);

        tabIndexToReturnTo = getIntent().getIntExtra(Constants.CURRENT_PAGE, -1);
        mRoutineName = getIntent().getStringExtra(Constants.ROUTINE_NAME);

        mWorkoutDAO = new WorkoutDataSource(getApplicationContext());
        mRoutineDAO = new RoutineDataSource(getApplicationContext());
        mWorkoutExerciseDAO = new WorkoutExerciseDataSource(getApplicationContext());
        mExerciseDAO = new ExerciseDataSource(getApplicationContext());
        mWorkoutDAO.open();
        mRoutineDAO.open();
        mWorkoutExerciseDAO.open();
        mExerciseDAO.open();


        mRoutineId = mRoutineDAO.getRoutineId(mRoutineName);
        mLstWorkouts = mWorkoutDAO.getAllWorkouts(mRoutineId);
        mRoutinesAdapter = new DisplayWorkoutsRecyclerAdapter(this, mLstWorkouts, mWorkoutExerciseDAO, mExerciseDAO);

        mRecyclerView = (RecyclerView) findViewById(R.id.routineRecyclerView);
        mRecyclerView.setAdapter(mRoutinesAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new SlideInDownAnimator());

        addListeners();
    }

    private void addListeners() {
        mBtnAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new NewWorkoutDialogFragment();
                ((NewWorkoutDialogFragment) dialog).setRoutineId(mRoutineId);
                ((NewWorkoutDialogFragment) dialog).setWorkoutDAO(mWorkoutDAO);
                dialog.show(getSupportFragmentManager(), "NewWorkoutDialogFragment");
            }
        });
    }


    @Override
    public Intent getSupportParentActivityIntent() { // getParentActivityIntent() if you are not using the Support Library
        final Bundle bundle = new Bundle();
        final Intent intent = new Intent(this, MainActivity.class);

        bundle.putInt(Constants.CURRENT_PAGE, tabIndexToReturnTo);
        intent.putExtras(bundle);

        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mExerciseDAO.close();
//        mWorkoutExerciseDAO.close();
//        mWorkoutDAO.close();
//        mRoutineDAO.close();
        mWorkoutDAO = null;
        mRoutineDAO = null;
        mWorkoutExerciseDAO = null;
        mExerciseDAO = null;
    }

    @Override
    public void onDialogPositiveClick(final WorkoutData workoutData) {
        final Workout workout = mWorkoutDAO.createWorkout(mRoutineId, workoutData.workoutName);
        for (int i=0; i<workoutData.lstExercises.size(); i++) {
            final WorkoutData.ExerciseData exerciseData = workoutData.lstExercises.get(i);

            final String exerciseName = exerciseData.name;
            final int sets = exerciseData.sets;
            final int reps = exerciseData.reps;
            final int rest = 90; //@todo

            final long exerciseId = mExerciseDAO.getExerciseId(exerciseName);
            mWorkoutExerciseDAO.createWorkoutExercise(workout.getId(), exerciseId, sets, reps, rest);
        }

        mRoutinesAdapter.getWorkoutsList().add(workout);
        mRoutinesAdapter.notifyItemInserted(mRoutinesAdapter.getWorkoutsList().size() - 1);
        mRecyclerView.smoothScrollToPosition(mRoutinesAdapter.getWorkoutsList().size() - 1);
    }

    @Override
    public void onDialogNegativeClick(final WorkoutData workoutData) {

    }
}
