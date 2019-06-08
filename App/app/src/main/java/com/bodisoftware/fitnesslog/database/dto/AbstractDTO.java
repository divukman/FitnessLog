package com.bodisoftware.fitnesslog.database.dto;

/**
 * Created by dvukman on 11/25/2016.
 */

public abstract class AbstractDTO {
    private long id;

    public long getId () {
        return id;
    }

    public void setId (final long id) {
        this.id = id;
    }
}
