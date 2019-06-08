package com.bodisoftware.fitnesslog.ui.tabs.excercises;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public String name;
    public final List<String> children = new ArrayList<String>();

    public Group(String string) {
        this.name = string;
    }
}