package com.bodisoftware.fitnesslog.ui.tabs.routine;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bodisoftware.fitnesslog.R;

/**
 * Created by dvukman on 1/19/2017.
 */

public class RoutineRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView mTxtViewWorkoutName;
    public TableLayout tblRoutine = null;


    public RoutineRecyclerViewHolder(View itemView) {
        super(itemView);

        mTxtViewWorkoutName = (TextView) itemView.findViewById(R.id.txtHeader);
        tblRoutine = (TableLayout) itemView.findViewById(R.id.tblRoutine);
    }

}