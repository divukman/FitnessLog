package com.bodisoftware.fitnesslog.ui.tabs.session;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bodisoftware.fitnesslog.Constants;
import com.bodisoftware.fitnesslog.MainActivity;
import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.FitnessDBHelper;
import com.bodisoftware.fitnesslog.database.dao.RoutineDataSource;
import com.bodisoftware.fitnesslog.database.dao.SessionDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutDataSource;
import com.bodisoftware.fitnesslog.database.dto.Routine;
import com.bodisoftware.fitnesslog.database.dto.Session;
import com.bodisoftware.fitnesslog.database.dto.Workout;
import com.bodisoftware.fitnesslog.ui.tabs.routine.NewWorkoutDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dvukman on 9/9/2017.
 */

public class NewSessionDialogFragment extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(SessionData sessionData);
        public void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    NewSessionDialogFragment.NoticeDialogListener mListener = null;
    private SessionData mSessionData = new SessionData();

    private final SimpleDateFormat PARSE_FORMAT = new SimpleDateFormat("EEEE dd. MMMM yyyy");
    private final SimpleDateFormat PARSE_FORMAT_DATE_ONLY = new SimpleDateFormat("dd. MMMM yyyy");
    private String STR_NONE = null;

    private View mRootView = null;
    private Cursor mRoutineCursor = null;
    private Cursor mWorkoutCursor = null;
    private RoutineDataSource mRoutineDAO = null;
    private SessionDataSource mSessionDAO = null;
    private WorkoutDataSource mWorkoutDAO = null;

    private TextView mTxtViewHeaderDate = null;
    private TextView mTxtNextSessionDate = null;
    private TextView mTxtLastRoutineName = null;
    private TextView mTxtLastWorkoutName = null;
    private TextView mTxtLastWorkoutDate = null;
    private Spinner mSpnRoutines = null;
    private Spinner mSpnWorkouts = null;


    public void setListener(final NewSessionDialogFragment.NoticeDialogListener listener) {
        mListener = listener;
    }

    //@todo move to util?
    public Fragment getVisibleFragment(final AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        STR_NONE = getResources().getString(R.string.none);
        String lastRoutineName = STR_NONE;
        String lastWorkoutName = STR_NONE;
        String lastWorkoutDate = STR_NONE;
        boolean lastSessionFound = false;
        String nextRoutineName = STR_NONE;
        String nextWorkoutName = STR_NONE;

        mRoutineDAO = new RoutineDataSource(getContext());
        mRoutineDAO.open();

        mSessionDAO = new SessionDataSource(getContext());
        mSessionDAO.open();

        mWorkoutDAO = new WorkoutDataSource(getContext());
        mWorkoutDAO.open();

        // Get the layout inflater
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mRootView = inflater.inflate(R.layout.dialog_new_session, null);

        // References to dialog widgets
        mTxtViewHeaderDate = (TextView) mRootView.findViewById(R.id.txtViewHeaderDate);
        mTxtNextSessionDate = (TextView) mRootView.findViewById(R.id.txtNextSessionDate);
        mTxtLastRoutineName = (TextView) mRootView.findViewById(R.id.txtLastRoutineName);
        mTxtLastWorkoutName = (TextView) mRootView.findViewById(R.id.txtLastWorkoutName);
        mTxtLastWorkoutDate = (TextView) mRootView.findViewById(R.id.txtLastWorkoutDate);
        mSpnRoutines = (Spinner) mRootView.findViewById(R.id.spnRoutines);
        mSpnWorkouts = (Spinner) mRootView.findViewById(R.id.spnWorkouts);

        final Session lastSession = mSessionDAO.getLastSession();
        if (lastSession != null) {
            lastSessionFound = true;
            final long workoutID = lastSession.getWorkout_id();
            final long workoutDate = lastSession.getDate();
            final String workoutNotes = lastSession.getNotes();

            final Workout workout = mWorkoutDAO.getWorkout(workoutID);
            final String workoutName = workout.getName();
            final long routineID = workout.getRoutineId();

            final Routine routine = mRoutineDAO.getRoutine(routineID);
            final String routineName = routine.getName();

            Date date = new Date(workoutDate);
            lastWorkoutName = workoutName;
            lastWorkoutDate = getDateStr(date, PARSE_FORMAT_DATE_ONLY);
            lastRoutineName = routineName;
            nextRoutineName = lastRoutineName;
            Workout nextWorkout = mWorkoutDAO.getNextWorkout(routineID, workoutID);
            if (nextWorkout != null) {
                nextWorkoutName = nextWorkout.getName();
            }
        }

        mTxtLastRoutineName.setText(lastRoutineName);
        mTxtLastWorkoutName.setText(lastWorkoutName);
        mTxtLastWorkoutDate.setText(lastWorkoutDate);

        Date date = new Date();
        mTxtViewHeaderDate.setText(getDateStr(date, PARSE_FORMAT));
        mTxtNextSessionDate.setText(getDateStr(date, PARSE_FORMAT_DATE_ONLY));

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
                        dialog.dismiss();

                        mListener.onDialogPositiveClick(mSessionData);//@todo
                    }
                });

                final Button btnCancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        mListener.onDialogNegativeClick();
                    }
                });
            }
        });

        createSpinnerAdaptersAndListeners(nextWorkoutName);

        if(!nextRoutineName.equalsIgnoreCase(STR_NONE)) {
            preselectSpinner(mSpnRoutines, nextRoutineName);
        }

        return dialog;
    }

    private void createSpinnerAdaptersAndListeners(final String nextWorkoutName) {
        final String [] from = new String[] {FitnessDBHelper.COL_NAME};
        final int[] to = new int[] {android.R.id.text1};

        mRoutineCursor = mRoutineDAO.getAllRoutinesCursor();
        Cursor newRoutineCursor = modifyRoutinesCursor(mRoutineCursor);
        SimpleCursorAdapter adapterRoutines = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, newRoutineCursor, from, to, 0);
        adapterRoutines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnRoutines.setAdapter(adapterRoutines);


        mSpnWorkouts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                mSessionData.workoutId = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpnRoutines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mWorkoutCursor = mWorkoutDAO.getWorkoutsCursor(id);
                SimpleCursorAdapter adapterWorkouts = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, mWorkoutCursor, from, to, 0);
                adapterWorkouts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpnWorkouts.setAdapter(adapterWorkouts);

                mSessionData.routineId = id;
                if(nextWorkoutName != null) {
                    preselectSpinner(mSpnWorkouts, nextWorkoutName);
                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //@todo move to util
    private void preselectSpinner(final Spinner spinner, final String value) {
        final SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            final Object obj = (Object) adapter.getItem(i);
            String result = null;
            if (obj instanceof SQLiteCursor) {
                result = ((Cursor)obj).getString(2);
            } else if (obj instanceof Cursor) {
                result = ((Cursor)obj).getString(1);
            } else {
                result = obj.toString();
            }

            if (result.equalsIgnoreCase(value)) {
                spinner.setSelection(i);
            }
        }
    }


    //@todo move to util
    private String capitalizeFirstLetter(final String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    //@todo move to util
    private String getDateStr(final Date date, final SimpleDateFormat FORMAT) {
        return capitalizeFirstLetter(FORMAT.format(date));
    }

    /**
     * Takes in a routines cursor and adds one more row at the top.
     * Row is for NO-ROUTINE when you add exercises as you go.
     * @return
     */
    private Cursor modifyRoutinesCursor(final Cursor cursor) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[] {FitnessDBHelper.COLUMN_ID, FitnessDBHelper.COL_NAME});
        matrixCursor.addRow(new Object[] {0, Constants.NO_ROUTINE});

        MergeCursor mergeCursor = new MergeCursor( new Cursor[] {matrixCursor, cursor});
        return mergeCursor;
    }

    @Override
    public void onStop() {
        super.onStop();
        mRoutineCursor = null;
        mWorkoutCursor = null;
        mRoutineDAO = null;
        mSessionDAO = null;
        mWorkoutDAO = null;
    }
}
