package com.bodisoftware.fitnesslog.ui.tabs.routine;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by dvukman on 7/29/2017.
 *
 * Simple class to hold workout related data, such as
 * workout name and a list of exercises.
 **/

public class WorkoutData {

    public static class ExerciseData {
        public int sets;
        public int reps;
        public String name;
        public long exerciseId; //used only when adding exercise to new session activity
    }

    public String workoutName;
    public ArrayList<ExerciseData> lstExercises = new ArrayList<ExerciseData>();

}
