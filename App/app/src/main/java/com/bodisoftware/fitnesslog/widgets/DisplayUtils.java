package com.bodisoftware.fitnesslog.widgets;

import android.content.Context;

/**
 * Created by dvukman on 8/22/2017.
 */

public final class DisplayUtils {

    public static float getDIPFactor(final Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

}
