package com.bodisoftware.fitnesslog.database.dto;

/**
 * Created by dvukman on 11/16/2016.
 * Exercise DTO.
 */

public class Exercise extends AbstractDTO {
    private long category_id;
    private String name;

    public long getCategoryId() {
        return category_id;
    }

    public void setCategoryId(final long id) {
        category_id = id;
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
