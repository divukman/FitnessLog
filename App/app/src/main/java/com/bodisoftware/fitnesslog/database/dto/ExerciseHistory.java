package com.bodisoftware.fitnesslog.database.dto;

/**
 * Created by dvukman on 11/25/2016.
 */

public class ExerciseHistory extends AbstractDTO {
    private int set;
    private int reps;
    private int rest;
    private float weight;

    private long session_id;
    private long exercise_id;

    public int getSet() {
        return set;
    }

    public void setSet(int setNumber) {
        this.set = setNumber;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getSession_id() {
        return session_id;
    }

    public void setSessionId(long session_id) {
        this.session_id = session_id;
    }

    public long getExerciseId() {
        return exercise_id;
    }

    public void setExerciseId(long exercise_id) {
        this.exercise_id = exercise_id;
    }
}
