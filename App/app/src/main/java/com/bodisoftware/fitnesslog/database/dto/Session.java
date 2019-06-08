package com.bodisoftware.fitnesslog.database.dto;

/**
 * Created by dvukman on 11/25/2016.
 */

public class Session extends AbstractDTO {
    private long date;
    private String notes;
    private long workout_id;

    public long getWorkout_id() {
        return workout_id;
    }

    public void setWorkout_id(long workout_id) {
        this.workout_id = workout_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
