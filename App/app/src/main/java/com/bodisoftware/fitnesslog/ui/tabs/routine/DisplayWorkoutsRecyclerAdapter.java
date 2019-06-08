package com.bodisoftware.fitnesslog.ui.tabs.routine;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.dao.ExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dao.SessionDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutDataSource;
import com.bodisoftware.fitnesslog.database.dao.WorkoutExerciseDataSource;
import com.bodisoftware.fitnesslog.database.dto.Exercise;
import com.bodisoftware.fitnesslog.database.dto.Workout;
import com.bodisoftware.fitnesslog.database.dto.WorkoutExercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvukman on 1/19/2017.
 */

public class DisplayWorkoutsRecyclerAdapter extends RecyclerView.Adapter<RoutineRecyclerViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List <Workout> mLstWorkouts = null;
    private WorkoutExerciseDataSource mWorkoutExerciseDAO = null;
    private ExerciseDataSource mExerciseDAO = null;

    public DisplayWorkoutsRecyclerAdapter(Context context, final List<Workout> lstWorkouts, final WorkoutExerciseDataSource workoutExerciseDataSource, final ExerciseDataSource exerciseDAO) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        mLstWorkouts = lstWorkouts;
        mWorkoutExerciseDAO = workoutExerciseDataSource;
        mExerciseDAO = exerciseDAO;
    }

    @Override
    public RoutineRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.card_routine, parent, false);
        RoutineRecyclerViewHolder viewHolder = new RoutineRecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RoutineRecyclerViewHolder holder, int position) {

        final Workout workout = mLstWorkouts.get(position);
        final long workoutId = workout.getId();
        final List<WorkoutExercise> lstWorkoutExercises = mWorkoutExerciseDAO.getAllWorkoutExercises(workoutId);

        holder.mTxtViewWorkoutName.setText(workout.getName());
        //  holder.imageView.setOnClickListener(clickListener);
        //   holder.imageView.setTag(holder);

        TableLayout tblRoutine = holder.tblRoutine;
        tblRoutine.removeAllViews();

        //Get exercises for this workout and fill out the table
        for (int i = 0; i < lstWorkoutExercises.size(); i++) {
            View row = inflater.inflate(R.layout.card_routine_row, tblRoutine, false);

            TextView txtExerciseName = (TextView) row.findViewById(R.id.txtRowExerciseName);
            TextView txtSets = (TextView) row.findViewById(R.id.txtRowNoOfSets);
            TextView txtReps = (TextView) row.findViewById(R.id.txtRowNoOrReps);

            final WorkoutExercise workoutExercise = lstWorkoutExercises.get(i);
            final int sets = workoutExercise.getSets();
            final int reps = workoutExercise.getReps();
            final int rest = workoutExercise.getRest();
            final Exercise exercise = mExerciseDAO.getExercise(workoutExercise.getExerciseId()); //should never be null!
            final String exerciseName = exercise.getName();

            txtExerciseName.setText(exerciseName);
            txtSets.setText(sets + " " + mContext.getResources().getString(R.string.sets));
            txtReps.setText(reps + " " + mContext.getResources().getString(R.string.reps));

            if (i % 2 == 1) {
                txtExerciseName.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                txtSets.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                txtReps.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                row.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
            }
            tblRoutine.addView(row);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RoutineRecyclerViewHolder vholder = (RoutineRecyclerViewHolder) v.getTag();
            int position = vholder.getPosition();


            Toast.makeText(mContext, "This is position " + position, Toast.LENGTH_LONG).show();

        }
    };


    @Override
    public int getItemCount() {
        return mLstWorkouts.size();
    }

    public List<Workout> getWorkoutsList() {
        return mLstWorkouts;
    }

}
