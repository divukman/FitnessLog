package com.bodisoftware.fitnesslog.database.dto;

/**
 * Created by dvukman on 11/25/2016.
 */

public class Workout extends AbstractDTO {
    private long routine_id;
    private String name;

    public long getRoutineId() {
        return routine_id;
    }

    public void setRoutineId(final long id) {
        routine_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
