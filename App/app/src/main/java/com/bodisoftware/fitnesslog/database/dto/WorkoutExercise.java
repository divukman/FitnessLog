package com.bodisoftware.fitnesslog.database.dto;

/**
 * Created by dvukman on 11/25/2016.
 */

public class WorkoutExercise extends AbstractDTO {
    private int reps;
    private int sets;
    private int rest;

    private long workout_id;
    private long exercise_id;

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public long getWorkoutId() {
        return workout_id;
    }

    public void setWorkoutId(long workout_id) {
        this.workout_id = workout_id;
    }

    public long getExerciseId() {
        return exercise_id;
    }

    public void setExerciseId(long exercise_id) {
        this.exercise_id = exercise_id;
    }
}
