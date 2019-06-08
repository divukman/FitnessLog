package com.bodisoftware.fitnesslog.database.dto;

/**
 * Created by dvukman on 11/16/2016.
 * Category DTO.
 */

public class Category extends AbstractDTO {
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
