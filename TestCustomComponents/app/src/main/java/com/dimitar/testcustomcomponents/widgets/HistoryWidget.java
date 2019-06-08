package com.dimitar.testcustomcomponents.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.dimitar.testcustomcomponents.R;

/**
 * Created by dvukman on 10/3/2017.
 */

public class HistoryWidget extends LinearLayout {

    private Context mContext = null;

    public HistoryWidget(final Context context, final) {
        super(context);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.history_exercise_widget_row, this);
    }


}
