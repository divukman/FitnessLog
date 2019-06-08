package com.dimitar.testcustomcomponents.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dimitar.testcustomcomponents.R;

import java.util.ArrayList;

/**
 * Created by dvukman on 8/8/2017.
 */

public class ExerciseWidget extends LinearLayout {

    public static class Set {
        public String name;
        public float weight;
        public int reps;
        public boolean done;
    }

    private Context mContext = null;
    private TextView mTxtHeader = null;
    private LinearLayout mLayoutSets = null;

    private ArrayList<SetWidget> mLstSetWidgets = new ArrayList<SetWidget>();
    private ArrayList<Set> mLstSets = new ArrayList<Set>();


    public ExerciseWidget(Context context) {
        super(context);
        initializeViews(context);
    }

    public ExerciseWidget(Context context, AttributeSet attrs) {
        super(context,attrs);
        initializeViews(context);
    }

    public ExerciseWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.exercise_widget, this);

        mContext = context;
        mTxtHeader = (TextView) view.findViewById(R.id.txtHeader);
        mLayoutSets = (LinearLayout) view.findViewById(R.id.setsLayout);
    }

    public void setHeader(final String header) {
        mTxtHeader.setText(header);
    }

    public void addSet(final String setHeader, final float weight, final int reps ) {
        SetWidget setWidget = new SetWidget(mContext);
        //@todo: move to builder pattern?
        setWidget.setName(setHeader);
        setWidget.setWeight(weight);
        setWidget.setReps(reps);

        final float factor = DisplayUtils.getDIPFactor(mContext);
        final int width = (int) (100 * factor);
        final int height = (int) (219 * factor);
        final int margin = (int) (12 * factor);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.setMargins(margin, 0, margin, 0);
        setWidget.setLayoutParams(lp);

        mLstSetWidgets.add(setWidget);
        mLayoutSets.addView(setWidget);
    }

    public void addAllDone() {
        final ToggleButton toggleButton = new ToggleButton(mContext);
        final float factor = DisplayUtils.getDIPFactor(mContext);
        final int width = (int) (60 * factor);
        final int height = (int) (60 * factor);
        final int margin = (int) (12 * factor);

        toggleButton.setTextColor(Color.WHITE);
        toggleButton.setTextOff("");
        toggleButton.setTextOn("");
        toggleButton.setText("");
        toggleButton.setBackgroundResource(R.drawable.toggle_alldone_background);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.setMargins(margin, 0, margin, 0);
        lp.gravity = Gravity.CENTER_VERTICAL;
        toggleButton.setLayoutParams(lp);
        mLayoutSets.addView(toggleButton);

        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean allDone = toggleButton.isChecked() ? true : false;
                for (SetWidget setWidget : mLstSetWidgets) {
                    setWidget.markSetFinished(allDone);
                }
            }
        });
    }

    public ArrayList<Set> getSets() {
        mLstSets.clear();

        for (SetWidget setWidget : mLstSetWidgets) {
            Set set = new Set();
            set.name = setWidget.getName();
            set.done = setWidget.isDone();
            set.weight = setWidget.getWeight();
            set.reps = setWidget.getReps();
            mLstSets.add(set);
        }

        return mLstSets;
    }
}
