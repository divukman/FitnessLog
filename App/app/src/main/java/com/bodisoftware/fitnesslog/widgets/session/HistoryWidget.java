package com.bodisoftware.fitnesslog.widgets.session;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bodisoftware.fitnesslog.R;
import com.bodisoftware.fitnesslog.database.dto.ExerciseHistory;
import com.bodisoftware.fitnesslog.widgets.DisplayUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dvukman on 10/4/2017.
 */

public class HistoryWidget extends LinearLayout {

    private Context mContext = null;
    private String mStrExerciseName = null;
    private ArrayList<ExerciseHistory> mLstSets = null;

    public HistoryWidget(final Context context, final String exerciseName, final ArrayList<ExerciseHistory> lstSets) {
        super(context);
        initializeViews(context, exerciseName, lstSets);
    }

    private void initializeViews(Context context, final String exerciseName, final ArrayList<ExerciseHistory> lstSets) {
        mContext = context;
        mStrExerciseName = exerciseName;
        mLstSets = lstSets;

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.history_exercise_widget, this);
        final TextView txtExerciseName = (TextView) view.findViewById(R.id.txtHistoryExerciseName);
        final TableLayout tblHistorySets = (TableLayout) view.findViewById(R.id.tblHistorySets);

        txtExerciseName.setText(exerciseName);
        txtExerciseName.setPadding(0,0,10,0);
        Iterator<ExerciseHistory> iterator = lstSets.iterator();

        /* Create a new row to be added. */
        TableRow tr = null;
        int columnCounter = 0;
        while (iterator.hasNext()) {
            final TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            tableLayoutParams.weight = 1;
            if (columnCounter == 0) {
                tr = new TableRow(mContext);
                tr.setLayoutParams(tableLayoutParams);
            }

            final ExerciseHistory set = (ExerciseHistory) iterator.next();
            final String str =  set.getReps() + (set.getWeight() > 0 ? "x" + (int)set.getWeight() : "");

            final TextView txtView = new TextView(mContext);
            txtView.setText(str);
            final TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.span = 1;
            txtView.setLayoutParams(params);
//            final float factor = DisplayUtils.getDIPFactor(mContext);
//            final int width = (int) (90 * factor);
//            txtView.setWidth(width);
            txtView.setGravity(Gravity.START);
            tr.addView(txtView);

            if (columnCounter == 4 || !iterator.hasNext()) { // 5 columns
                tblHistorySets.addView(tr, tableLayoutParams);
                columnCounter = 0;
            } else {
                columnCounter ++;
            }
        }

    }


}