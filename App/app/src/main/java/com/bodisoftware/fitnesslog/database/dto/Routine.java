package com.bodisoftware.fitnesslog.database.dto;

/**
 * Created by dvukman on 11/25/2016.
 */

public class Routine extends AbstractDTO {
    private String name;

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
