package com.bodisoftware.fitnesslog.ui.tabs.session;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bodisoftware.fitnesslog.Constants;
import com.bodisoftware.fitnesslog.MainActivity;
import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.dao.ExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dao.ExerciseHistoryDataSource;
import com.bodisoftware.fitnesslog.database.dao.SessionDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dto.Exercise;
import com.bodisoftware.fitnesslog.database.dto.ExerciseHistory;
import com.bodisoftware.fitnesslog.database.dto.Session;
import com.bodisoftware.fitnesslog.database.dto.WorkoutExercise;
import com.bodisoftware.fitnesslog.ui.tabs.routine.WorkoutData;
import com.bodisoftware.fitnesslog.widgets.session.ExerciseWidget;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dvukman on 9/18/2017.
 */

public class NewSessionActivity extends AppCompatActivity implements  AddExerciseDialogFragment.NoticeDialogListener {
    private static final LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private int tabIndexToReturnTo = -1;
    private SessionData mNewSessionData = null;
    LinearLayout mExercisesLayout = null;
    private SessionDataSource mSessionDAO = null;
    private WorkoutExerciseDataSource mWorkoutExerciseDAO = null;
    private ExerciseHistoryDataSource mExerciseHistoryDAO = null;
    private ExerciseDataSource mExerciseDAO = null;

    private ArrayList<ExerciseWidget> mLstExerciseWidget = new ArrayList<>();
    private Button mBtnSaveAndFinish = null;
    private Button mBtnAddExercise = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndexToReturnTo = getIntent().getIntExtra(Constants.CURRENT_PAGE, -1);
        mNewSessionData = (SessionData) getIntent().getSerializableExtra(Constants.DATA);

        setContentView(R.layout.activity_new_session);
        mExercisesLayout = (LinearLayout) findViewById(R.id.verticalLayout);
        mBtnSaveAndFinish = (Button) findViewById(R.id.btnSaveAndFinish);
        mBtnAddExercise = (Button) findViewById(R.id.btnAddExerciseToSession);

        mSessionDAO = new SessionDataSource(getApplicationContext());
        mSessionDAO.open();

        mWorkoutExerciseDAO = new WorkoutExerciseDataSource(getApplicationContext());
        mWorkoutExerciseDAO.open();

        mExerciseHistoryDAO = new ExerciseHistoryDataSource(getApplicationContext());
        mExerciseHistoryDAO.open();

        mExerciseDAO = new ExerciseDataSource(getApplicationContext());
        mExerciseDAO.open();

        initializeWidgets();
        addListeners();
    }

    private void addListeners() {
        mBtnSaveAndFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialDialog.Builder(NewSessionActivity.this)
                        .title(R.string.save_session)
                        .content(R.string.save_session_text)
                        .positiveText(R.string.save)
                        .negativeText(R.string.cancel)
                        .iconRes(R.drawable.ic_save_black_48dp)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                saveAndFinish();
                            }
                        })
                        .show();
            }
        });

        mBtnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new AddExerciseDialogFragment();
                dialog.show(getSupportFragmentManager(), "AddExerciseDialogFragment");
            }
        });
    }

    private void initializeWidgets() {


        /** README!
         * mNewSessionData can be empty. This means that user has chosen a CUSTOM workout and will add exercises during the
         * workout. We need to check for these conditions to know how to proceed.
         *
         * If user has chosen a workout from previously added Routines/Workouts, then we need to:
         * 1. Check history and continue where user has stopped last time (same exercises, weights, repetitions)
         * 2. If there is nothing in the history, then we load the data from the stored Routine/Workout template.
         */

        // If there is new session data, then this is a stored workout template
        if (mNewSessionData != null && mNewSessionData.routineId > 0 && mNewSessionData.workoutId > 0) { // 0 is a custom routine (NO ROUTINE)
            // Each workout has a number of exercises.
            // Each exercise has a number of sets and reps!
            // 1. Search the last workout done in sessions history table, if found, use ALL! data from there
            // 2. If this is the first time workout is used, get data from workout-exercise table

            final Session lastSessionForThisWorkout = mSessionDAO.getLastSessionForWorkout(mNewSessionData.workoutId);
            if (lastSessionForThisWorkout != null) {
                // Get the data from the history
                final long lastSessionId = lastSessionForThisWorkout.getId();
                mExerciseHistoryDAO.open();
                final List<ExerciseHistory> lstExerciseHistory = mExerciseHistoryDAO.getExerciseHistory(lastSessionId);
                final ArrayList<ExerciseHistory> lstTemp = new ArrayList<ExerciseHistory>();
                if (lstExerciseHistory.size() > 0) {
                    Iterator<ExerciseHistory> iterator = lstExerciseHistory.iterator();

                    while (iterator.hasNext()) {
                        final ExerciseHistory nextEntry = iterator.next();

                        if (lstTemp.size() == 0) {
                            lstTemp.add(nextEntry);
                        } else {
                            final ExerciseHistory firstEntry = lstTemp.get(0);
                            if (nextEntry.getExerciseId() == firstEntry.getExerciseId()) {
                                lstTemp.add(nextEntry);
                            } else {
                                addExerciseWidgetFromHistory(lstTemp);

                                lstTemp.clear();
                                lstTemp.add(nextEntry);
                            }
                        }

                    }

                    if (lstTemp.size() > 0) {
                        addExerciseWidgetFromHistory(lstTemp);

                        lstTemp.clear();
                    }
                }

                //Cursor cursorHistory = mExerciseHistoryDAO.getExerciseHistory(lastSessionId);
                //@todo dalje vidit sta vraca i kako kombinirat to za kreirat kontrole
            } else {
                // Get the data from the template
                final long workoutId = mNewSessionData.workoutId;
                List<WorkoutExercise> lstWorkoutExercise = mWorkoutExerciseDAO.getAllWorkoutExercises(workoutId);
                Iterator<WorkoutExercise> iterator = lstWorkoutExercise.iterator();
                while (iterator.hasNext()) {
                    WorkoutExercise workoutExercise = (WorkoutExercise) iterator.next();
                    final Exercise exercise = mExerciseDAO.getExercise(workoutExercise.getExerciseId());

                    final String header = exercise.getName();
                    final int reps = workoutExercise.getReps();
                    final int sets = workoutExercise.getSets();
                    final String setTitle = getResources().getString(R.string.set);

                    ExerciseWidget exerciseWidget = new ExerciseWidget(getApplicationContext());
                    exerciseWidget.exerciseID = exercise.getId();
                    exerciseWidget.setLayoutParams(LP);
                    exerciseWidget.setHeader(header);

                    for (int i = 0; i < sets; i++) {
                        final int setNumber = i + 1;
                        exerciseWidget.addSet(setNumber, setTitle + " " + setNumber, 0, reps);
                    }

                    exerciseWidget.addAllDone();
                    final int addIndex = mExercisesLayout.getChildCount() - 2; //append to last -2 (last two are buttons is the save button)
                    mExercisesLayout.addView(exerciseWidget, addIndex);
                    mLstExerciseWidget.add(exerciseWidget);
                }
            }

        } else {
            // If there is no new session data, then this is a custom (on the fly) workout

        }

//        ExerciseWidget exerciseWidget = new ExerciseWidget(getApplicationContext());
//        exerciseWidget.setLayoutParams(lp);
//        exerciseWidget.setHeader("Pull ups");
//        exerciseWidget.addSet("Warmup set", 0, 5);
//        exerciseWidget.addSet("Work set", 0, 7);
//        exerciseWidget.addSet("Heavy", 0, 10);
//        exerciseWidget.addSet("Rest set", 0, 5);
//        exerciseWidget.addSet("Final set", 0, 10);
//        exerciseWidget.addAllDone();
//        mExercisesLayout.addView(exerciseWidget);
    }

    private void addExerciseWidgetFromHistory(final ArrayList<ExerciseHistory> lstTemp) {
        final String setTitle = getResources().getString(R.string.set);
        mExerciseDAO.open();
        ExerciseHistory firstExerciseHistoryEntry = lstTemp.get(0);
        final Exercise exercise = mExerciseDAO.getExercise(firstExerciseHistoryEntry.getExerciseId());
        final String exerciseName = exercise.getName();
        final long exerciseId = exercise.getId();

        ExerciseWidget exerciseWidget = new ExerciseWidget(getApplicationContext());
        exerciseWidget.exerciseID = exercise.getId();
        exerciseWidget.setLayoutParams(LP);
        exerciseWidget.setHeader(exerciseName);

        for (int i = 0; i < lstTemp.size(); i++) {
            final int setNumber = i + 1;
            ExerciseHistory item = lstTemp.get(i);
            exerciseWidget.addSet(setNumber, setTitle + " " + setNumber, item.getWeight(), item.getReps());
        }

        exerciseWidget.addAllDone();
        final int addIndex = mExercisesLayout.getChildCount() - 2; //append to -2 (last two are buttons)
        mExercisesLayout.addView(exerciseWidget, addIndex);
        mLstExerciseWidget.add(exerciseWidget);
    }

    @Override
    public Intent getSupportParentActivityIntent() { // getParentActivityIntent() if you are not using the Support Library
        final Bundle bundle = new Bundle();
        final Intent intent = new Intent(this, MainActivity.class);

        bundle.putInt(Constants.CURRENT_PAGE, tabIndexToReturnTo);
        intent.putExtras(bundle);

        return intent;
    }

    /**
     * Saves the data and finishes the activity.
     */
    private void saveAndFinish() {
        final long workoutID = mNewSessionData.workoutId;
        final long date = (new Date()).getTime();
        final Session session = mSessionDAO.createSession(workoutID, null, date);

        final Iterator<ExerciseWidget> iterator = mLstExerciseWidget.iterator();
        while (iterator.hasNext()) {
            final ExerciseWidget exerciseWidget = (ExerciseWidget) iterator.next();
            final long exerciseID = exerciseWidget.exerciseID;

            ArrayList<ExerciseWidget.Set> sets = exerciseWidget.getSets();
            Iterator<ExerciseWidget.Set> iterSet = sets.iterator();
            while (iterSet.hasNext()) {
                final ExerciseWidget.Set set = iterSet.next();
                final int setNumber = set.setNumber;
                final int reps = set.done ? set.reps : 0;
                final float weight = set.done ? set.weight : 0;

                mExerciseHistoryDAO.createExerciseHistory(
                        session.getId(),
                        exerciseID,
                        setNumber,
                        reps,
                        0,
                        weight);
            }
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", Boolean.TRUE);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mSessionDAO = null;
        mWorkoutExerciseDAO = null;
        mExerciseHistoryDAO = null;
        mExerciseDAO = null;
    }

    @Override
    public void onDialogPositiveClick(WorkoutData.ExerciseData exerciseData) {
        final String setTitle = getResources().getString(R.string.set);
        ExerciseWidget exerciseWidget = new ExerciseWidget(getApplicationContext());
        exerciseWidget.exerciseID = exerciseData.exerciseId;
        exerciseWidget.setLayoutParams(LP);
        exerciseWidget.setHeader(exerciseData.name);

        for (int i = 0; i < exerciseData.sets; i++) {
            final int setNumber = i + 1;
            exerciseWidget.addSet(setNumber, setTitle + " " + setNumber, 0, 0);
        }

        exerciseWidget.addAllDone();
        final int addIndex = mExercisesLayout.getChildCount() - 2; //append to last -2 (last two are buttons is the save button)
        mExercisesLayout.addView(exerciseWidget, addIndex);
        mLstExerciseWidget.add(exerciseWidget);
    }
}
