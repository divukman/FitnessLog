package com.bodisoftware.fitnesslog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dvukman on 9/22/2017.
 */

public class DateUtil {

    public static final SimpleDateFormat PARSE_FORMAT_DAY_AND_DATE = new SimpleDateFormat("EEEE dd. MMMM yyyy");
    public static final SimpleDateFormat PARSE_FORMAT_DATE_ONLY = new SimpleDateFormat("dd. MMMM yyyy");

    public static String getDateStr(final Date date, final SimpleDateFormat FORMAT) {
        return capitalizeFirstLetter(FORMAT.format(date));
    }

    private static String capitalizeFirstLetter(final String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
